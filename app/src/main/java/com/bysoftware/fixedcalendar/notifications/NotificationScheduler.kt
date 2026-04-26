package com.bysoftware.fixedcalendar.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bysoftware.fixedcalendar.MainActivity
import com.bysoftware.fixedcalendar.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object NotificationScheduler {

    const val CHANNEL_ID = "fixed_calendar_special_days"
    private const val REQUEST_CODE_YEAR_DAY = 1001
    private const val REQUEST_CODE_LEAP_DAY = 1002

    const val ACTION_NOTIFY = "com.bysoftware.fixedcalendar.ACTION_NOTIFY"
    const val EXTRA_KIND = "extra_kind"
    const val KIND_YEAR_DAY = "year_day"
    const val KIND_LEAP_DAY = "leap_day"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.notif_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = context.getString(R.string.notif_channel_desc)
                }
                nm.createNotificationChannel(channel)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleAll(context: Context) {
        ensureChannel(context)
        val today = LocalDate.now()
        val notifyTime = LocalTime.of(9, 0)

        // Year Day = yılın 365. (artık yılda 366.) günü
        val yearDayCandidate = if (today.isLeapYear) {
            LocalDate.ofYearDay(today.year, 366)
        } else {
            LocalDate.ofYearDay(today.year, 365)
        }
        val yearDayDate = if (yearDayCandidate.isBefore(today)) {
            val nextYear = today.year + 1
            val nextYearLeap = LocalDate.of(nextYear, 1, 1).isLeapYear
            if (nextYearLeap) LocalDate.ofYearDay(nextYear, 366)
            else LocalDate.ofYearDay(nextYear, 365)
        } else yearDayCandidate

        scheduleAt(
            context,
            LocalDateTime.of(yearDayDate, notifyTime),
            REQUEST_CODE_YEAR_DAY,
            KIND_YEAR_DAY
        )

        // Leap Day - sadece artık yıllarda yıl ortasında (169. gün)
        val leapDayDate: LocalDate? = when {
            today.isLeapYear -> {
                val candidate = LocalDate.ofYearDay(today.year, 169)
                if (candidate.isBefore(today)) {
                    findNextLeapYear(today.year + 1)?.let { LocalDate.ofYearDay(it, 169) }
                } else candidate
            }
            else -> findNextLeapYear(today.year + 1)?.let { LocalDate.ofYearDay(it, 169) }
        }
        leapDayDate?.let {
            scheduleAt(
                context,
                LocalDateTime.of(it, notifyTime),
                REQUEST_CODE_LEAP_DAY,
                KIND_LEAP_DAY
            )
        }
    }

    fun cancelAll(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(buildPendingIntent(context, REQUEST_CODE_YEAR_DAY, KIND_YEAR_DAY))
        am.cancel(buildPendingIntent(context, REQUEST_CODE_LEAP_DAY, KIND_LEAP_DAY))
    }

    private fun findNextLeapYear(startYear: Int): Int? {
        for (y in startYear..(startYear + 4)) {
            if (LocalDate.of(y, 1, 1).isLeapYear) return y
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleAt(
        context: Context,
        dateTime: LocalDateTime,
        requestCode: Int,
        kind: String
    ) {
        val triggerAt = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerAt <= System.currentTimeMillis()) return
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = buildPendingIntent(context, requestCode, kind)
        try {
            am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        } catch (_: SecurityException) {
            // exact alarm permission yok - basit set zaten exact değil
        }
    }

    private fun buildPendingIntent(context: Context, requestCode: Int, kind: String): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_NOTIFY
            putExtra(EXTRA_KIND, kind)
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getBroadcast(context, requestCode, intent, flags)
    }

    fun showNotification(context: Context, kind: String) {
        ensureChannel(context)
        val (titleRes, textRes) = when (kind) {
            KIND_YEAR_DAY -> R.string.notif_year_day_title to R.string.notif_year_day_text
            KIND_LEAP_DAY -> R.string.notif_leap_day_title to R.string.notif_leap_day_text
            else -> return
        }
        val openIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val piFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val contentPi = PendingIntent.getActivity(context, 0, openIntent, piFlags)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.logo)
            .setContentTitle(context.getString(titleRes))
            .setContentText(context.getString(textRes))
            .setAutoCancel(true)
            .setContentIntent(contentPi)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(kind.hashCode(), notification)
    }
}
