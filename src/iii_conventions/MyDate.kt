package iii_conventions

import java.sql.Time

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate) = when {
        year != other.year -> year - other.year
        month != other.month -> month - other.month
        else -> dayOfMonth - other.dayOfMonth
    }
}

operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

enum class TimeInterval {
    DAY,
    WEEK,
    YEAR
}

operator fun MyDate.plus(interval: TimeInterval): MyDate = addTimeIntervals(interval, 1)

class RepeatedTimeInterval(val interval: TimeInterval, val count: Int)

operator fun TimeInterval.times(count: Int) = RepeatedTimeInterval(this, count)

operator fun MyDate.plus(interval: RepeatedTimeInterval): MyDate = addTimeIntervals(interval.interval, interval.count)

class DateIterator(val range: DateRange) : Iterator<MyDate> {
    var current = range.start
    override fun next(): MyDate {
        val next = current
        current = current.nextDay()
        return next
    }

    override fun hasNext(): Boolean = current <= range.endInclusive

}

class DateRange(override val start: MyDate, override val endInclusive: MyDate)
    : ClosedRange<MyDate>, Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate> = DateIterator(this)
}
