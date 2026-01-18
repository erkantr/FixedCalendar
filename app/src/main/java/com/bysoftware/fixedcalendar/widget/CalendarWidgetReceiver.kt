package com.bysoftware.fixedcalendar.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CalendarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = CalendarWidget()
    
    private val coroutineScope = MainScope()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        
        // Widget'ı güncelle
        coroutineScope.launch {
            CalendarWidget().updateAll(context)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        // Özel intent'ler için güncelleme
        when (intent.action) {
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            ACTION_UPDATE_WIDGET -> {
                coroutineScope.launch {
                    CalendarWidget().updateAll(context)
                }
            }
        }
    }

    companion object {
        const val ACTION_UPDATE_WIDGET = "com.bysoftware.fixedcalendar.UPDATE_WIDGET"
    }
}
