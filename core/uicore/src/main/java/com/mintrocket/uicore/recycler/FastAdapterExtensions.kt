package com.mintrocket.uicore.recycler

import com.mikepenz.fastadapter.GenericItem

private const val SHIFT_MULTIPLIER = 31

fun GenericItem.generateId(vararg field: Any): Long {
    var identifier = type.toLong()
    field.forEach {
        identifier = identifier * SHIFT_MULTIPLIER + it.hashCode()
    }
    return identifier
}