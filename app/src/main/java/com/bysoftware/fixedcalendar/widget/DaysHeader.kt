package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import androidx.glance.text.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun DaysHeader(
    isDarkMode: Boolean = false,
    textColor: Color = Color.White
) {
    val headerColor = textColor.copy(alpha = 0.5f)
    
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU").forEach { day ->
            Box(
                modifier = GlanceModifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    style = TextStyle(
                        color = ColorProvider(headerColor),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}
