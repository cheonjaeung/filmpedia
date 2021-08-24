package io.woong.filmpedia.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object DateUtil {

    private const val formatString: String = "yyyy-MM-dd"

    private val today: String
        get() {
            val dateObj = Date()
            val formatter = SimpleDateFormat(formatString, Locale.getDefault())
            return formatter.format(dateObj)
        }

    fun getAge(birthday: String, deathday: String?, default: Int = -1): Int {
        return if (isValidFormat(birthday)) {
            if (deathday != null) {
                if (isValidFormat(deathday)) {
                    calculateAge(birthday, deathday)
                } else {
                    default
                }
            } else {
                calculateAge(birthday, today)
            }
        } else {
            default
        }
    }

    private fun isValidFormat(date: String): Boolean {
        return try {
            val formatter = SimpleDateFormat(formatString, Locale.getDefault())
            formatter.isLenient = false
            formatter.parse(date)
            true
        } catch (e: ParseException) {
            false
        }
    }

    private fun toCalendar(dateString: String): Calendar {
        val formatter = SimpleDateFormat(formatString, Locale.getDefault())
        return Calendar.getInstance().apply {
            time = formatter.parse(dateString)!!
        }
    }

    private fun calculateAge(birthday: String, lastday: String): Int {
        val start = toCalendar(birthday)
        val end = toCalendar(lastday)

        val startYear = start.get(Calendar.YEAR)
        val endYear = end.get(Calendar.YEAR)
        var age = abs(endYear - startYear)

        start.set(
            start.get(Calendar.YEAR) + age,
            start.get(Calendar.MONTH),
            start.get(Calendar.DAY_OF_MONTH)
        )

        if (start.after(end)) {
            age--
        }

        return age
    }
}
