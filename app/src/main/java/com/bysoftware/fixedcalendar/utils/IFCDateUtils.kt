package com.bysoftware.fixedcalendar.utils

import java.time.LocalDate

class IFCDateUtils {
    companion object {
        private const val DAYS_IN_MONTH = 28
        private const val MONTHS_IN_YEAR = 13
        private const val LEAP_DAY_MONTH = 6 // Sol ayı
        private const val LEAP_DAY_DAY = 29
        private const val YEAR_DAY_MONTH = 13
        private const val YEAR_DAY_DAY = 29

        data class IFCDate(
            val month: Int,
            val day: Int,
            val isLeapDay: Boolean = false,
            val isYearDay: Boolean = false
        )

        fun convertToIFC(date: LocalDate): IFCDate {
            val dayOfYear = date.dayOfYear
            val isLeapYear = date.isLeapYear

            return when {
                // Yıl sonu günü kontrolü
                dayOfYear == 365 || (isLeapYear && dayOfYear == 366) -> {
                    IFCDate(YEAR_DAY_MONTH, YEAR_DAY_DAY, isYearDay = true)
                }
                // Artık gün kontrolü
                isLeapYear && dayOfYear == 169 -> {
                    IFCDate(LEAP_DAY_MONTH, LEAP_DAY_DAY, isLeapDay = true)
                }
                else -> {
                    var adjustedDayOfYear = dayOfYear
                    if (isLeapYear && dayOfYear > 169) {
                        adjustedDayOfYear--
                    }
                    val month = ((adjustedDayOfYear - 1) / DAYS_IN_MONTH) + 1
                    val day = ((adjustedDayOfYear - 1) % DAYS_IN_MONTH) + 1
                    IFCDate(month, day)
                }
            }
        }

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
                else -> "Unknown"
            }
        }

        fun getSpecialDayName(isLeapDay: Boolean, isYearDay: Boolean): String {
            return when {
                isLeapDay -> "Leap Day"
                isYearDay -> "Year Day"
                else -> ""
            }
        }
    }
} 