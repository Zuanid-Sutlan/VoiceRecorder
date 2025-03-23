package com.example.voicerecorder.model

data class Recording(
    val filePath: String,
    val fileName: String,
    val duration: Long = 0 // Optional: Add duration if needed
)