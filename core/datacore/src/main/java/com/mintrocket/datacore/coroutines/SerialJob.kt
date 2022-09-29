package com.mintrocket.datacore.coroutines

import kotlinx.coroutines.Job

class SerialJob {

    private var currentJob: Job? = null

    fun set(job: Job) {
        currentJob?.takeIf { !it.isCancelled }?.cancel()
        currentJob = job
    }

    fun get(): Job? = currentJob

    fun cancel() {
        currentJob?.takeIf { !it.isCancelled }?.cancel()
    }
}