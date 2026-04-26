package com.bysoftware.fixedcalendar.widget

import android.content.Context
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.bysoftware.fixedcalendar.utils.IFCDateUtils
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class CalendarWidget : GlanceAppWidget() {

    companion object {
        val SMALL_SIZE = DpSize(140.dp, 140.dp)
        val MEDIUM_SIZE = DpSize(220.dp, 180.dp)
        val LARGE_SIZE = DpSize(250.dp, 250.dp)
    }

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(SMALL_SIZE, MEDIUM_SIZE, LARGE_SIZE)
    )

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val dataStore = com.bysoftware.fixedcalendar.data.ThemeDataStore(context)

        val widgetStyleKey = dataStore.widgetStyle.first()
        val widgetUseCustomTheme = dataStore.widgetUseCustomTheme.first()

        // Widget kendi temasını kullanıyorsa widget-specific değerleri, aksi halde
        // uygulama temasını kullan.
        val isDarkMode: Boolean
        val useCustomTheme: Boolean
        val useDynamicColor: Boolean
        val customPrimaryColor: androidx.compose.ui.graphics.Color
        if (widgetUseCustomTheme) {
            isDarkMode = dataStore.widgetIsDarkMode.first()
            useCustomTheme = dataStore.widgetUseCustomColor.first()
            useDynamicColor = dataStore.widgetUseDynamicColor.first()
            customPrimaryColor = dataStore.widgetCustomPrimaryColor.first()
        } else {
            isDarkMode = dataStore.isDarkMode.first()
            useCustomTheme = dataStore.useCustomTheme.first()
            useDynamicColor = dataStore.useDynamicColor.first()
            customPrimaryColor = dataStore.customPrimaryColor.first()
        }

        // Primary renk: dinamik renk (Android 12+) > özel tema > default mavi
        val resolvedPrimary: androidx.compose.ui.graphics.Color = when {
            useDynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
                val resId = if (isDarkMode) {
                    android.R.color.system_accent1_200
                } else {
                    android.R.color.system_accent1_600
                }
                androidx.compose.ui.graphics.Color(context.getColor(resId))
            }
            useCustomTheme -> customPrimaryColor
            else -> androidx.compose.ui.graphics.Color(0xFF1E88E5)
        }

        val today = LocalDate.now()
        val ifcDate = IFCDateUtils.convertToIFC(today)
        val monthName = IFCDateUtils.getMonthName(ifcDate.month)
        val year = today.year
        val ifcDayText = if (ifcDate.isLeapDay) "Leap Day"
            else if (ifcDate.isYearDay) "Year Day"
            else "${ifcDate.day} $monthName"
        val gregorianText = "${today.dayOfMonth} ${today.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${today.year}"

        provideContent {
            CalendarWidgetContent(
                isDarkMode = isDarkMode,
                useCustomTheme = true, // primary renk router tarafında çözüldü, doğrudan iletiyoruz
                customPrimaryColor = resolvedPrimary,
                currentMonth = ifcDate.month,
                currentDay = ifcDate.day,
                monthName = monthName,
                year = year,
                isLeapDay = ifcDate.isLeapDay,
                isYearDay = ifcDate.isYearDay,
                widgetStyle = WidgetStyle.fromKey(widgetStyleKey),
                ifcDayText = ifcDayText,
                gregorianText = gregorianText
            )
        }
    }
}
