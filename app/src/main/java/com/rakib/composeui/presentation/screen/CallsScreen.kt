package com.rakib.composeui.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rakib.composeui.domain.model.Call

import com.rakib.composeui.presentation.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val calls by viewModel.calls.collectAsState(initial = emptyList())
    val users by viewModel.users.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Calls") },
            actions = {
                IconButton(onClick = { /* Handle new call */ }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Call,
                        contentDescription = "New Call"
                    )
                }
            }
        )
        when {
            calls.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn {
                    items(calls) { call ->
                        val user = users.find { it.id == call.userId }
                        CallItem(call = call, userName = user?.name ?: "Unknown")
                    }
                }
            }
        }
    }
}

@Composable
fun CallItem(call: Call, userName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (call.isVideo) androidx.compose.material.icons.Icons.Default.Videocam else androidx.compose.material.icons.Icons.Default.Call,
                contentDescription = if (call.isVideo) "Video Call" else "Voice Call",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (call.isOutgoing) "Outgoing" else "Incoming",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(call.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}