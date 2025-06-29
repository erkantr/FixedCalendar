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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.viewmodel.CalendarViewModel

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
    val screenHeight = configuration.screenHeightDp.dp
    val isTablet = remember { screenWidth >= 600.dp }
    val isLandscape = remember { screenWidth > screenHeight }
    
    // Responsive değerler - Tüm ekranları destekler
    val gridColumns = remember { 
        when {
            isTablet -> 4
            isLandscape -> 3
            else -> 2
        }
    }
    
    val cardHeight = remember { 
        when {
            isTablet -> 200.dp
            isLandscape -> 120.dp // Landscape'de çok daha küçük
            else -> 160.dp // Portrait'te daha küçük
        }
    }
    
    val dayChipSize = remember {
        when {
            isTablet -> 32.dp
            isLandscape -> 20.dp // Landscape'de çok küçük
            else -> 24.dp // Portrait'te normal
        }
    }
    
    val monthTitleSize = remember {
        when {
            isTablet -> 18.sp
            isLandscape -> 14.sp
            else -> 16.sp
        }
    }

    Scaffold(

        topBar = {
        }
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding() // Status bar için padding - doğru yer
        ) {
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
                Spacer(modifier = Modifier.height(8.dp))
                DateCard(
                    gregorianDate = viewModel.gregorianDate,
                    ifcDateText = viewModel.ifcDateText,
                    currentYear = viewModel.currentYear
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
                                cardHeight = cardHeight,
                                dayChipSize = dayChipSize,
                                monthTitleSize = monthTitleSize,
                                isLandscape = isLandscape,
                                onMonthClick = {
                                    // TODO: Navigate to detailed month view
                                    // Şimdilik sadece placeholder, AŞAMA 2'de implement edeceğiz
                                }
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
    ifcDateText: String,
    currentYear: Int
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sol taraf - Yıl ve Gregorian
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = currentYear.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = gregorianDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
            
            // Sağ taraf - IFC
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(R.string.international_fixed_calendar),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
                Text(
                    text = ifcDateText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
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
    cardHeight: Dp,
    dayChipSize: Dp,
    monthTitleSize: TextUnit,
    isLandscape: Boolean,
    onMonthClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    
    val containerColor = when {
        isCurrentMonth -> colorScheme.primaryContainer.copy(alpha = 0.4f) // Daha belirgin
        else -> colorScheme.surface.copy(alpha = 0.95f) // Hafif gölgelendirme
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isCurrentMonth) 4.dp else 2.dp // Daha belirgin gölge
        ),
        onClick = onMonthClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isLandscape) 8.dp else 12.dp), // Landscape'de daha az padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(if (isLandscape) 4.dp else 6.dp)
        ) {
            // Month Header - Daha compact
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCurrentMonth) {
                    Box(
                        modifier = Modifier
                            .size(6.dp) // Daha küçük indicator
                            .background(colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = monthName,
                    fontSize = monthTitleSize,
                    color = if (isCurrentMonth) colorScheme.primary else colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            if (month == 14) {
                // Year Day özel tasarımı - Compact
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🎊",
                            fontSize = if (isLandscape) 24.sp else 32.sp,
                            maxLines = 1
                        )
                        Text(
                            text = specialDayName,
                            fontSize = if (isLandscape) 12.sp else 14.sp,
                            color = colorScheme.tertiary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            } else {
                // Responsive Day Grid
                ResponsiveDayGrid(
                    days = days,
                    currentDay = currentDay,
                    isCurrentDay = isCurrentDay,
                    isCurrentMonth = isCurrentMonth,
                    dayChipSize = dayChipSize,
                    isLandscape = isLandscape
                )
            }
        }
    }
}

@Composable
fun ResponsiveDayGrid(
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    isCurrentMonth: Boolean,
    dayChipSize: Dp,
    isLandscape: Boolean
) {
    val rows = remember(days) { days.chunked(7) }
    val colorScheme = MaterialTheme.colorScheme
    val dayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(if (isLandscape) 1.dp else 2.dp)
    ) {
        // Compact day headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorScheme.primaryContainer.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(vertical = if (isLandscape) 2.dp else 3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayHeaders.forEach { header ->
                Text(
                    text = header,
                    fontSize = if (isLandscape) 8.sp else 10.sp,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Responsive day rows - GARANTİLİ FİT
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dayChipSize),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Her gün için eşit alan
                repeat(7) { index ->
                    if (index < row.size) {
                        ResponsiveDayChip(
                            day = row[index],
                            isSelected = isCurrentDay(row[index]),
                            isCurrentMonth = isCurrentMonth,
                            chipSize = dayChipSize,
                            isLandscape = isLandscape
                        )
                    } else {
                        // Boş alan - aynı boyutta
                        Spacer(modifier = Modifier.size(dayChipSize))
                    }
                }
            }
        }
    }
}

@Composable
fun ModernDayGrid(
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    isCurrentMonth: Boolean
) {
    // Backward compatibility - default values
    ResponsiveDayGrid(
        days = days,
        currentDay = currentDay,
        isCurrentDay = isCurrentDay,
        isCurrentMonth = isCurrentMonth,
        dayChipSize = 28.dp,
        isLandscape = false
    )
}

@Composable
fun DayGrid(
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    isCurrentMonth: Boolean
) {
    ModernDayGrid(days, currentDay, isCurrentDay, isCurrentMonth)
}


@Composable
fun ResponsiveDayChip(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    chipSize: Dp,
    isLandscape: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    
    val backgroundColor = when {
        isSelected -> colorScheme.primary
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> colorScheme.onPrimary
        isCurrentMonth -> colorScheme.onSurface
        else -> colorScheme.onSurfaceVariant
    }

    val fontSize = when {
        isLandscape -> (chipSize.value * 0.4f).sp // Landscape'de çok küçük font
        chipSize <= 20.dp -> 8.sp
        chipSize <= 24.dp -> 10.sp
        else -> 12.sp
    }

    Box(
        modifier = Modifier
            .size(chipSize)
            .clip(RoundedCornerShape(if (isLandscape) 4.dp else 6.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = fontSize,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
fun ModernDayChip(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean
) {
    // Backward compatibility
    ResponsiveDayChip(
        day = day,
        isSelected = isSelected,
        isCurrentMonth = isCurrentMonth,
        chipSize = 30.dp,
        isLandscape = false
    )
}

@Composable
fun DayChip(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean
) {
    ModernDayChip(day, isSelected, isCurrentMonth)
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
                    ifcDateText = "22 April",
                    currentYear = 2024
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
                cardHeight = 160.dp,
                dayChipSize = 24.dp,
                monthTitleSize = 16.sp,
                isLandscape = false,
                onMonthClick = {}
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
                cardHeight = 160.dp,
                dayChipSize = 24.dp,
                monthTitleSize = 16.sp,
                isLandscape = false,
                onMonthClick = {}
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
            ResponsiveDayGrid(
                days = (1..28).toList(),
                currentDay = 15,
                isCurrentDay = { it == 15 },
                isCurrentMonth = true,
                dayChipSize = 24.dp,
                isLandscape = false
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
                ResponsiveDayChip(day = 15, isSelected = true, isCurrentMonth = true, chipSize = 24.dp, isLandscape = false)
                ResponsiveDayChip(day = 16, isSelected = false, isCurrentMonth = true, chipSize = 24.dp, isLandscape = false)
            }
        }
    }
}