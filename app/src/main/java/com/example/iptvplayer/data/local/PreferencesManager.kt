package com.example.iptvplayer.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("iptv_prefs", Context.MODE_PRIVATE)

    fun saveM3uUrl(url: String) {
        prefs.edit().putString("m3u_url", url).apply()
    }

    fun getM3uUrl(): String {
        return prefs.getString("m3u_url", "") ?: ""
    }
}