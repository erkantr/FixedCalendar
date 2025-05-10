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