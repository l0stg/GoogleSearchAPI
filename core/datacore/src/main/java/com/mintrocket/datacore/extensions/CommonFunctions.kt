package com.mintrocket.datacore.extensions

// when smart cast doesn't work
inline fun <A, B, R> ifNotNull(a: A?, b: B?, code: (A, B) -> R): R? {
    return if (a != null && b != null) {
        code(a, b)
    } else null
}

fun <KEY, VALUE> MutableMap<KEY, VALUE>.putNotNull(vararg entries: Pair<KEY, VALUE?>): MutableMap<KEY, VALUE> {
    entries.forEach { entry ->
        entry.second?.let { this[entry.first] = it }
    }
    return this
}

// for exhaustive when expressions
val <T> T.exhaustive: T
    get() = this