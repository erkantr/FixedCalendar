package com.bysoftware.fixedcalendar.ui.screens

import android.content.res.Resources.Theme
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.viewmodel.CalendarViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onInfoClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = remember { screenWidth >= 600.dp }
    val gridColumns = remember { if (isTablet) 4 else 2 }
    val cardHeight = remember { if (isTablet) 200.dp else 160.dp }

    Scaffold(

        topBar = {
        }
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
        ) {
           // AdmobBanner(modifier = Modifier.fillMaxWidth())

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Fixed Calendar",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = stringResource(R.string.info_top)
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_top)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = if (isTablet) 32.dp else 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarih kartı
                Spacer(modifier = Modifier.padding(top = 10.dp))
                DateCard(
                    gregorianDate = viewModel.gregorianDate,
                    ifcDateText = viewModel.ifcDateText
                )

                // Takvim grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    userScrollEnabled = true,
                    flingBehavior = ScrollableDefaults.flingBehavior() // Performans iyileştirmesi
                ) {
                    items(
                        items = viewModel.months,
                        key = { it }
                    ) { month ->
                        key(month) {
                            val isCurrentDayFunc =
                                { day: Int -> viewModel.isCurrentDay(month, day) }
                            MonthCard(
                                month = month,
                                monthName = viewModel.getMonthName(month),
                                isCurrentMonth = month == viewModel.currentMonth,
                                isSpecialDay = viewModel.isSpecialDay(month),
                                specialDayName = viewModel.getSpecialDayName(month),
                                monthDescription = viewModel.getMonthDescription(month),
                                days = viewModel.getDaysForMonth(month),
                                currentDay = viewModel.currentDay,
                                isCurrentDay = isCurrentDayFunc,
                                cardHeight = cardHeight
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DateCard(
    gregorianDate: String,
    ifcDateText: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.gregorian_calendar),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = gregorianDate,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.international_fixed_calendar),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ifcDateText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}
@Composable
fun MonthCard(
    month: Int,
    monthName: String,
    isCurrentMonth: Boolean,
    isSpecialDay: Boolean,
    specialDayName: String,
    monthDescription: String,
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    cardHeight: Dp
) {
    // Cache calculations to improve performance
    val cachedDays = remember(days) { days }
    val colorScheme = MaterialTheme.colorScheme
    val containerColor = if (isCurrentMonth) {
        colorScheme.primary.copy(alpha = 0.1f)
    } else {
        colorScheme.surface
    }

    val textColor = if (isCurrentMonth) {
        colorScheme.primary
    } else {
        colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        border = BorderStroke(0.5.dp, if (isSystemInDarkTheme()) colorScheme.onPrimary else colorScheme.primary),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = monthName,
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = if (isCurrentMonth) FontWeight.Bold else FontWeight.Bold,
                maxLines = 1
            )

            if (month == 14) {
                Text(
                    text = specialDayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = monthDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "1",
                    fontSize = 40.sp,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Spacer(modifier = Modifier.height(1.dp))
                // Günler grid
                DayGrid(
                    days = cachedDays,
                    currentDay = currentDay,
                    isCurrentDay = isCurrentDay,
                    isCurrentMonth = isCurrentMonth
                )
            }
        }
    }
}

@Composable
fun DayGrid(
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    isCurrentMonth: Boolean
) {
    val rows = remember(days) { days.chunked(7) }
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { day ->
                    key(day) {
                        DayChip(
                            day = day,
                            isSelected = isCurrentDay(day),
                            isCurrentMonth = isCurrentMonth
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DayChip(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = when {
        isSelected -> colorScheme.primary
        //isCurrentMonth -> colorScheme.primary.copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected and isCurrentMonth-> colorScheme.onPrimary
        isCurrentMonth -> colorScheme.primary
       // isCurrentMonth -> colorScheme.onSecondary
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .size(22.dp)
            .then(
                if (isSelected || isCurrentMonth) {
                    Modifier
                        .clip(CircleShape)
                        .background(backgroundColor)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
            ),
            color = if (!isSelected and !isCurrentMonth) MaterialTheme.colorScheme.onSurface else textColor,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    PreviewFixedCalendarTheme {
        CalendarScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DateCard(
                    gregorianDate = "16 April 2024",
                    ifcDateText = "22 April"
                )

                MonthCardPreview()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthCardPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            MonthCard(
                month = 1,
                monthName = "January",
                isCurrentMonth = true,
                isSpecialDay = false,
                specialDayName = "",
                monthDescription = "January ayı, 28 günden oluşur.",
                days = (1..28).toList(),
                currentDay = 15,
                isCurrentDay = { it == 15 },
                cardHeight = 160.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpecialMonthCardPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            MonthCard(
                month = 7,
                monthName = "Sol",
                isCurrentMonth = false,
                isSpecialDay = true,
                specialDayName = "Sol",
                monthDescription = stringResource(R.string.sol_description),
                days = (1..1).toList(),
                currentDay = 0,
                isCurrentDay = { false },
                cardHeight = 160.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayGridPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            DayGrid(
                days = (1..28).toList(),
                currentDay = 15,
                isCurrentDay = { it == 15 },
                isCurrentMonth = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayChipPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DayChip(day = 15, isSelected = true, isCurrentMonth = true)
                DayChip(day = 16, isSelected = false, isCurrentMonth = true)
            }
        }
    }
}