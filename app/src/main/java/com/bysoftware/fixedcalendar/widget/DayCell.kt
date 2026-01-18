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
fun DayCell(
    day: Int,
    selected: Boolean,
    isCurrentMonth: Boolean = true,
    accentColor: Color = Color(0xFF4CFF9E),
    textColor: Color = Color.White
) {
    // Metin rengi
    val dayTextColor = when {
        selected -> Color(0xFF000000) // Seçili gün için siyah metin
        !isCurrentMonth -> textColor.copy(alpha = 0.3f) // Diğer ayların günleri soluk
        else -> textColor // Normal günler beyaz
    }
    
    val modifier = if (selected) {
        // Seçili gün için daire arka plan
        GlanceModifier
            .size(24.dp)
            .padding(1.dp)
            .background(ImageProvider(R.drawable.widget_day_selected))
    } else {
        // Normal günler için şeffaf arka plan
        GlanceModifier
            .size(24.dp)
            .padding(1.dp)
    }
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = TextStyle(
                color = ColorProvider(dayTextColor),
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
