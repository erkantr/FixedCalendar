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
import com.bysoftware.fixedcalendar.notifications.NotificationScheduler
import androidx.glance.appwidget.updateAll
import com.bysoftware.fixedcalendar.widget.CalendarWidget
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

    val useDynamicColor: StateFlow<Boolean> = themeDataStore.useDynamicColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val showWeekNumbers: StateFlow<Boolean> = themeDataStore.showWeekNumbers
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val enableNotifications: StateFlow<Boolean> = themeDataStore.enableNotifications
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val headerStyle: StateFlow<Int> = themeDataStore.headerStyle
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val widgetStyle: StateFlow<Int> = themeDataStore.widgetStyle
        .stateIn(viewModelScope, SharingStarted.Eagerly, 3)

    val hasSeenOnboarding: StateFlow<Boolean> = themeDataStore.hasSeenOnboarding
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val widgetUseCustomTheme: StateFlow<Boolean> = themeDataStore.widgetUseCustomTheme
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val widgetIsDarkMode: StateFlow<Boolean> = themeDataStore.widgetIsDarkMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val widgetUseDynamicColor: StateFlow<Boolean> = themeDataStore.widgetUseDynamicColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val widgetUseCustomColor: StateFlow<Boolean> = themeDataStore.widgetUseCustomColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val widgetCustomPrimaryColor: StateFlow<Color> = themeDataStore.widgetCustomPrimaryColor
        .stateIn(viewModelScope, SharingStarted.Eagerly, Color(0xFFD32F2F))

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

    fun setUseDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setUseDynamicColor(enabled)
            updateWidget()
        }
    }

    fun setShowWeekNumbers(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setShowWeekNumbers(enabled)
        }
    }

    fun setHeaderStyle(value: Int) {
        viewModelScope.launch {
            themeDataStore.setHeaderStyle(value)
            updateWidget()
        }
    }

    fun setWidgetStyle(value: Int) {
        viewModelScope.launch {
            themeDataStore.setWidgetStyle(value)
            updateWidget()
        }
    }

    fun setHasSeenOnboarding(value: Boolean) {
        viewModelScope.launch {
            themeDataStore.setHasSeenOnboarding(value)
        }
    }

    fun setWidgetUseCustomTheme(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setWidgetUseCustomTheme(enabled)
            updateWidget()
        }
    }

    fun setWidgetIsDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setWidgetIsDarkMode(enabled)
            updateWidget()
        }
    }

    fun setWidgetUseDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setWidgetUseDynamicColor(enabled)
            updateWidget()
        }
    }

    fun setWidgetUseCustomColor(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setWidgetUseCustomColor(enabled)
            updateWidget()
        }
    }

    fun setWidgetCustomPrimaryColor(color: Color) {
        viewModelScope.launch {
            themeDataStore.setWidgetCustomPrimaryColor(color)
            updateWidget()
        }
    }

    fun setEnableNotifications(enabled: Boolean) {
        viewModelScope.launch {
            themeDataStore.setEnableNotifications(enabled)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (enabled) {
                    NotificationScheduler.scheduleAll(appContext)
                } else {
                    NotificationScheduler.cancelAll(appContext)
                }
            }
        }
    }
    
    /**
     * Widget'ı anında günceller.
     * - Doğrudan Glance updateAll çağırır (güvenilir, instant)
     * - Ayrıca yedek olarak broadcast yollar.
     */
    private suspend fun updateWidget() {
        runCatching { CalendarWidget().updateAll(appContext) }
        runCatching {
            val intent = Intent(appContext, CalendarWidgetReceiver::class.java).apply {
                action = CalendarWidgetReceiver.ACTION_UPDATE_WIDGET
            }
            appContext.sendBroadcast(intent)
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