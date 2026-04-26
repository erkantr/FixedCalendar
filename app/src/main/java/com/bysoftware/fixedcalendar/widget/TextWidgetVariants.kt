package com.bysoftware.fixedcalendar.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

/**
 * Widget metin varyantları.
 * Şeffaf arkaplan + uygulama theme rengi.
 */

private fun textColors(isDarkMode: Boolean): Pair<Color, Color> {
    val onSurface = if (isDarkMode) Color(0xFFE6E6E6) else Color(0xFF1A1A1A)
    val onSurfaceMuted = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF555555)
    return onSurface to onSurfaceMuted
}

@Composable
fun TextHeroCompactWidget(
    primaryColor: Color,
    isDarkMode: Boolean,
    ifcText: String,
    gregorianText: String,
    size: DpSize
) {
    val (onSurface, onSurfaceMuted) = textColors(isDarkMode)
    val ifcFont: TextUnit
    val labelFont: TextUnit
    val gregFont: TextUnit
    when {
        size.width < 180.dp -> {
            ifcFont = 24.sp; labelFont = 10.sp; gregFont = 14.sp
        }
        size.width < 260.dp -> {
            ifcFont = 30.sp; labelFont = 11.sp; gregFont = 16.sp
        }
        else -> {
            ifcFont = 38.sp; labelFont = 12.sp; gregFont = 18.sp
        }
    }

    Box(
        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = "INTERNATIONAL FIXED",
                    style = TextStyle(
                        color = ColorProvider(primaryColor),
                        fontSize = labelFont,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = ifcText,
                    style = TextStyle(
                        color = ColorProvider(onSurface),
                        fontSize = ifcFont,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(GlanceModifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "GREGORIAN",
                    style = TextStyle(
                        color = ColorProvider(onSurfaceMuted),
                        fontSize = labelFont,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = gregorianText,
                    style = TextStyle(
                        color = ColorProvider(onSurface),
                        fontSize = gregFont,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun TextStackedMinimalWidget(
    primaryColor: Color,
    isDarkMode: Boolean,
    ifcText: String,
    gregorianText: String,
    size: DpSize
) {
    val (onSurface, onSurfaceMuted) = textColors(isDarkMode)
    val ifcFont: TextUnit
    val labelFont: TextUnit
    val gregFont: TextUnit
    when {
        size.width < 180.dp -> {
            ifcFont = 32.sp; labelFont = 10.sp; gregFont = 14.sp
        }
        size.width < 260.dp -> {
            ifcFont = 42.sp; labelFont = 11.sp; gregFont = 16.sp
        }
        else -> {
            ifcFont = 54.sp; labelFont = 12.sp; gregFont = 18.sp
        }
    }

    Box(
        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "INTERNATIONAL FIXED",
                style = TextStyle(
                    color = ColorProvider(primaryColor),
                    fontSize = labelFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(GlanceModifier.height(2.dp))
            Text(
                text = ifcText,
                style = TextStyle(
                    color = ColorProvider(onSurface),
                    fontSize = ifcFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(GlanceModifier.height(4.dp))
            Text(
                text = "Gregorian: $gregorianText",
                style = TextStyle(
                    color = ColorProvider(onSurfaceMuted),
                    fontSize = gregFont,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
fun TextPillChipWidget(
    primaryColor: Color,
    isDarkMode: Boolean,
    ifcText: String,
    gregorianText: String,
    size: DpSize
) {
    val (onSurface, onSurfaceMuted) = textColors(isDarkMode)
    val labelFont: TextUnit
    val valueFont: TextUnit
    when {
        size.width < 180.dp -> {
            labelFont = 10.sp; valueFont = 16.sp
        }
        size.width < 260.dp -> {
            labelFont = 11.sp; valueFont = 20.sp
        }
        else -> {
            labelFont = 12.sp; valueFont = 24.sp
        }
    }

    val primaryBg = Color(
        red = primaryColor.red,
        green = primaryColor.green,
        blue = primaryColor.blue,
        alpha = 0.12f
    )
    val mutedBg = if (isDarkMode) {
        Color(0x33FFFFFF)
    } else {
        Color(0x141A1A1A)
    }

    Box(
        modifier = GlanceModifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pill 1: IFC
            Box(
                modifier = GlanceModifier
                    .cornerRadius(20.dp)
                    .background(primaryBg)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "IFC",
                        style = TextStyle(
                            color = ColorProvider(primaryColor),
                            fontSize = labelFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(GlanceModifier.width(6.dp))
                    Text(
                        text = ifcText,
                        style = TextStyle(
                            color = ColorProvider(onSurface),
                            fontSize = valueFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(GlanceModifier.height(6.dp))
            // Pill 2: Gregorian
            Box(
                modifier = GlanceModifier
                    .cornerRadius(20.dp)
                    .background(mutedBg)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "GREG",
                        style = TextStyle(
                            color = ColorProvider(onSurfaceMuted),
                            fontSize = labelFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(GlanceModifier.width(6.dp))
                    Text(
                        text = gregorianText,
                        style = TextStyle(
                            color = ColorProvider(onSurface),
                            fontSize = valueFont,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
