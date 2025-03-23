package com.example.voicerecorder

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object KoinModule {

    val appModule = module {
        single { AudioRecorder() }
        viewModel { RecordingViewModel(get()) }
    }

}