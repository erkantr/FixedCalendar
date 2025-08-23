package com.bysoftware.fixedcalendar.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.bysoftware.fixedcalendar.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class Month(
    val month: Int,
    val monthName: String,
    val days: List<Int>
)

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)

class CalendarViewModel @Inject constructor() : ViewModel() {

    private val currentDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())

    val gregorianDate: String
        get() = currentDate.format(dateFormatter)

    val ifcDateText: String
        get() = convertToIFC(currentDate)

    val currentYear: Int
        get() = currentDate.year

    val ifcYear: Int
        get() = currentDate.year // IFC yılı Gregorian ile aynı

    val months: List<Month>
        get() = (1..14).map { month ->
            Month(
                month = month,
                monthName = getMonthName(month),
                days = getDaysForMonth(month)
            )
        }

    val currentMonth: Int
        get() = calculateCurrentMonth()

    val currentDay: Int
        get() = calculateCurrentDay()

    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "Sol"
            8 -> "July"
            9 -> "August"
            10 -> "September"
            11 -> "October"
            12 -> "November"
            13 -> "December"
            14 -> "Year Day"
            else -> ""
        }
    }

    fun isSpecialDay(month: Int): Boolean {
        return month == 7 || month == 14 // Sol ve Year Day için
    }

    @Composable
    fun getSpecialDayName(month: Int): String {
        return when (month) {
            7 -> stringResource(R.string.sol)
            14 -> stringResource(R.string.year_day)
            else -> ""
        }
    }

    fun isCurrentDay(month: Int, day: Int): Boolean {
        val dayOfYear = currentDate.dayOfYear
        val isLeapYear = currentDate.isLeapYear

        // Year Day kontrolü
        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return month == 14 && day == 1
        }

        if (month != currentMonth) return false
        return day == currentDay
    }

    fun isCurrentDay(day: Int): Boolean {
        return day == currentDay
    }
    private fun convertToIFC(date: LocalDate): String {
        val dayOfYear = date.dayOfYear
        val isLeapYear = date.isLeapYear

        // Artık yıl kontrolü
        if (isLeapYear && dayOfYear == 366) {
            return "Year Day"
        }

        // Yıl sonu günü kontrolü
        if (!isLeapYear && dayOfYear == 365) {
            return "Year Day"
        }

        // Normal günler için hesaplama
        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        val month = ((adjustedDayOfYear - 1) / 28) + 1
        val day = ((adjustedDayOfYear - 1) % 28) + 1

        return "$day ${getMonthName(month)}"
    }

    private fun calculateCurrentMonth(): Int {
        val dayOfYear = currentDate.dayOfYear
        val isLeapYear = currentDate.isLeapYear

        // Year Day kontrolü
        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return 14
        }

        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        return ((adjustedDayOfYear - 1) / 28) + 1
    }

    private fun calculateCurrentDay(): Int {
        val dayOfYear = currentDate.dayOfYear
        val isLeapYear = currentDate.isLeapYear

        // Year Day kontrolü
        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return 1
        }

        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        return ((adjustedDayOfYear - 1) % 28) + 1
    }

    fun getDaysForMonth(month: Int): List<Int> {
        return when (month) {
            14 -> listOf(1) // Year Day için tek gün
            else -> (1..28).toList() // Diğer aylar için 28 gün
        }
    }
    @Composable
    fun getMonthDescription(month: Int): String {
        return when (month) {
            7 -> stringResource(R.string.sol_description)
            14 -> stringResource(R.string.year_day_description)
            13 -> "" // Aralık ayı için açıklama yok
            else -> "${getMonthName(month)} " + stringResource(R.string.month_default_description)
        }
    }
}