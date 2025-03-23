package com.example.voicerecorder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.voicerecorder.model.Recording

@Composable
fun RecordingList(
    recordings: List<Recording>,
    onPlayClick: (Recording) -> Unit,
    onDeleteClick: (Recording) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(recordings) { recording ->
            RecordingItem(
                recording = recording,
                onPlayClick = onPlayClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun RecordingItem(
    recording: Recording,
    onPlayClick: (Recording) -> Unit,
    onDeleteClick: (Recording) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(4f),
                text = recording.fileName,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = { onPlayClick(recording) }
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = { onDeleteClick(recording) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}