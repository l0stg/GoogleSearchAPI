package com.mintrocket.baseproject.debug_screen.logreader.data

import com.mintrocket.datacore.utils.Trigger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.util.*

class LogReaderRepository {

    companion object {
        private const val DEBOUNCE_MS = 500L
    }

    private val parser = LogParser()
    private val logs = Collections.synchronizedList(LinkedList<LogCatLine>())
    private val triggerFlow = MutableStateFlow(Trigger)

    private val rawLogsFlow = getLogsStreamFlow()
        .mapNotNull { parser.parse(it) }
        .catch { Timber.e(it) }
        .onEach { logs.add(it) }
        .debounce(DEBOUNCE_MS)
        .combine(triggerFlow) { logs, trigger -> logs }
        .map { logs.toList() }
        .flowOn(Dispatchers.IO)

    fun observeLogs(): Flow<List<LogCatLine>> = rawLogsFlow

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            Runtime.getRuntime().exec("logcat -c")
            logs.clear()
            triggerFlow.value = Trigger
        }
    }

    private fun getLogsStream(): BufferedReader {
        return Runtime.getRuntime().exec("logcat").inputStream.bufferedReader()
    }

    private fun getLogsStreamFlow(): Flow<String> = flow {
        val stream = getLogsStream()
        stream.useLines { lines ->
            lines.forEach {
                emit(it)
            }
        }
    }
}