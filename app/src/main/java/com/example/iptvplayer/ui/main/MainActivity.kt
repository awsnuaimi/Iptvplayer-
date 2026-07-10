package com.example.iptvplayer.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iptvplayer.R
import com.example.iptvplayer.data.local.PreferencesManager
import com.example.iptvplayer.data.model.Channel
import com.example.iptvplayer.ui.adapter.ChannelAdapter
import com.example.iptvplayer.ui.player.PlayerActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ChannelAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    private var allChannels = listOf<Channel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ربط العناصر الموجودة في الواجهة
        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        // تهيئة الـ ViewModel
        viewModel = MainViewModel()

        // تهيئة الـ Adapter (بدون بيانات حالياً)
        adapter = ChannelAdapter(emptyList()) { channel ->
            // عند الضغط على أي قناة، ننتقل لمشغل الفيديو
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("channel_url", channel.url)
            intent.putExtra("channel_name", channel.name)
            startActivity(intent)
        }

        // تجهيز الـ RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // تحميل الرابط المحفوظ من الإعدادات وبدء جلب القنوات
        val prefs = PreferencesManager(this)
        val url = prefs.getM3uUrl()
        if (url.isNotEmpty()) {
            viewModel.loadChannels(url)
        }

        // 1. مراقبة القنوات (لتحديث القائمة)
        lifecycleScope.launch {
            viewModel.channels.collect { list ->
                allChannels = list
                // عرض القائمة مع فلتر "الكل" بشكل افتراضي
                filterChannels("الكل")
            }
        }

        // 2. مراقبة المجموعات (لإنشاء التبويبات)
        lifecycleScope.launch {
            viewModel.groups.collect { groups ->
                setupTabs(groups)
            }
        }

        // 3. مراقبة حالة التحميل (إظهار أو إخفاء الـ ProgressBar)
        lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                progressBar.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
            }
        }

        // 4. مراقبة الأخطاء (إظهار رسالة الخطأ)
        lifecycleScope.launch {
            viewModel.error.collect { errorMsg ->
                if (errorMsg != null) {
                    tvError.text = errorMsg
                    tvError.visibility = android.view.View.VISIBLE
                } else {
                    tvError.visibility = android.view.View.GONE
                }
            }
        }
    }

    // دالة إعداد التبويبات (حسب المجموعات المستخرجة)
    private fun setupTabs(groups: List<String>) {
        tabLayout.removeAllTabs()
        groups.forEach { group ->
            tabLayout.addTab(tabLayout.newTab().setText(group))
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val groupName = tab?.text.toString()
                filterChannels(groupName)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // دالة فلتر القنوات حسب التبويب المختار
    private fun filterChannels(group: String) {
        val filtered = if (group == "الكل") {
            allChannels
        } else {
            allChannels.filter { it.group == group }
        }
        adapter.updateList(filtered)
    }
}