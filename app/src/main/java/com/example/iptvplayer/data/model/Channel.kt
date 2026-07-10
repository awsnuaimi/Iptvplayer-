package com.example.iptvplayer.data.model

data class Channel(
    val name: String,
    val url: String,
    val logo: String = "",
    val group: String = "عام"
)