package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.*

@Composable
fun DaysGrid(
    selectedDay: Int,
    currentMonth: Int,
    isDarkMode: Boolean = false,
    accentColor: Color = Color(0xFF4CFF9E),
    textColor: Color = Color.White
) {
    // Fixed Calendar'da her ay 28 gün ve Pazartesi ile başlar
    // Önceki ayın son günlerini ve sonraki ayın ilk günlerini göster
    
    // Her ay 28 gün ve Pazartesi ile başladığı için
    // İlk satırda önceki ayın günlerini göstermemize gerek yok
    // Son satırda sonraki ayın ilk günlerini gösterelim
    
    val days = (1..28).toList()
    
    Column(modifier = GlanceModifier.fillMaxWidth()) {
        // 4 hafta (28 gün = 4 hafta x 7 gün)
        days.chunked(7).forEachIndexed { weekIndex, week ->
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                week.forEach { day ->
                    DayCell(
                        day = day,
                        selected = day == selectedDay,
                        isCurrentMonth = true,
                        accentColor = accentColor,
                        textColor = textColor
                    )
                }
            }
            
            // Satırlar arası boşluk
            if (weekIndex < 3) {
                Spacer(GlanceModifier.height(2.dp))
            }
        }
        
        // Son satır - sonraki ayın ilk günleri (5. hafta)
        Spacer(GlanceModifier.height(2.dp))
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sonraki ayın 1 ve 2'si soluk renkte
            listOf(1, 2).forEach { day ->
                DayCell(
                    day = day,
                    selected = false,
                    isCurrentMonth = false,
                    accentColor = accentColor,
                    textColor = textColor
                )
            }
            // Geri kalan 5 boş hücre
            repeat(5) {
                Spacer(modifier = GlanceModifier.size(24.dp))
            }
        }
    }
}
