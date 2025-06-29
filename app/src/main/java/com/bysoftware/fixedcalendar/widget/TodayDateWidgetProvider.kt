package com.bysoftware.fixedcalendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.bysoftware.fixedcalendar.MainActivity
import com.bysoftware.fixedcalendar.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TodayDateWidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val currentDate = LocalDate.now()
        
        // Gregorian date formatting
        val gregorianFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        val gregorianDate = currentDate.format(gregorianFormatter)
        
        // IFC date calculation
        val ifcDate = convertToIFC(currentDate)
        
        // Create RemoteViews
        val views = RemoteViews(context.packageName, R.layout.widget_today_date)
        
        // Update widget content - DateCard format
        views.setTextViewText(R.id.widget_year, currentDate.year.toString())
        views.setTextViewText(R.id.widget_gregorian_date, gregorianDate)
        views.setTextViewText(R.id.widget_ifc_date, ifcDate)
        
        // Create intent to open app when widget is clicked
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        views.setOnClickPendingIntent(R.id.widget_today_date, pendingIntent)
        
        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToIFC(date: LocalDate): String {
        val dayOfYear = date.dayOfYear
        val isLeapYear = date.isLeapYear

        // Year Day kontrolü
        if ((dayOfYear == 365 && !isLeapYear) || (dayOfYear == 366 && isLeapYear)) {
            return "Year Day"
        }

        val adjustedDayOfYear = if (dayOfYear > 365) dayOfYear - 1 else dayOfYear
        val month = ((adjustedDayOfYear - 1) / 28) + 1
        val day = ((adjustedDayOfYear - 1) % 28) + 1

        val monthName = getMonthName(month)
        return "$day $monthName"
    }

    private fun getMonthName(month: Int): String {
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
            else -> ""
        }
    }
} 