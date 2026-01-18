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

@Composable
fun DayCellModern(
    day: Int,
    selected: Boolean,
    primaryColor: Color = Color(0xFF1E88E5),
    isDarkMode: Boolean = false
) {
    // CalendarScreen.kt DayChip ile uyumlu renk şeması
    val textColor = when {
        selected -> Color.White  // Seçili günde beyaz
        else -> primaryColor     // Normal günlerde primary renk
    }
    
    // Tam daire için wrapper Box kullan
    Box(
        modifier = GlanceModifier
            .size(21.dp)
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = if (selected) {
                // Seçili gün için tam yuvarlak daire
                GlanceModifier
                    .size(19.dp)
                    .background(ImageProvider(R.drawable.widget_day_selected_primary))
            } else {
                GlanceModifier.size(19.dp)
            },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                style = TextStyle(
                    color = ColorProvider(textColor),
                    fontSize = 10.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}
