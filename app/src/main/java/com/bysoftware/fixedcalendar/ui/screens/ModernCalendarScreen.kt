package com.bysoftware.fixedcalendar.ui.screens

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.screens.components.HeaderStyle
import com.bysoftware.fixedcalendar.ui.screens.components.HeroCompactHeader
import com.bysoftware.fixedcalendar.ui.screens.components.PillChipHeader
import com.bysoftware.fixedcalendar.ui.screens.components.StackedMinimalHeader
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.viewmodel.CalendarViewModel
import com.bysoftware.fixedcalendar.utils.IFCDateUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernCalendarScreen(
    onInfoClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onConverterClick: () -> Unit = {},
    showWeekNumbers: Boolean = false,
    headerStyle: HeaderStyle = HeaderStyle.HERO_COMPACT,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val currentDate = remember { LocalDate.now() }
    val ifcDate = remember { IFCDateUtils.convertToIFC(currentDate) }
    val context = LocalContext.current

    val gregorianFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    val gregorianDateStr = currentDate.format(gregorianFormatter)

    val monthName = IFCDateUtils.getMonthName(ifcDate.month)
    val dayText = if (ifcDate.isLeapDay || ifcDate.isYearDay) {
        IFCDateUtils.getSpecialDayName(ifcDate.isLeapDay, ifcDate.isYearDay)
    } else {
        "${ifcDate.day} $monthName"
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val horizontalPadding = when {
        screenWidth < 360.dp -> 12.dp
        screenWidth < 600.dp -> 16.dp
        else -> 24.dp
    }
    val cardSpacing = if (screenWidth < 360.dp) 8.dp else 12.dp
    val minCardSize = when {
        screenWidth < 360.dp -> 140.dp
        screenWidth < 600.dp -> 160.dp
        else -> 180.dp
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = onConverterClick) {
                        Icon(
                            Icons.Default.SwapHoriz,
                            contentDescription = stringResource(R.string.converter),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = {
                        val shareText = context.getString(
                            R.string.share_today_text,
                            dayText,
                            gregorianDateStr
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
                            contentDescription = stringResource(R.string.share),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = stringResource(R.string.info),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_top),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = minCardSize),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = horizontalPadding),
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(cardSpacing),
            verticalArrangement = Arrangement.spacedBy(cardSpacing)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                when (headerStyle) {
                    HeaderStyle.HERO_COMPACT -> HeroCompactHeader(
                        ifcText = dayText,
                        gregorianText = gregorianDateStr
                    )
                    HeaderStyle.STACKED_MINIMAL -> StackedMinimalHeader(
                        ifcText = dayText,
                        gregorianText = gregorianDateStr
                    )
                    HeaderStyle.PILL_CHIP -> PillChipHeader(
                        ifcText = dayText,
                        gregorianText = gregorianDateStr
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                MonthNavigationBar(
                    monthLabel = viewModel.viewedMonthLabel,
                    onPrev = { viewModel.prevMonth() },
                    onNext = { viewModel.nextMonth() },
                    onToday = { viewModel.goToToday() },
                    canGoToday = !viewModel.isViewingToday
                )
            }

            val months = (1..13).toList()
            items(months) { month ->
                ModernMonthCard(
                    month = month,
                    currentMonth = ifcDate.month,
                    currentDay = ifcDate.day,
                    isSpecial = month == 7,
                    isSol = month == 7,
                    showWeekNumbers = showWeekNumbers,
                    isViewingToday = viewModel.isViewingToday
                )
            }

            item {
                YearDayCard()
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(R.string.leap_day_footer),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun ModernMonthCard(
    month: Int,
    currentMonth: Int,
    currentDay: Int,
    @Suppress("UNUSED_PARAMETER") isSpecial: Boolean,
    isSol: Boolean = false,
    showWeekNumbers: Boolean = false,
    isViewingToday: Boolean = true
) {
    val isCurrentMonth = isViewingToday && month == currentMonth
    val monthName = IFCDateUtils.getMonthName(month)

    val cardColor = when {
        isSol -> MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        isSol -> MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
        isCurrentMonth -> MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    val alpha = if (isCurrentMonth || isSol) 1f else 0.85f
    val weekShort = stringResource(R.string.week_short)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSol) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (isCurrentMonth) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = stringResource(R.string.now_badge),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (showWeekNumbers) {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (week in 0..3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showWeekNumbers) {
                            Text(
                                text = "$weekShort${week + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                modifier = Modifier.width(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        for (dayOfWeek in 0..6) {
                            val day = week * 7 + dayOfWeek + 1
                            if (day <= 28) {
                                val isToday = isCurrentMonth && day == currentDay
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isToday) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize(0.85f)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary)
                                                .semantics {
                                                    contentDescription = "Today, day $day of $monthName"
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = day.toString(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = day.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 11.sp,
                                            color = if (isSol) {
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                            } else {
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                            }
                                        )
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearDayCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.year_day_header),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = stringResource(R.string.global_holiday),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "29",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.dec_fixed),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 360)
@Composable
fun ModernCalendarScreenPreview() {
    FixedCalendarTheme {
        ModernCalendarScreen(
            onInfoClick = {},
            onSettingsClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 600)
@Composable
fun ModernCalendarScreenTabletPreview() {
    FixedCalendarTheme {
        ModernCalendarScreen(
            onInfoClick = {},
            onSettingsClick = {}
        )
    }
}
