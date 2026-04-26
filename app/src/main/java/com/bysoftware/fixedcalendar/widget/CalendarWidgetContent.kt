package com.bysoftware.fixedcalendar.widget

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.*
import com.bysoftware.fixedcalendar.MainActivity
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme

enum class WidgetSize { SMALL, MEDIUM, LARGE }

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
    isYearDay: Boolean = false,
    widgetStyle: WidgetStyle = WidgetStyle.GRID_CLASSIC,
    ifcDayText: String = "",
    gregorianText: String = ""
) {
    val size = LocalSize.current
    val context = LocalContext.current

    val bucket = when {
        size.width < 180.dp || size.height < 160.dp -> WidgetSize.SMALL
        size.width < 240.dp || size.height < 220.dp -> WidgetSize.MEDIUM
        else -> WidgetSize.LARGE
    }

    val openAppIntent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    val primaryColor = if (useCustomTheme) customPrimaryColor else Color(0xFF1E88E5)

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(0.dp)
            .clickable(actionStartActivity(openAppIntent)),
        contentAlignment = Alignment.Center
    ) {
        when (widgetStyle) {
            WidgetStyle.TEXT_HERO_COMPACT -> TextHeroCompactWidget(
                primaryColor = primaryColor,
                isDarkMode = isDarkMode,
                ifcText = ifcDayText,
                gregorianText = gregorianText,
                size = size
            )
            WidgetStyle.TEXT_STACKED_MINIMAL -> TextStackedMinimalWidget(
                primaryColor = primaryColor,
                isDarkMode = isDarkMode,
                ifcText = ifcDayText,
                gregorianText = gregorianText,
                size = size
            )
            WidgetStyle.TEXT_PILL_CHIP -> TextPillChipWidget(
                primaryColor = primaryColor,
                isDarkMode = isDarkMode,
                ifcText = ifcDayText,
                gregorianText = gregorianText,
                size = size
            )
            WidgetStyle.GRID_CLASSIC -> ClassicCalendarCard(
                primaryColor = primaryColor,
                isDarkMode = isDarkMode,
                currentMonth = currentMonth,
                currentDay = currentDay,
                monthName = monthName,
                year = year,
                isLeapDay = isLeapDay,
                isYearDay = isYearDay,
                widgetSize = bucket
            )
        }
    }
}

@Composable
@Preview
fun CalendarWidgetPreview2() {
    FixedCalendarTheme {
    CalendarWidgetContent(
        isDarkMode = false,
        useCustomTheme = false,
        customPrimaryColor = Color(0xFFD32F2F),
        currentMonth = 1,
        currentDay = 1,
        monthName = "January",
        year = 2026,
        isLeapDay = false,
        isYearDay = false,
        widgetStyle = WidgetStyle.GRID_CLASSIC,
        ifcDayText = "",
        gregorianText = ""
    )
}
}
