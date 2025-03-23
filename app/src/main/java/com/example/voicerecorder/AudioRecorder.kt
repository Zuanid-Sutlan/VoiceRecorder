package com.example.voicerecorder

import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class AudioRecorder {

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var isRecording = false

    fun startRecording(outputFile: File) {
        this.outputFile = outputFile
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            try {
                prepare()
                start()
                isRecording = true
            } catch (e: IOException) {
                e.printStackTrace()
                releaseMediaRecorder()
            }
        }
    }

    fun stopRecording() {
        if (isRecording) {
            mediaRecorder?.apply {
                try {
                    stop()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } finally {
                    releaseMediaRecorder()
                }
            }
        }
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.release()
        mediaRecorder = null
        isRecording = false
    }
}