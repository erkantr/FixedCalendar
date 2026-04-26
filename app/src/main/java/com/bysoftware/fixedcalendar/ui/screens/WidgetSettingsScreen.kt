package com.bysoftware.fixedcalendar.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.screens.components.ColorPickerDialog
import com.bysoftware.fixedcalendar.ui.screens.components.SwitchSettingItem
import com.bysoftware.fixedcalendar.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val widgetStyleKey by viewModel.widgetStyle.collectAsState()
    val widgetUseCustomTheme by viewModel.widgetUseCustomTheme.collectAsState()
    val widgetIsDarkMode by viewModel.widgetIsDarkMode.collectAsState()
    val widgetUseDynamicColor by viewModel.widgetUseDynamicColor.collectAsState()
    val widgetUseCustomColor by viewModel.widgetUseCustomColor.collectAsState()
    val widgetCustomPrimaryColor by viewModel.widgetCustomPrimaryColor.collectAsState()
    val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    var showColorPicker by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.settings_widget_title),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WidgetStyleSelectorCard(
                    selectedKey = widgetStyleKey,
                    onSelected = { viewModel.setWidgetStyle(it) }
                )

                SwitchSettingItem(
                    title = stringResource(R.string.widget_use_custom_theme),
                    description = stringResource(R.string.widget_use_custom_theme_desc),
                    checked = widgetUseCustomTheme,
                    onCheckedChange = { viewModel.setWidgetUseCustomTheme(it) }
                )

                if (!widgetUseCustomTheme) {
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.widget_follows_app_theme_info),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                SwitchSettingItem(
                    title = stringResource(R.string.widget_dark_mode),
                    description = stringResource(R.string.widget_dark_mode_desc),
                    checked = widgetIsDarkMode,
                    onCheckedChange = { viewModel.setWidgetIsDarkMode(it) },
                    enabled = widgetUseCustomTheme
                )

                if (supportsDynamicColor) {
                    SwitchSettingItem(
                        title = stringResource(R.string.widget_dynamic_color),
                        description = stringResource(R.string.widget_dynamic_color_desc),
                        checked = widgetUseDynamicColor,
                        onCheckedChange = { viewModel.setWidgetUseDynamicColor(it) },
                        enabled = widgetUseCustomTheme
                    )
                }

                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.widget_custom_color),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (widgetUseCustomTheme) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    }
                                )
                                Text(
                                    text = stringResource(R.string.widget_custom_color_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (widgetUseCustomTheme) {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    }
                                )
                            }
                            Switch(
                                checked = widgetUseCustomColor,
                                onCheckedChange = { viewModel.setWidgetUseCustomColor(it) },
                                enabled = widgetUseCustomTheme
                            )
                        }

                        if (widgetUseCustomTheme && widgetUseCustomColor) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.theme_color),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(widgetCustomPrimaryColor)
                                        .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                        .clickable { showColorPicker = true }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showColorPicker) {
            ColorPickerDialog(
                initialColor = widgetCustomPrimaryColor,
                onDismiss = { showColorPicker = false },
                onConfirm = { viewModel.setWidgetCustomPrimaryColor(it) }
            )
        }
    }
}
