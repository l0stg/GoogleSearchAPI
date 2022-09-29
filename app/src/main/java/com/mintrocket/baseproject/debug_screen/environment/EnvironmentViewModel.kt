package com.mintrocket.baseproject.debug_screen.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentEntry
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentRepository
import com.mintrocket.datacore.Event
import kotlinx.coroutines.flow.*
import timber.log.Timber

class EnvironmentViewModel(
    private val environmentRepository: EnvironmentRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(EnvironmentScreenState())
    val screenState = _screenState.asStateFlow()

    private val _errorEvent = MutableLiveData<Event<String>>()
    val errorEvent: LiveData<Event<String>> = _errorEvent

    init {

        environmentRepository.observeEntries()
            .combine(environmentRepository.observeSelectedEntry()) { entries, selected ->
                EnvironmentScreenState(entries, selected.id)
            }
            .onEach { _screenState.value = it }
            .launchIn(viewModelScope)
    }

    fun onSelect(entry: EnvironmentEntry) {
        runCatching {
            environmentRepository.selectEntry(entry.id)
        }.onFailure {
            showError(it)
        }
    }

    fun onDelete(entry: EnvironmentEntry) {
        runCatching {
            environmentRepository.removeEntry(entry.id)
        }.onFailure {
            showError(it)
        }
    }

    private fun showError(throwable: Throwable) {
        Timber.e(throwable)
        _errorEvent.value = Event(throwable.message ?: "Unknown error message")
    }
}