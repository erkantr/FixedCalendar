package com.bysoftware.fixedcalendar.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val kind = intent.getStringExtra(NotificationScheduler.EXTRA_KIND) ?: return
        NotificationScheduler.showNotification(context, kind)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rescheduleNext(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun rescheduleNext(context: Context) {
        NotificationScheduler.scheduleAll(context)
    }
}
