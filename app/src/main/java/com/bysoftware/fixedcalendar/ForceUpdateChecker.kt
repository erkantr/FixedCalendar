package com.bysoftware.fixedcalendar

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
// BuildConfig import sorunlu olduğu için kaldırıyoruz
// import com.bysoftware.fixedcalendar.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

/**
 * Uygulama için zorunlu güncelleme kontrolü sağlayan sınıf
 */
class ForceUpdateChecker(private val context: Context) {

    // Version bilgilerini güvenli bir şekilde alıyoruz
    private val versionCode: Long by lazy {
        try {
            getAppVersionCode(context)
        } catch (e: Exception) {
            Log.e(TAG, "Version code alınamadı: ${e.message}")
            0L // Hata durumunda varsayılan değer
        }
    }

    interface OnUpdateNeededListener {
        fun onUpdateNeeded(updateUrl: String)
    }

    fun check(onUpdateNeededListener: OnUpdateNeededListener) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        
        // Remote Config için ayarlar - canlıda daha uzun süre olabilir
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // Test için 1 dakika
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Varsayılan değerler
        val defaults = HashMap<String, Any>()
        defaults["force_update_required"] = false
        defaults["force_update_current_version"] = versionCode
        defaults["force_update_store_url"] = "https://play.google.com/store/apps/details?id=${context.packageName}"
        
        remoteConfig.setDefaultsAsync(defaults)

        // Remote config verilerini fetch et
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Remote config'den verileri al
                    val updateRequired = remoteConfig.getBoolean("force_update_required")
                    val remoteVersion = remoteConfig.getLong("force_update_current_version")
                    val storeUrl = remoteConfig.getString("force_update_store_url")
                    
                    // Güncelleme gerekli mi kontrol et (Long türüyle doğru karşılaştırma)
                    Log.d(TAG, "Güncelleme kontrolü: remote=$remoteVersion, local=$versionCode, required=$updateRequired")
                    if (updateRequired && versionCode < remoteVersion) {
                        onUpdateNeededListener.onUpdateNeeded(storeUrl)
                    }
                } else {
                    Log.e(TAG, "Remote Config yüklenemedi: ${task.exception?.message}")
                }
            }
    }

    // Version code alma işlemini Android sürümüne göre yapan yardımcı fonksiyon
    private fun getAppVersionCode(context: Context): Long {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Version code alınamadı", e)
            0L
        }
    }

    companion object {
        private const val TAG = "ForceUpdateChecker"
    }
}
