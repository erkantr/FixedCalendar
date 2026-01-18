package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.layout.*
import androidx.compose.ui.unit.dp
import androidx.glance.background

@Composable
fun CalendarWidgetContent(
    isDarkMode: Boolean = false,
    useCustomTheme: Boolean = false,
    customPrimaryColor: Color = Color(0xFFD32F2F),
    currentMonth: Int = 1,
    currentDay: Int = 1,
    monthName: String = "January",
    year: Int = 2026,
    isLeapDay: Boolean = false,
    isYearDay: Boolean = false
) {
    // Sadece takvim kartı - siyah arka plan yok
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(0.dp),
        contentAlignment = Alignment.Center
    ) {
        CalendarCard(
            primaryColor = if (useCustomTheme) customPrimaryColor else Color(0xFF1E88E5),
            isDarkMode = true,
            currentMonth = currentMonth,
            currentDay = currentDay,
            monthName = monthName,
            year = year,
            isLeapDay = isLeapDay,
            isYearDay = isYearDay
        )
    }
}
