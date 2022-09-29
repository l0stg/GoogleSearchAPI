package com.mintrocket.uicore.livedata

import androidx.lifecycle.LiveData

class SafeMutableLiveData<T>(value: T) : LiveData<T>() {
    init {
        super.setValue(value)
    }

    override fun getValue(): T = super.getValue() as T
    public override fun setValue(value: T) = super.setValue(value)
    public override fun postValue(value: T) = super.postValue(value)
}