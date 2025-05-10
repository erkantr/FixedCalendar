package com.bysoftware.fixedcalendar

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.bysoftware.fixedcalendar.data.ThemeDataStore
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class FixedCalendarApp : Application() {
    
    @Inject
    lateinit var themeDataStore: ThemeDataStore
    
    override fun onCreate() {
        super.onCreate()
        
        // İlk çalıştırmada, cihaz diline göre uygulama dilini ayarla
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.contains("selected_language")) {
            val currentLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                resources.configuration.locales.get(0)
            } else {
                resources.configuration.locale
            }
            
            // Cihaz diline göre uygun dil adını belirle
            val deviceLanguageCode = currentLocale.language
            
            // Dilin uygulamada desteklenip desteklenmediğini kontrol et
            val languageName = when (deviceLanguageCode) {
                "tr" -> "Türkçe"
                "en" -> "English"
                "de" -> "Deutsch"
                "fr" -> "Français"
                "es" -> "Español"
                "pt" -> "Português"
                "pl" -> "Polski"
                "it" -> "Italiano"
                "ru" -> "Русский" 
                "zh" -> "中文"
                "ar" -> "العربية"
                else -> "English" // Varsayılan dil
            }
            
            // Dil adını SharedPreferences'a kaydet
            prefs.edit().putString("selected_language", languageName).apply()
            
            // LocaleHelper ile dili uygula
            LocaleHelper.setLocale(this, deviceLanguageCode)
        }
    }
    
    override fun attachBaseContext(base: Context) {
        // attachBaseContext ThemeDataStore inject edilmeden çağrılabilir,
        // bu yüzden direkt ThemeDataStore oluşturuyoruz
        val prefs = PreferenceManager.getDefaultSharedPreferences(base)
        val savedLanguageName = prefs.getString("selected_language", null)
        
        if (savedLanguageName != null) {
            // Dil daha önce ayarlanmış
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
            val languageCode = languageCodeMap[savedLanguageName] ?: "en"
            val context = LocaleHelper.setLocale(base, languageCode)
            super.attachBaseContext(context)
        } else {
            // Dil henüz ayarlanmamış, cihaz dilini kullan
            val currentLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                base.resources.configuration.locales.get(0)
            } else {
                base.resources.configuration.locale
            }
            
            val deviceLanguageCode = currentLocale.language
            val context = LocaleHelper.setLocale(base, deviceLanguageCode)
            super.attachBaseContext(context)
        }
    }
}