package com.mintrocket.uicore.livedata

import androidx.lifecycle.MediatorLiveData

class SafeMediatorLiveData<T>(value: T) : MediatorLiveData<T>() {

    init {
        super.setValue(value)
    }

    override fun getValue(): T = super.getValue() as T
}