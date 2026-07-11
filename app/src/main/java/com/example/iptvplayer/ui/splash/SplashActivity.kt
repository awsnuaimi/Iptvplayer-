package com.example.iptvplayer.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iptvplayer.R
import com.example.iptvplayer.data.local.PreferencesManager
import com.example.iptvplayer.ui.main.MainActivity
import com.example.iptvplayer.ui.settings.SettingsActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            val prefs = PreferencesManager(this@SplashActivity)
            if (prefs.getM3uUrl().isNotEmpty()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, SettingsActivity::class.java))
            }
            finish()
        }
    }
}
