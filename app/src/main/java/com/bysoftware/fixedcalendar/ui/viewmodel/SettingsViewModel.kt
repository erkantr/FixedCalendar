package com.bysoftware.fixedcalendar.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.preference.PreferenceManager.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bysoftware.fixedcalendar.LocaleHelper
import com.bysoftware.fixedcalendar.data.ThemeDataStore
import com.bysoftware.fixedcalendar.widget.CalendarWidgetReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeDataStore: ThemeDataStore,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = themeDataStore.isDarkMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val useCustomTheme: StateFlow<Boolean> = themeDataStore.useCustomTheme
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val customPrimaryColor: StateFlow<Color> = themeDataStore.customPrimaryColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, Color(0xFFD32F2F))

    val language: StateFlow<String> = themeDataStore.language
        .stateIn(viewModelScope, SharingStarted.Eagerly, "English")

    val useModernDesign: StateFlow<Boolean> = themeDataStore.useModernDesign
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setDarkMode(enabled)
            updateWidget()
        }
    }

    fun setUseCustomTheme(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setUseCustomTheme(enabled)
            updateWidget()
        }
    }

    fun setCustomPrimaryColor(color: Color) {
        viewModelScope.launch {
            themeDataStore.setCustomPrimaryColor(color)
            updateWidget()
        }
    }

    fun setUseModernDesign(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setUseModernDesign(enabled)
        }
    }
    
    private fun updateWidget() {
        val intent = Intent(appContext, CalendarWidgetReceiver::class.java).apply {
            action = CalendarWidgetReceiver.ACTION_UPDATE_WIDGET
        }
        appContext.sendBroadcast(intent)
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