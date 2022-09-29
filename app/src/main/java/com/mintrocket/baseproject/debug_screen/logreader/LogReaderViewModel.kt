package com.mintrocket.baseproject.debug_screen.logreader

import android.os.Process
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.baseproject.debug_screen.logreader.data.LogCatFilter
import com.mintrocket.baseproject.debug_screen.logreader.data.LogCatLine
import com.mintrocket.baseproject.debug_screen.logreader.data.LogReaderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class LogReaderViewModel(
    private val repository: LogReaderRepository
) : ViewModel() {

    private val _progress = MutableStateFlow(true)

    private val _logLines = MutableStateFlow<List<LogCatLine>>(emptyList())

    private val _filterFlow = MutableStateFlow(
        LogCatFilter(
            "",
            listOf(
                Log.DEBUG,
                Log.ERROR,
                Log.INFO,
                Log.WARN,
                Log.VERBOSE,
                Log.ASSERT
            ),
            true
        )
    )

    val progress: StateFlow<Boolean> = _progress.asStateFlow()

    val logLines: StateFlow<List<LogCatLine>> = _logLines.asStateFlow()

    val filterFlow: StateFlow<LogCatFilter> = _filterFlow

    init {
        repository.observeLogs()
            .combine(filterFlow, ::combineLogsWithFilter)
            .map { it.asReversed() }
            .flowOn(Dispatchers.IO)
            .onEach {
                _progress.value = false
                _logLines.value = it
            }
            .launchIn(viewModelScope)
    }

    fun setQuery(query: String) {
        _filterFlow.value = filterFlow.value.copy(query = query)
    }

    fun setOnlyLatestPid(enabled: Boolean) {
        _filterFlow.value = filterFlow.value.copy(onlyLatestPid = enabled)
    }

    fun setFilterLevels(levels: List<Int>) {
        _filterFlow.value = filterFlow.value.copy(levels = levels)
    }

    fun onDeleteAllLogsClick() {
        viewModelScope.launch {
            runCatching {
                repository.clear()
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private fun combineLogsWithFilter(
        rawLogs: List<LogCatLine>,
        filter: LogCatFilter
    ): List<LogCatLine> = rawLogs
        .filterByPid(filter.onlyLatestPid)
        .filterByLevels(filter.levels)
        .filterByQuery(filter.query)

    private fun List<LogCatLine>.filterByPid(onlyLatestPid: Boolean): List<LogCatLine> {
        val currentPid = Process.myPid()
        return if (onlyLatestPid) {
            filter { it.pid == currentPid }
        } else {
            this
        }
    }

    private fun List<LogCatLine>.filterByLevels(levels: List<Int>): List<LogCatLine> {
        return filter { levels.contains(it.level) }
    }

    private fun List<LogCatLine>.filterByQuery(query: String): List<LogCatLine> {
        return if (query.isNotEmpty()) {
            filter {
                it.tag.contains(query, true) || it.message.contains(query, true)
            }
        } else {
            this
        }
    }
}