package com.mintrocket.baseproject.debug_screen.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentRepository
import com.mintrocket.datacore.Event
import timber.log.Timber

class EnvironmentAddViewModel(
    private val environmentRepository: EnvironmentRepository
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _errorEvent = MutableLiveData<Event<String>>()
    val errorEvent: LiveData<Event<String>> = _errorEvent

    fun onSaveClick(entryId: String?, name: String, url: String) {
        runCatching {
            if (entryId != null) {
                environmentRepository.updateEntry(entryId, name, url)
            } else {
                environmentRepository.addEntry(name, url)
            }
            _closeEvent.value = Event(Unit)
        }.onFailure {
            Timber.e(it)
            _errorEvent.value = Event(it.message ?: "Unknown error message")
        }
    }

    fun onCloseClick() {
        _closeEvent.value = Event(Unit)
    }
}