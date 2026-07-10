package com.example.iptvplayer.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iptvplayer.R
import com.example.iptvplayer.data.local.PreferencesManager
import com.example.iptvplayer.ui.main.MainActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var etUrl: EditText
    private lateinit var btnSave: Button
    private lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings) // سننشئ هذا الملف بعد قليل

        etUrl = findViewById(R.id.et_m3u_url)
        btnSave = findViewById(R.id.btn_save_url)
        prefs = PreferencesManager(this)

        // إذا كان هناك رابط محفوظ مسبقاً (في حالة تعديل الإعدادات)، نضعه في الحقل
        etUrl.setText(prefs.getM3uUrl())

        btnSave.setOnClickListener {
            val url = etUrl.text.toString().trim()
            if (url.isEmpty()) {
                Toast.makeText(this, "الرجاء إدخال رابط صحيح", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // التحقق البسيط من صحة الرابط
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                Toast.makeText(this, "الرابط يجب أن يبدأ بـ http:// أو https://", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // حفظ الرابط والانتقال إلى الشاشة الرئيسية
            prefs.saveM3uUrl(url)
            Toast.makeText(this, "تم حفظ الرابط بنجاح", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}