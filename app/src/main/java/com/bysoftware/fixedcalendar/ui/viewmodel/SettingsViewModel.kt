package com.bysoftware.fixedcalendar.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.preference.PreferenceManager.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bysoftware.fixedcalendar.LocaleHelper
import com.bysoftware.fixedcalendar.data.ThemeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = themeDataStore.isDarkMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val useCustomTheme: StateFlow<Boolean> = themeDataStore.useCustomTheme
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val customPrimaryColor: StateFlow<Color> = themeDataStore.customPrimaryColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, Color(0xFFD32F2F))

    val language: StateFlow<String> = themeDataStore.language
        .stateIn(viewModelScope, SharingStarted.Eagerly, "English")

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setDarkMode(enabled)
        }
    }

    fun setUseCustomTheme(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setUseCustomTheme(enabled)
        }
    }

    fun setCustomPrimaryColor(color: Color) {
        viewModelScope.launch {
            themeDataStore.setCustomPrimaryColor(color)
        }
    }

    fun setLanguage(selectedLanguage: String) {
        viewModelScope.launch {
            themeDataStore.setLanguage(selectedLanguage)
        }
    }


    fun changeLanguage(context: Context, selectedLanguageName: String) {
        viewModelScope.launch {
            // Dil adını SharedPreferences'a kaydet
            val prefs = getDefaultSharedPreferences(context)
            prefs.edit { putString("selected_language", selectedLanguageName) }
            
            // DataStore'daki dil ayarını güncelle
            themeDataStore.setLanguage(selectedLanguageName)

            // Dil kodunu alıp LocaleHelper ile uygula
            val languageCode = themeDataStore.getLanguageCodeFromName(selectedLanguageName)
            LocaleHelper.setLocale(context, languageCode)

            // Daha yumuşak geçiş için
            kotlinx.coroutines.delay(50) // çok kısa bir gecikme
            
            if (context is Activity) {
                // Animasyon ile geçiş için Intent'i kullanmak yerine doğrudan yeniden oluşturma
                context.runOnUiThread {
                    context.recreate()
                }
            }
        }
    }


}