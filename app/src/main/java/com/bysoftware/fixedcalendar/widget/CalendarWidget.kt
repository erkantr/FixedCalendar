package com.bysoftware.fixedcalendar.widget

import androidx.glance.appwidget.GlanceAppWidget
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.bysoftware.fixedcalendar.utils.IFCDateUtils
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class CalendarWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        // Tema verilerini oku
        val dataStore = com.bysoftware.fixedcalendar.data.ThemeDataStore(context)
        val isDarkMode = dataStore.isDarkMode.first()
        val useCustomTheme = dataStore.useCustomTheme.first()
        val customPrimaryColor = dataStore.customPrimaryColor.first()
        
        // Bugünün tarihini Fixed Calendar formatına çevir
        val today = LocalDate.now()
        val ifcDate = IFCDateUtils.convertToIFC(today)
        val monthName = IFCDateUtils.getMonthName(ifcDate.month)
        val year = today.year
        
        provideContent {
            CalendarWidgetContent(
                isDarkMode = isDarkMode,
                useCustomTheme = useCustomTheme,
                customPrimaryColor = customPrimaryColor,
                currentMonth = ifcDate.month,
                currentDay = ifcDate.day,
                monthName = monthName,
                year = year,
                isLeapDay = ifcDate.isLeapDay,
                isYearDay = ifcDate.isYearDay
            )
        }
    }
}

