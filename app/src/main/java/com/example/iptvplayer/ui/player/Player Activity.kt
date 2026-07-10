package com.example.iptvplayer.ui.player

import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.iptvplayer.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player) // سننشئه بعد قليل

        // جعل التطبيق يظهر بملء الشاشة (إخفاء شريط الحالة والأزرار السفلية)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()

        // ربط العناصر
        playerView = findViewById(R.id.player_view)
        btnBack = findViewById(R.id.btnBack)

        // جلب رابط القناة من الـ Intent
        val url = intent.getStringExtra("channel_url") ?: ""

        // تهيئة مشغل ExoPlayer
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        // تجهيز مصدر البث
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play() // بدء البث مباشرة

        // إضافة مستمع لإعادة المحاولة التلقائية إذا توقف البث (للحصول على تجربة سلسة)
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    // إذا انتهى البث (يحدث أحياناً في البث المباشر)، نعيد المحاولة
                    player.seekTo(0)
                    player.play()
                }
            }
        })

        // زر العودة إلى القائمة
        btnBack.setOnClickListener {
            player.release() // تحرير المشغل لتوفير الذاكرة
            finish()
        }
    }

    // عند تدمير النشاط، نحرر المشغل
    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}