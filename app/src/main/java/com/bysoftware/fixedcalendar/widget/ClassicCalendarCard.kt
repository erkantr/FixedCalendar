package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.RowScope
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.bysoftware.fixedcalendar.R

/**
 * Klasik takvim widget'ı.
 * Yuvarlak köşeli kart + tema rengiyle dinamik seçili-gün dairesi.
 * Hücreler kare; weight ile genişliğe yayılır.
 */
@Composable
fun ClassicCalendarCard(
    primaryColor: Color = Color(0xFF1E88E5),
    isDarkMode: Boolean = false,
    @Suppress("UNUSED_PARAMETER") currentMonth: Int = 1,
    currentDay: Int = 1,
    monthName: String = "January",
    year: Int = 2026,
    isLeapDay: Boolean = false,
    isYearDay: Boolean = false,
    widgetSize: WidgetSize = WidgetSize.MEDIUM
) {
    val onSurface = if (isDarkMode) Color(0xFFE6E6E6) else Color(0xFF1A1A1A)
    val onSurfaceMuted = if (isDarkMode) Color(0xFF9E9E9E) else Color(0xFF666666)

    val cellHeight: Dp
    val daySize: Dp
    val dayFont: TextUnit
    val headerFont: TextUnit
    val weekFont: TextUnit
    val cardPadding: Dp
    val rowSpacing: Dp

    when (widgetSize) {
        WidgetSize.SMALL -> {
            cellHeight = 22.dp; daySize = 20.dp; dayFont = 9.sp; headerFont = 11.sp
            weekFont = 7.sp; cardPadding = 4.dp; rowSpacing = 2.dp
        }
        WidgetSize.MEDIUM -> {
            cellHeight = 28.dp; daySize = 26.dp; dayFont = 12.sp; headerFont = 14.sp
            weekFont = 9.sp; cardPadding = 6.dp; rowSpacing = 3.dp
        }
        WidgetSize.LARGE -> {
            cellHeight = 36.dp; daySize = 32.dp; dayFont = 14.sp; headerFont = 16.sp
            weekFont = 10.sp; cardPadding = 8.dp; rowSpacing = 4.dp
        }
    }

    val backgroundDrawable = if (isDarkMode) {
        R.drawable.widget_card_dark
    } else {
        R.drawable.widget_card_light
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(backgroundDrawable))
            .padding(cardPadding),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = GlanceModifier.fillMaxWidth().padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$monthName  ·  $year",
                style = TextStyle(
                    color = ColorProvider(primaryColor),
                    fontSize = headerFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(GlanceModifier.height(rowSpacing))

        if (!isLeapDay && !isYearDay) {
            if (widgetSize != WidgetSize.SMALL) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU").forEach { day ->
                        Box(
                            modifier = GlanceModifier
                                .defaultWeight()
                                .height(cellHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                style = TextStyle(
                                    color = ColorProvider(onSurfaceMuted),
                                    fontSize = weekFont,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
                Spacer(GlanceModifier.height(2.dp))
            }

            Column(modifier = GlanceModifier.fillMaxWidth()) {
                (1..28).chunked(7).forEachIndexed { weekIndex, week ->
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        week.forEach { day ->
                            ClassicDayCell(
                                day = day,
                                selected = day == currentDay,
                                primaryColor = primaryColor,
                                onSurface = onSurface,
                                cellHeight = cellHeight,
                                daySize = daySize,
                                fontSize = dayFont
                            )
                        }
                    }
                    if (weekIndex < 3) {
                        Spacer(GlanceModifier.height(if (widgetSize == WidgetSize.SMALL) 2.dp else 4.dp))
                    }
                }
            }
        } else {
            Box(
                modifier = GlanceModifier.fillMaxWidth().padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isLeapDay) "Leap Day" else "Year Day",
                    style = TextStyle(
                        color = ColorProvider(primaryColor),
                        fontSize = headerFont,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun RowScope.ClassicDayCell(
    day: Int,
    selected: Boolean,
    primaryColor: Color,
    onSurface: Color,
    cellHeight: Dp,
    daySize: Dp,
    fontSize: TextUnit
) {
    val textColor = if (selected) Color.White else onSurface

    // Outer hücre weight ile genişliğin eşit kısmını alır.
    Box(
        modifier = GlanceModifier
            .defaultWeight()
            .height(cellHeight),
        contentAlignment = Alignment.Center
    ) {
        // İç chip kare; cornerRadius ile yuvarlatılmış. Renk dinamik (theme primary).
        Box(
            modifier = if (selected) {
                GlanceModifier
                    .size(daySize)
                    .cornerRadius(daySize / 2)
                    .background(ColorProvider(primaryColor))
            } else {
                GlanceModifier.size(daySize)
            },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                style = TextStyle(
                    color = ColorProvider(textColor),
                    fontSize = fontSize,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
