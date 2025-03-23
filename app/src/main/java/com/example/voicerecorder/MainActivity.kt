package com.example.voicerecorder

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicerecorder.ui.theme.VoiceRecorderTheme
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel: RecordingViewModel = koinViewModel()

            val context = LocalContext.current
            val recordingState by viewModel.recordingState.collectAsStateWithLifecycle()
            val recordings by viewModel.recordings.collectAsStateWithLifecycle()

            // Request permissions
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions[android.Manifest.permission.RECORD_AUDIO] == true &&
                    permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                ) {
                    // Permissions granted
                }
            }

            LaunchedEffect(Unit) {
                permissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }

            VoiceRecorderTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* No-op */ },
                            modifier = Modifier.padding(16.dp),
                            containerColor = if (recordingState) Color.Red else MaterialTheme.colorScheme.primary,
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Record"
                                )
                            },
                            interactionSource = remember { MutableInteractionSource() }
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect {
                                            when (it) {
                                                is PressInteraction.Press -> viewModel.startRecording()
                                                is PressInteraction.Release -> viewModel.stopRecording()
                                            }
                                        }
                                    }
                                }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        RecordingList(
                            recordings = recordings,
                            onPlayClick = { recording ->
                                playRecording(context, recording.filePath)
                            },
                            onDeleteClick = { recording ->
                                viewModel.deleteRecording(recording)
                            }
                        )
                    }
                }
            }
        }
    }
    private fun playRecording(context: Context, filePath: String) {
        val mediaPlayer = MediaPlayer().apply {
            setDataSource(context, filePath.toUri())
            prepare()
            start()
        }
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoiceRecorderTheme {
        Greeting("Android")
    }
}