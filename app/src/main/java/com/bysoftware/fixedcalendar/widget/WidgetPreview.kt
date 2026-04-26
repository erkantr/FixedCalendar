package com.bysoftware.fixedcalendar.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Widget Preview - Compose ile simülasyon
 * Bu dosyayı açın ve sağ üstteki "Split" veya "Design" butonuna tıklayın
 * Preview'ı görmek için Android Studio'nun Preview panelini açın
 */

@Preview(
    name = "Widget - Small (140x140)",
    widthDp = 140,
    heightDp = 140,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun WidgetPreviewSmall() {
    WidgetPreviewModern(
        currentDay = 18,
        monthName = "January",
        year = 2026
    )
}

@Preview(
    name = "Widget - Medium (220x180)",
    widthDp = 220,
    heightDp = 180,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun WidgetPreviewMedium() {
    WidgetPreviewModern(
        currentDay = 7,
        monthName = "February",
        year = 2026,
        primaryColor = Color(0xFFD32F2F)
    )
}

@Preview(
    name = "Widget - Large (250x250)",
    widthDp = 250,
    heightDp = 250,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun WidgetPreviewLarge() {
    WidgetPreviewModern(
        currentDay = 28,
        monthName = "December",
        year = 2026,
        primaryColor = Color(0xFF4CAF50)
    )
}

@Composable
fun WidgetPreviewModern(
    currentDay: Int = 18,
    monthName: String = "January",
    year: Int = 2026,
    primaryColor: Color = Color(0xFF1E88E5)
) {
    // Minimal boşluklu widget simülasyonu - CalendarScreen.kt renk şeması
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Başlık - basit, ortada
                Text(
                    text = "$monthName, $year",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                
                // Takvim Grid - 28 gün (CalendarScreen.kt gibi)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    (1..28).chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            week.forEach { day ->
                                PreviewDayCellModern(
                                    day = day,
                                    selected = day == currentDay,
                                    primaryColor = primaryColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PreviewDayCellModern(
    day: Int,
    selected: Boolean,
    primaryColor: Color = Color(0xFF1E88E5)
) {
    // CalendarScreen.kt DayChip ile birebir aynı
    val textColor = if (selected) Color.White else primaryColor
    val bgColor = if (selected) primaryColor else Color.Transparent
    
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}

