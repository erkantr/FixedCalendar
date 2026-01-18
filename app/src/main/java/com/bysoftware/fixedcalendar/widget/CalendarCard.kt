package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.glance.background
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.unit.ColorProvider
import androidx.glance.ImageProvider
import com.bysoftware.fixedcalendar.R

@Composable
fun CalendarCard(
    primaryColor: Color = Color(0xFF1E88E5),
    isDarkMode: Boolean = false,
    currentMonth: Int = 1,
    currentDay: Int = 1,
    monthName: String = "January",
    year: Int = 2026,
    isLeapDay: Boolean = false,
    isYearDay: Boolean = false
) {
    val textColor = Color.White
    val accentColor = Color(0xFF4CFF9E)
    val headerBgColor = primaryColor.copy(alpha = 0.1f)
    val onSurfaceVariant = if (isDarkMode) Color(0xFF9E9E9E) else Color(0xFF666666)
    
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(ImageProvider(R.drawable.widget_card_modern))
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Başlık kartı - CalendarWidgerC.kt tasarımı
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(ImageProvider(R.drawable.widget_header_rounded))
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol ok
                Text(
                    "‹",
                    style = TextStyle(
                        color = ColorProvider(primaryColor.copy(alpha = 0.6f)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                
                Spacer(GlanceModifier.width(12.dp))
                
                // Ay ve Yıl
                Text(
                    "$monthName, $year",
                    style = TextStyle(
                        color = ColorProvider(primaryColor),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Spacer(GlanceModifier.width(12.dp))
                
                // Sağ ok
                Text(
                    "›",
                    style = TextStyle(
                        color = ColorProvider(primaryColor.copy(alpha = 0.6f)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        Spacer(GlanceModifier.height(6.dp))
        
        if (!isLeapDay && !isYearDay) {
            // Haftanın günleri
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU").forEach { day ->
                    Box(
                        modifier = GlanceModifier.size(21.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            style = TextStyle(
                                color = ColorProvider(onSurfaceVariant),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
            
            Spacer(GlanceModifier.height(4.dp))
            
            // Takvim grid'i - 28 gün
            Column(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                (1..28).chunked(7).forEachIndexed { weekIndex, week ->
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        week.forEach { day ->
                            DayCellModern(
                                day = day,
                                selected = day == currentDay,
                                primaryColor = primaryColor,
                                isDarkMode = isDarkMode
                            )
                        }
                    }
                    if (weekIndex < 3) {
                        Spacer(GlanceModifier.height(6.dp))
                    }
                }
            }
        } else {
            // Özel günler için özel görünüm
            Box(
                modifier = GlanceModifier.fillMaxWidth().padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isLeapDay) "🎉 Leap Day 🎉" else "🎊 Year Day 🎊",
                    style = TextStyle(
                        color = ColorProvider(primaryColor),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
