package com.bysoftware.fixedcalendar.utils

import java.time.LocalDate

class IFCDateUtils {
    companion object {
        private const val DAYS_IN_MONTH = 28
        private const val MONTHS_IN_YEAR = 13
        private const val LEAP_DAY_MONTH = 6 // Sol ayı (gösterim için)
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
                dayOfYear == 365 || (isLeapYear && dayOfYear == 366) -> {
                    IFCDate(YEAR_DAY_MONTH, YEAR_DAY_DAY, isYearDay = true)
                }
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

        /**
         * Verilen IFC tarihini Gregorian'a dönüştürür.
         * @param month 1..13 (1..13 normal aylar). Year Day için month=13, day=29 kullanın.
         * @param day 1..28 (Year Day için 29; Leap Day için month=6, day=29 ve year artık yıl olmalı)
         * @param year Gregorian yıl
         * @return Geçerli LocalDate veya parametreler geçersizse null.
         */
        fun convertFromIFC(month: Int, day: Int, year: Int): LocalDate? {
            if (month < 1 || month > MONTHS_IN_YEAR) return null
            val isLeapYear = LocalDate.of(year, 1, 1).isLeapYear

            // Year Day - 13. ayın 29'u
            if (month == YEAR_DAY_MONTH && day == YEAR_DAY_DAY) {
                return if (isLeapYear) LocalDate.ofYearDay(year, 366)
                else LocalDate.ofYearDay(year, 365)
            }

            // Leap Day - sadece artık yıllarda Sol ayı (gösterimde 6, IFC'de 6'nın 29'u)
            if (month == LEAP_DAY_MONTH && day == LEAP_DAY_DAY) {
                if (!isLeapYear) return null
                return LocalDate.ofYearDay(year, 169)
            }

            if (day < 1 || day > DAYS_IN_MONTH) return null

            // Normal IFC tarih → Gregorian dayOfYear
            var dayOfYear = (month - 1) * DAYS_IN_MONTH + day
            if (isLeapYear && dayOfYear >= 169) dayOfYear++
            return LocalDate.ofYearDay(year, dayOfYear)
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
