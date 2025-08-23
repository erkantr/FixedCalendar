package com.bysoftware.fixedcalendar.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.viewmodel.CalendarViewModel
import com.bysoftware.fixedcalendar.ui.viewmodel.Month

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onInfoClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = remember { screenWidth >= 600.dp }
    val gridColumns = remember { if (isTablet) 4 else 2 }
    val cardHeight = remember { if (isTablet) 220.dp else 180.dp }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = stringResource(R.string.info_top),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_top),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .systemBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Modern Date Card
            Spacer(modifier = Modifier.height(16.dp))
            ModernDateCard(
                gregorianDate = viewModel.gregorianDate,
                ifcDateText = viewModel.ifcDateText
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Calendar Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.months) { month ->
                    ModernMonthCard(
                        month = month.month,
                        monthName = month.monthName,
                        days = month.days,
                        currentDay = viewModel.currentDay,
                        isCurrentDay = { day -> viewModel.isCurrentDay(month.month, day) },
                        cardHeight = cardHeight
                    )
                }
            }
        }
    }
}

@Composable
fun ModernDateCard(
    gregorianDate: String,
    ifcDateText: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = remember { screenWidth >= 600.dp }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        if (isTablet) {
            // Tablet - Yatay düzen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateCardSection(
                    icon = Icons.Default.DateRange,
                    title = stringResource(R.string.gregorian_calendar),
                    date = gregorianDate,
                    alignment = Alignment.Start
                )
                
                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                )
                
                DateCardSection(
                    icon = Icons.Default.CalendarToday,
                    title = stringResource(R.string.international_fixed_calendar),
                    date = ifcDateText,
                    alignment = Alignment.End,
                    iconAtEnd = true
                )
            }
        } else {
            // Phone - Dikey düzen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DateCardSection(
                    icon = Icons.Default.DateRange,
                    title = stringResource(R.string.gregorian_calendar),
                    date = gregorianDate,
                    alignment = Alignment.CenterHorizontally
                )
                
                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                )
                
                DateCardSection(
                    icon = Icons.Default.CalendarToday,
                    title = stringResource(R.string.international_fixed_calendar),
                    date = ifcDateText,
                    alignment = Alignment.CenterHorizontally,
                    iconAtEnd = true
                )
            }
        }
    }
}

@Composable
private fun DateCardSection(
    icon: ImageVector,
    title: String,
    date: String,
    alignment: Alignment.Horizontal,
    iconAtEnd: Boolean = false
) {
    Column(
        horizontalAlignment = when (alignment) {
            Alignment.Start -> Alignment.Start
            Alignment.End -> Alignment.End
            else -> Alignment.CenterHorizontally
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!iconAtEnd) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            if (iconAtEnd) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
fun ModernMonthCard(
    month: Int,
    monthName: String,
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    cardHeight: Dp
) {
    val isCurrentMonth = remember { month == 7 } // July ayı için test
    val colorScheme = MaterialTheme.colorScheme
    
    val containerColor = when {
        isCurrentMonth -> colorScheme.primaryContainer.copy(alpha = 0.2f)
        else -> colorScheme.surface
    }
    
    val borderColor = when {
        isCurrentMonth -> colorScheme.primary.copy(alpha = 0.2f)
        else -> colorScheme.outline.copy(alpha = 0.08f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentMonth) 4.dp else 1.dp
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Month Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCurrentMonth) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isCurrentMonth) colorScheme.primary else colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (month == 14) {
                // Year Day Special Design
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🎊",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Year Day",
                            style = MaterialTheme.typography.titleMedium,
                            color = colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Modern Day Grid
                ModernDayGrid(
                    days = days,
                    currentDay = currentDay,
                    isCurrentDay = isCurrentDay,
                    isCurrentMonth = isCurrentMonth
                )
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
    val rows = remember(days) { days.chunked(7) }
    val colorScheme = MaterialTheme.colorScheme
    val dayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Day Headers - Responsive
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayHeaders.forEach { header ->
                Text(
                    text = header,
                    fontSize = 8.sp,
                    color = colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Day Rows - Responsive
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { index ->
                    if (index < row.size) {
                        ResponsiveDayChip(
                            day = row[index],
                            isSelected = isCurrentDay(row[index]) && isCurrentMonth,
                            isCurrentMonth = isCurrentMonth
                        )
                    } else {
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ResponsiveDayChip(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean
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

    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 9.sp,
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

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    PreviewFixedCalendarTheme {
        CalendarScreen(
            onInfoClick = {},
            onSettingsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ModernDateCardPreview() {
    PreviewFixedCalendarTheme {
        ModernDateCard(
            gregorianDate = "16 April 2024",
            ifcDateText = "22 April"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ModernMonthCardPreview() {
    PreviewFixedCalendarTheme {
        ModernMonthCard(
            month = 1,
            monthName = "January",
            days = (1..28).toList(),
            currentDay = 19,
            isCurrentDay = { it == 19 },
            cardHeight = 180.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ModernDayGridPreview() {
    PreviewFixedCalendarTheme {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ModernDayGrid(
                days = (1..28).toList(),
                currentDay = 19,
                isCurrentDay = { it == 19 },
                isCurrentMonth = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModernDayChipPreview() {
    PreviewFixedCalendarTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModernDayChip(day = 19, isSelected = true, isCurrentMonth = true)
            ModernDayChip(day = 20, isSelected = false, isCurrentMonth = true)
            ModernDayChip(day = 17, isSelected = false, isCurrentMonth = false)
        }
    }
}