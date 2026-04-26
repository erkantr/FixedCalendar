package com.bysoftware.fixedcalendar.ui.screens

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.screens.components.HeaderStyle
import com.bysoftware.fixedcalendar.ui.screens.components.HeroCompactHeader
import com.bysoftware.fixedcalendar.ui.screens.components.PillChipHeader
import com.bysoftware.fixedcalendar.ui.screens.components.StackedMinimalHeader
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.viewmodel.CalendarViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onInfoClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onConverterClick: () -> Unit = {},
    headerStyle: HeaderStyle = HeaderStyle.HERO_COMPACT,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current

    // Responsive sütun sayısı ve yatay boşluk
    val gridColumns = when {
        screenWidth < 360.dp -> 1
        screenWidth < 600.dp -> 2
        screenWidth < 840.dp -> 3
        else -> 4
    }
    val horizontalPadding: Dp = when {
        screenWidth < 360.dp -> 12.dp
        screenWidth < 600.dp -> 16.dp
        else -> 32.dp
    }

    Scaffold(
        topBar = {}
    ) { paddingValues ->
        Column(
            Modifier.fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = onConverterClick) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = stringResource(R.string.converter)
                        )
                    }
                    IconButton(onClick = {
                        val shareText = context.getString(
                            R.string.share_today_text,
                            viewModel.ifcDateTextRaw,
                            viewModel.gregorianDate
                        )
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                sendIntent,
                                context.getString(R.string.share)
                            )
                        )
                    }) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = stringResource(R.string.share)
                        )
                    }
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
                    .padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Ay navigasyonu + tarih kartı
                MonthNavigationBar(
                    monthLabel = viewModel.viewedMonthLabel,
                    onPrev = { viewModel.prevMonth() },
                    onNext = { viewModel.nextMonth() },
                    onToday = { viewModel.goToToday() },
                    canGoToday = !viewModel.isViewingToday
                )

                when (headerStyle) {
                    HeaderStyle.HERO_COMPACT -> HeroCompactHeader(
                        ifcText = viewModel.ifcDateText,
                        gregorianText = viewModel.gregorianDate
                    )
                    HeaderStyle.STACKED_MINIMAL -> StackedMinimalHeader(
                        ifcText = viewModel.ifcDateText,
                        gregorianText = viewModel.gregorianDate
                    )
                    HeaderStyle.PILL_CHIP -> PillChipHeader(
                        ifcText = viewModel.ifcDateText,
                        gregorianText = viewModel.gregorianDate
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    userScrollEnabled = true,
                    flingBehavior = ScrollableDefaults.flingBehavior()
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
                                isCurrentDay = isCurrentDayFunc
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthNavigationBar(
    monthLabel: String,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit,
    canGoToday: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrev) {
                Icon(
                    Icons.Default.ChevronLeft,
                    contentDescription = stringResource(R.string.previous_month),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = monthLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                if (canGoToday) {
                    AssistChip(
                        onClick = onToday,
                        label = {
                            Text(
                                stringResource(R.string.today),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
            IconButton(onClick = onNext) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = stringResource(R.string.next_month),
                    tint = MaterialTheme.colorScheme.primary
                )
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
    @Suppress("UNUSED_PARAMETER") isSpecialDay: Boolean,
    specialDayName: String,
    monthDescription: String,
    days: List<Int>,
    currentDay: Int,
    isCurrentDay: (Int) -> Boolean
) {
    val cachedDays = remember(days) { days }
    val colorScheme = MaterialTheme.colorScheme
    val containerColor = if (isCurrentMonth) {
        colorScheme.primary.copy(alpha = 0.1f)
    } else {
        colorScheme.surface
    }
    val textColor = colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp),
        border = BorderStroke(
            0.5.dp,
            if (isSystemInDarkTheme()) colorScheme.onPrimary else colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "1",
                    fontSize = 40.sp,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Spacer(modifier = Modifier.height(2.dp))
                DayGrid(
                    days = cachedDays,
                    currentDay = currentDay,
                    isCurrentDay = isCurrentDay,
                    isCurrentMonth = isCurrentMonth,
                    monthName = monthName
                )
            }
        }
    }
}

@Composable
fun DayGrid(
    days: List<Int>,
    @Suppress("UNUSED_PARAMETER") currentDay: Int,
    isCurrentDay: (Int) -> Boolean,
    isCurrentMonth: Boolean,
    monthName: String = ""
) {
    val rows = remember(days) { days.chunked(7) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { day ->
                    key(day) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            DayChip(
                                day = day,
                                isSelected = isCurrentDay(day),
                                isCurrentMonth = isCurrentMonth,
                                monthName = monthName
                            )
                        }
                    }
                }
                // Eksik günleri telafi etmek için (örn. 28 değilse)
                repeat(7 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun DayChip(
    day: Int,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    monthName: String = ""
) {
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = when {
        isSelected -> colorScheme.primary
        else -> Color.Transparent
    }
    val textColor = when {
        isSelected && isCurrentMonth -> colorScheme.onPrimary
        isCurrentMonth -> colorScheme.primary
        else -> colorScheme.onSurface
    }

    val description = if (isSelected) {
        stringResource(R.string.cd_day_today, day, monthName)
    } else {
        stringResource(R.string.cd_day, day, monthName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (isSelected) Modifier.clip(CircleShape).background(backgroundColor)
                else Modifier
            )
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
            ),
            color = textColor,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun MainPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MonthCardPreview()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
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
                isCurrentDay = { it == 15 }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
private fun MonthCardSmallPreview() {
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
                monthDescription = "",
                days = (1..28).toList(),
                currentDay = 15,
                isCurrentDay = { it == 15 }
            )
        }
    }
}
