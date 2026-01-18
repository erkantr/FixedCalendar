package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.background
import androidx.glance.unit.ColorProvider
import androidx.glance.ImageProvider
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.utils.IFCDateUtils

@Composable
fun InfoCard(
    isDarkMode: Boolean = false,
    currentDay: Int = 1,
    monthName: String = "January",
    year: Int = 2026,
    isLeapDay: Boolean = false,
    isYearDay: Boolean = false,
    accentColor: Color = Color(0xFF4CFF9E)
) {
    val titleColor = accentColor
    val textColor = Color(0xFF8E8E93) // Soluk gri metin
    
    val dateText = when {
        isLeapDay -> "🎉 ${IFCDateUtils.getSpecialDayName(isLeapDay, false)}, $year"
        isYearDay -> "🎊 ${IFCDateUtils.getSpecialDayName(false, isYearDay)}, $year"
        else -> "📅 $currentDay $monthName, $year"
    }
    
    val messageText = when {
        isLeapDay -> "A special day that occurs once every 4 years! Enjoy this unique moment in Fixed Calendar."
        isYearDay -> "The final day of the year! A special celebration day before the new year begins."
        else -> "Fixed Calendar brings perfect harmony with 28-day months and aligned weeks."
    }
    
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(ImageProvider(R.drawable.widget_info_background))
            .padding(16.dp)
    ) {
        Text(
            dateText,
            style = TextStyle(
                color = ColorProvider(titleColor),
                fontSize = 14.sp,
                fontWeight = androidx.glance.text.FontWeight.Medium
            )
        )

        Spacer(GlanceModifier.height(6.dp))

        Text(
            messageText,
            style = TextStyle(
                color = ColorProvider(textColor),
                fontSize = 13.sp,
                fontWeight = androidx.glance.text.FontWeight.Normal
            )
        )
    }
}
