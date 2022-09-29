package com.mintrocket.datacore.extensions

import java.util.*

fun Date.isSameDay(other: Date): Boolean {
    val thisCalendar = Calendar.getInstance()
    thisCalendar.time = this
    val otherCalendar = Calendar.getInstance()
    otherCalendar.time = other

    return thisCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR)
            && thisCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)
            && thisCalendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.isSameOrLessDay(other: Date): Boolean {
    val thisCalendar = Calendar.getInstance()
    thisCalendar.time = this
    val otherCalendar = Calendar.getInstance()
    otherCalendar.time = other

    return thisCalendar.get(Calendar.YEAR) <= otherCalendar.get(Calendar.YEAR)
            && thisCalendar.get(Calendar.MONTH) <= otherCalendar.get(Calendar.MONTH)
            && thisCalendar.get(Calendar.DAY_OF_MONTH) <= otherCalendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.isDayBefore(other: Date): Boolean {
    val thisCalendar = Calendar.getInstance()
    thisCalendar.time = this
    val otherCalendar = Calendar.getInstance()
    otherCalendar.time = other

    val thisYear = thisCalendar.get(Calendar.YEAR)
    val otherYear = otherCalendar.get(Calendar.YEAR)
    val thisMonth = thisCalendar.get(Calendar.MONTH)
    val otherMonth = otherCalendar.get(Calendar.MONTH)
    val thisDay = thisCalendar.get(Calendar.DAY_OF_MONTH)
    val otherDay = otherCalendar.get(Calendar.DAY_OF_MONTH)

    return thisYear < otherYear
            || (thisYear == otherYear && thisMonth < otherMonth)
            || (thisYear == otherYear && thisMonth == otherMonth && thisDay < otherDay)
}

fun Date.addDay(days: Int = 1): Date {
    with(Calendar.getInstance()) {
        time = this@addDay
        add(Calendar.DAY_OF_MONTH, days)
        return time
    }
}

fun Date.applyTime(hour: Int, minutes: Int): Date {
    with(Calendar.getInstance()) {
        time = this@applyTime
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minutes)
        return time
    }
}

fun Date.greaterThan(other: Date, minThresholdMinutes: Int = 1): Boolean {
    val otherWithAddition = Calendar.getInstance().apply {
        time = other
        add(Calendar.MINUTE, minThresholdMinutes)
    }.time

    return this.after(otherWithAddition)
}