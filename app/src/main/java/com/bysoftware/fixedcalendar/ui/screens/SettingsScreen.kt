package com.bysoftware.fixedcalendar.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bysoftware.fixedcalendar.R
import com.bysoftware.fixedcalendar.ui.screens.components.HeaderStyle
import com.bysoftware.fixedcalendar.ui.screens.components.NavigationSettingItem
import com.bysoftware.fixedcalendar.ui.screens.components.SwitchSettingItem
import com.bysoftware.fixedcalendar.ui.theme.PreviewFixedCalendarTheme
import com.bysoftware.fixedcalendar.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onThemeSettingsClick: () -> Unit,
    onWidgetSettingsClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    var expanded by remember { mutableStateOf(false) }
    val language by viewModel.language.collectAsState()
    val enableNotifications by viewModel.enableNotifications.collectAsState()
    val headerStyleKey by viewModel.headerStyle.collectAsState()

    var selectedLanguage by remember { mutableStateOf(language) }

    LaunchedEffect(language) {
        selectedLanguage = language
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.settings_top),
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
                NavigationSettingItem(
                    icon = Icons.Filled.Palette,
                    title = stringResource(R.string.settings_theme_title),
                    description = stringResource(R.string.settings_theme_desc),
                    onClick = onThemeSettingsClick
                )

                NavigationSettingItem(
                    icon = Icons.Filled.GridView,
                    title = stringResource(R.string.settings_widget_title),
                    description = stringResource(R.string.settings_widget_desc),
                    onClick = onWidgetSettingsClick
                )

                HeaderStyleSelectorCard(
                    selected = HeaderStyle.fromKey(headerStyleKey),
                    onSelected = { viewModel.setHeaderStyle(it.key) }
                )

                SwitchSettingItem(
                    title = stringResource(R.string.enable_notifications),
                    description = stringResource(R.string.enable_notifications_desc),
                    checked = enableNotifications,
                    onCheckedChange = { viewModel.setEnableNotifications(it) }
                )

                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.language),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedLanguage,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    listOf(
                                        "Türkçe", "English", "Deutsch", "Français", "Español",
                                        "Português", "Polski", "Italiano", "Русский", "中文", "العربية"
                                    ).forEach { lang ->
                                        DropdownMenuItem(
                                            text = { Text(lang) },
                                            onClick = {
                                                selectedLanguage = lang
                                                viewModel.setLanguage(lang)
                                                viewModel.changeLanguage(context, lang)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                stringResource(R.string.calendar),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                context.packageManager.getPackageInfo(
                                    context.packageName,
                                    PackageManager.PackageInfoFlags.of(0)
                                )
                            } else {
                                context.packageManager.getPackageInfo(context.packageName, 0)
                            }
                            val versionName = packageInfo.versionName

                            Text(
                                stringResource(R.string.version) + " " + versionName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// Header style selector (Tema/Renk değil → ana ekranda kalıyor: kullanıcının
// hızlıca üst başlık stilini değiştirebilmesi için.)
// =============================================================================

@Composable
internal fun HeaderStyleSelectorCard(
    selected: HeaderStyle,
    onSelected: (HeaderStyle) -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ViewHeadline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.header_style_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = stringResource(R.string.header_style_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderStyle.values().forEach { style ->
                    HeaderStyleOption(
                        style = style,
                        selected = style == selected,
                        onSelect = { onSelected(style) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderStyleOption(
    style: HeaderStyle,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val (labelRes, descRes) = when (style) {
        HeaderStyle.HERO_COMPACT -> R.string.header_style_compact to R.string.header_style_compact_desc
        HeaderStyle.STACKED_MINIMAL -> R.string.header_style_stacked to R.string.header_style_stacked_desc
        HeaderStyle.PILL_CHIP -> R.string.header_style_pill to R.string.header_style_pill_desc
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(selected = selected, onClick = null)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(labelRes),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(descRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =============================================================================
// Widget style selector — WidgetSettingsScreen tarafından kullanılır.
// =============================================================================

@Composable
internal fun WidgetStyleSelectorCard(
    selectedKey: Int,
    onSelected: (Int) -> Unit
) {
    val options = listOf(
        Triple(0, R.string.widget_style_text_compact, R.string.widget_style_text_compact_desc),
        Triple(1, R.string.widget_style_text_stacked, R.string.widget_style_text_stacked_desc),
        Triple(2, R.string.widget_style_text_pill, R.string.widget_style_text_pill_desc),
        Triple(3, R.string.widget_style_grid_classic, R.string.widget_style_grid_classic_desc)
    )

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.widget_style_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.widget_style_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { (key, labelRes, descRes) ->
                    val isSelected = key == selectedKey
                    val borderColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = { onSelected(key) },
                                role = Role.RadioButton
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(selected = isSelected, onClick = null)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(labelRes),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = stringResource(descRes),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    PreviewFixedCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Preview için ViewModel oluşturulamadığından gerçek ekranı gösteremiyoruz.
            // Statik bir başlık göstermek yeterli.
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.settings_top),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
