package com.example.voicerecorder

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicerecorder.model.Recording
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordingViewModel(private val audioRecorder: AudioRecorder) : ViewModel() {

    private val _recordingState = MutableStateFlow(false)
    val recordingState: StateFlow<Boolean> = _recordingState

    private val _recordings = MutableStateFlow<List<Recording>>(emptyList())
    val recordings: StateFlow<List<Recording>> = _recordings

    private val storageDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
        "Recordings"
    ).apply { mkdirs() }

    init {
        loadRecordings()
    }

    fun startRecording() {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val fileName = "Recording_${dateFormat.format(Date())}.aac"
            val outputFile = File(storageDir, fileName)
            audioRecorder.startRecording(outputFile)
            _recordingState.value = true
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            audioRecorder.stopRecording()
            _recordingState.value = false
            loadRecordings() // Refresh the list after recording
        }
    }

    private fun loadRecordings() {
        val files = storageDir.listFiles()?.filter { it.isFile && it.name.endsWith(".aac") }
        _recordings.value = files?.map {
            Recording(
                filePath = it.absolutePath,
                fileName = it.name
            )
        } ?: emptyList()
    }

    fun deleteRecording(recording: Recording) {
        val file = File(recording.filePath)
        if (file.exists()) {
            file.delete()
            loadRecordings() // Refresh the list after deletion
        }
    }
}