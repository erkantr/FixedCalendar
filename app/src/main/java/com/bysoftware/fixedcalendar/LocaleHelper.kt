package com.bysoftware.fixedcalendar

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        
        // API Level 25+ durumunda dil değişimini daha düzgün uygulamak için
        // Kaynakları optimize ederek yükleme
        return context.createConfigurationContext(config)
    }
    
    // Mevcut locale bilgisini almak için yardımcı metod
    fun getLocale(context: Context): Locale {
        val config = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0)
        } else {
            config.locale
        }
    }
}
