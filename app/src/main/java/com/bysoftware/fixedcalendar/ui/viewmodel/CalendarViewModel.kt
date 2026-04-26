package com.bysoftware.fixedcalendar.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.bysoftware.fixedcalendar.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val today: LocalDate = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())

    // Görüntülenen yıl - kullanıcı ay/yıl ileri-geri gidebilir
    var viewedYear by mutableStateOf(today.year)
        private set

    val gregorianDate: String
        get() = today.format(dateFormatter)

    /** UI'da gösterilen IFC tarih metni (composable, lokalize). */
    val ifcDateText: String
        @Composable
        get() = convertToIFC(today)

    /** Lokalize edilmemiş ham IFC tarih metni (paylaşım vb. için). */
    val ifcDateTextRaw: String
        get() = rawConvertToIFC(today)

    val months: List<Int>
        get() = (1..14).toList()

    val currentMonth: Int
        get() = calculateCurrentMonth()

    val currentDay: Int
        get() = calculateCurrentDay()

    val isViewingToday: Boolean
        get() = viewedYear == today.year

    val viewedMonthLabel: String
        get() = "$viewedYear"

    fun nextMonth() {
        viewedYear += 1
    }

    fun prevMonth() {
        viewedYear -= 1
    }

    fun goToToday() {
        viewedYear = today.year
    }

    @Composable
    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> stringResource(R.string.january)
            2 -> stringResource(R.string.february)
            3 -> stringResource(R.string.march)
            4 -> stringResource(R.string.april)
            5 -> stringResource(R.string.may)
            6 -> stringResource(R.string.june)
            7 -> stringResource(R.string.sol)
            8 -> stringResource(R.string.july)
            9 -> stringResource(R.string.august)
            10 -> stringResource(R.string.september)
            11 -> stringResource(R.string.october)
            12 -> stringResource(R.string.november)
            13 -> stringResource(R.string.december)
            14 -> stringResource(R.string.year_day)
            else -> ""
        }
    }

    fun isSpecialDay(month: Int): Boolean {
        return month == 7 || month == 14
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
        if (!isViewingToday) return false
        val dayOfYear = today.dayOfYear
        val isLeapYear = today.isLeapYear

        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return month == 14 && day == 1
        }
        if (month != currentMonth) return false
        return day == currentDay
    }

    @Composable
    private fun convertToIFC(date: LocalDate): String {
        val dayOfYear = date.dayOfYear
        val isLeapYear = date.isLeapYear

        if (isLeapYear && dayOfYear == 366) {
            return stringResource(R.string.year_day)
        }
        if (!isLeapYear && dayOfYear == 365) {
            return stringResource(R.string.year_day)
        }
        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        val month = ((adjustedDayOfYear - 1) / 28) + 1
        val day = ((adjustedDayOfYear - 1) % 28) + 1
        return "$day ${getMonthName(month)}"
    }

    private fun rawConvertToIFC(date: LocalDate): String {
        val dayOfYear = date.dayOfYear
        val isLeapYear = date.isLeapYear

        if (isLeapYear && dayOfYear == 366) return "Year Day"
        if (!isLeapYear && dayOfYear == 365) return "Year Day"
        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        val month = ((adjustedDayOfYear - 1) / 28) + 1
        val day = ((adjustedDayOfYear - 1) % 28) + 1
        val monthName = when (month) {
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
            else -> ""
        }
        return "$day $monthName"
    }

    private fun calculateCurrentMonth(): Int {
        val dayOfYear = today.dayOfYear
        val isLeapYear = today.isLeapYear

        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return 14
        }
        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        return ((adjustedDayOfYear - 1) / 28) + 1
    }

    private fun calculateCurrentDay(): Int {
        val dayOfYear = today.dayOfYear
        val isLeapYear = today.isLeapYear

        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return 1
        }
        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        return ((adjustedDayOfYear - 1) % 28) + 1
    }

    fun getDaysForMonth(month: Int): List<Int> {
        return when (month) {
            14 -> listOf(1)
            else -> (1..28).toList()
        }
    }

    @Composable
    fun getMonthDescription(month: Int): String {
        return when (month) {
            7 -> stringResource(R.string.sol_description)
            14 -> stringResource(R.string.year_day_description)
            13 -> ""
            else -> "${getMonthName(month)} " + stringResource(R.string.month_default_description)
        }
    }
}
