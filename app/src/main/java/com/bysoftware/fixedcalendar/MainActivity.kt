package com.bysoftware.fixedcalendar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bysoftware.fixedcalendar.data.ThemeDataStore
import com.bysoftware.fixedcalendar.ui.screens.CalendarScreen
import com.bysoftware.fixedcalendar.ui.screens.InfoScreen
import com.bysoftware.fixedcalendar.ui.screens.SettingsScreen
import com.bysoftware.fixedcalendar.ui.theme.FixedCalendarTheme
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeDataStore: ThemeDataStore
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Debug amacıyla log ekleyelim
        Log.d("ADS_DEBUG", "MainActivity.onCreate called")
        
        // Zorunlu güncelleme kontrolü
        checkForUpdates()
        
        //MobileAds.setLogLevel(MobileAds.LogLevel.DEBUG)
        
        // İlklendirme
        MobileAds.initialize(this) { initStatus ->
            Log.d("ADS_DEBUG", "MobileAds initialization complete!")
        }
        
        setContent {
            // Test cihazı belirtelim
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                    .build()
            )
            
            FixedCalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        AdmobBanner(modifier = Modifier.fillMaxWidth())

                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = "calendar",
                            modifier = Modifier.weight(1f)
                        ) {
                            composable("calendar") {
                                CalendarScreen(
                                    onInfoClick = { navController.navigate("info") },
                                    onSettingsClick = { navController.navigate("settings") }
                                )
                            }
                            composable("info") {
                                InfoScreen(
                                    onBackClick = { navController.navigateUp() }
                                )
                            }
                            composable("settings") {
                                SettingsScreen(
                                    onBackClick = { navController.navigateUp() }
                                )
                            }
                        }
                        
                        // XML tabanlı ad view'ı göster
                       // XmlAdBanner(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }

    /**
     * Firebase Remote Config ile güncelleme kontrolü
     */
    private fun checkForUpdates() {
        ForceUpdateChecker(this).check(object : ForceUpdateChecker.OnUpdateNeededListener {
            override fun onUpdateNeeded(updateUrl: String) {
                showUpdateDialog(updateUrl)
            }
        })
    }

    /**
     * Zorunlu güncelleme için kullanıcıya dialog gösterme
     */
    private fun showUpdateDialog(updateUrl: String) {
        AlertDialog.Builder(this)
            .setTitle("Yeni Güncelleme Mevcut")
            .setMessage("Uygulamanın yeni bir sürümü mevcut. Devam etmek için güncellemelisiniz.")
            .setCancelable(false) // Kullanıcı geri tuşuna basarak kapatamasın
            .setPositiveButton("Güncelle") { _, _ ->
                // Play Store sayfasına yönlendir
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)))
                    finish() // Uygulamayı kapat
                } catch (e: Exception) {
                    // Play Store uygulaması yoksa web tarayıcıdan aç
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)))
                    finish()
                }
            }
            .show()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val context = ThemeDataStore(newBase).applySavedLanguage(newBase)
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(newBase)
        }
    }

}

@Composable
fun AdmobBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                // Adaptive Banner boyutu ekran genişliğine göre ayarlanır
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context, AdSize.FULL_WIDTH
                )
                setAdSize(adSize)
                adUnitId = "ca-app-pub-3940256099942544/9214589741"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

// XML tabanlı reklam bileşeni 
@Composable
fun XmlAdBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            Log.d("ADS_DEBUG", "Creating XML AdView")
            
            // XML layout'u inflate et
            val view = LayoutInflater.from(context).inflate(R.layout.ad_banner, null) as RelativeLayout
            
            // AdView'a referans al
            val adView = view.findViewById<AdView>(R.id.adView)
            
            // Reklam listener'ını ayarla
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d("ADS_DEBUG", "✅ Ad loaded successfully!")
                    super.onAdLoaded()
                }
                
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("ADS_DEBUG", "❌ Ad failed to load: Error ${error.code} - ${error.message}")
                    super.onAdFailedToLoad(error)
                }
            }
            
            // Reklam yükle
            try {
                Log.d("ADS_DEBUG", "Requesting ad...")
                adView.loadAd(AdRequest.Builder().build())
            } catch (e: Exception) {
                Log.e("ADS_DEBUG", "Error loading ad: ${e.message}", e)
            }
            
            view
        }
    )
}