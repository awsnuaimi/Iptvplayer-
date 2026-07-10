package com.example.iptvplayer.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iptvplayer.data.model.Channel
import com.example.iptvplayer.data.repository.ChannelRepository
import com.example.iptvplayer.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channels.asStateFlow()

    private val _groups = MutableStateFlow<List<String>>(emptyList())
    val groups = _groups.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val repository = ChannelRepository()

    fun loadChannels(url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.fetchChannels(url).collect { result ->
                when (result) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _channels.value = result.data
                        // استخراج المجموعات الفريدة (لإنشاء التبويبات)
                        val groupList = result.data.map { it.group }.distinct().sorted()
                        _groups.value = listOf("الكل") + groupList
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _error.value = result.message
                        _isLoading.value = false
                    }
                }
            }
        }
    }
}