package com.example.iptvplayer.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iptvplayer.data.local.PreferencesManager
import com.example.iptvplayer.ui.main.MainActivity
import com.example.iptvplayer.ui.settings.SettingsActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // سننشئ هذا الملف بعد قليل

        // ننتظر 1.5 ثانية ثم ننتقل للشاشة المناسبة
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            val prefs = PreferencesManager(this@SplashActivity)
            if (prefs.getM3uUrl().isNotEmpty()) {
                // يوجد رابط -> اذهب للرئيسية
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // أول مرة -> اذهب لإدخال الرابط
                startActivity(Intent(this@SplashActivity, SettingsActivity::class.java))
            }
            finish()
        }
    }
}