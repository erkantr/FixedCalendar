package com.bysoftware.fixedcalendar.data

import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bysoftware.fixedcalendar.LocaleHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

@Singleton
class ThemeDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    val languageCodeMap = mapOf(
        "Türkçe" to "tr",
        "English" to "en",
        "Deutsch" to "de",
        "Français" to "fr",
        "Español" to "es",
        "Português" to "pt",
        "Polski" to "pl",
        "Italiano" to "it",
        "Русский" to "ru",
        "中文" to "zh",
        "العربية" to "ar"
    )

    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val USE_CUSTOM_THEME = booleanPreferencesKey("use_custom_theme")
        val CUSTOM_PRIMARY_COLOR = intPreferencesKey("custom_primary_color")
        val LANGUAGE = stringPreferencesKey("language")
        val USE_MODERN_DESIGN = booleanPreferencesKey("use_modern_design")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val SHOW_WEEK_NUMBERS = booleanPreferencesKey("show_week_numbers")
        val ENABLE_NOTIFICATIONS = booleanPreferencesKey("enable_notifications")
        val HEADER_STYLE = intPreferencesKey("header_style")
        val WIDGET_STYLE = intPreferencesKey("widget_style")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        // Widget'a özel tema (uygulama temasından bağımsız)
        val WIDGET_USE_CUSTOM_THEME = booleanPreferencesKey("widget_use_custom_theme")
        val WIDGET_IS_DARK_MODE = booleanPreferencesKey("widget_is_dark_mode")
        val WIDGET_USE_DYNAMIC_COLOR = booleanPreferencesKey("widget_use_dynamic_color")
        val WIDGET_USE_CUSTOM_COLOR = booleanPreferencesKey("widget_use_custom_color")
        val WIDGET_CUSTOM_PRIMARY_COLOR = intPreferencesKey("widget_custom_primary_color")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_DARK_MODE] ?: false
    }

    val useCustomTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USE_CUSTOM_THEME] ?: false
    }

    val customPrimaryColor: Flow<Color> = context.dataStore.data.map { preferences ->
        Color(preferences[PreferencesKeys.CUSTOM_PRIMARY_COLOR] ?: Color(0xFFD32F2F).toArgb())
    }

    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LANGUAGE] ?: "English"
    }

    val useModernDesign: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USE_MODERN_DESIGN] ?: false
    }

    val useDynamicColor: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: false
    }

    val showWeekNumbers: Flow<Boolean> = context.dataStore.data.map { preferences ->
        // Yeni kullanıcılar için varsayılan açık
        preferences[PreferencesKeys.SHOW_WEEK_NUMBERS] ?: true
    }

    val enableNotifications: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ENABLE_NOTIFICATIONS] ?: false
    }

    val headerStyle: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HEADER_STYLE] ?: 0
    }

    val widgetStyle: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WIDGET_STYLE] ?: 3 // Varsayılan: GRID_CLASSIC
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HAS_SEEN_ONBOARDING] ?: false
    }

    // Widget'a özel tema (default kapalı; widget app temasını izler)
    val widgetUseCustomTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WIDGET_USE_CUSTOM_THEME] ?: false
    }

    val widgetIsDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WIDGET_IS_DARK_MODE] ?: false
    }

    val widgetUseDynamicColor: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WIDGET_USE_DYNAMIC_COLOR] ?: false
    }

    val widgetUseCustomColor: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.WIDGET_USE_CUSTOM_COLOR] ?: false
    }

    val widgetCustomPrimaryColor: Flow<Color> = context.dataStore.data.map { preferences ->
        Color(preferences[PreferencesKeys.WIDGET_CUSTOM_PRIMARY_COLOR] ?: Color(0xFFD32F2F).toArgb())
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun setUseCustomTheme(useCustomTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_CUSTOM_THEME] = useCustomTheme
        }
    }

    suspend fun setCustomPrimaryColor(color: Color) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_PRIMARY_COLOR] = color.toArgb()
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    suspend fun setUseModernDesign(useModernDesign: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_MODERN_DESIGN] = useModernDesign
        }
    }

    suspend fun setUseDynamicColor(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = enabled
        }
    }

    suspend fun setShowWeekNumbers(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_WEEK_NUMBERS] = enabled
        }
    }

    suspend fun setEnableNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_NOTIFICATIONS] = enabled
        }
    }

    suspend fun setHeaderStyle(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HEADER_STYLE] = value
        }
    }

    suspend fun setWidgetStyle(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_STYLE] = value
        }
    }

    suspend fun setHasSeenOnboarding(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_SEEN_ONBOARDING] = value
        }
    }

    suspend fun setWidgetUseCustomTheme(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_USE_CUSTOM_THEME] = value
        }
    }

    suspend fun setWidgetIsDarkMode(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_IS_DARK_MODE] = value
        }
    }

    suspend fun setWidgetUseDynamicColor(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_USE_DYNAMIC_COLOR] = value
        }
    }

    suspend fun setWidgetUseCustomColor(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_USE_CUSTOM_COLOR] = value
        }
    }

    suspend fun setWidgetCustomPrimaryColor(color: Color) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WIDGET_CUSTOM_PRIMARY_COLOR] = color.toArgb()
        }
    }

     fun getLanguageCodeFromName(languageName: String): String {
        return languageCodeMap[languageName] ?: "en" // fallback
    }

    fun applySavedLanguage(context: Context): Context {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val savedLanguageName = prefs.getString("selected_language", "English") ?: "English"
        val languageCode = getLanguageCodeFromName(savedLanguageName)
        return LocaleHelper.setLocale(context, languageCode)
    }
} 