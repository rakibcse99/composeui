package com.rakib.composeui.presentation.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.presentation.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(userId: Int, onBackClick: () -> Unit, viewModel: ChatViewModel = hiltViewModel()) {
    val uiState by viewModel.chatUiState.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(userId) {
        viewModel.loadMessagesForUser(userId)
    }

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val message = Message(
                userId = userId,
                senderId = userId.toString(),
                text = "File: $it",
                timestamp = System.currentTimeMillis(),
                isSent = true
            )
            viewModel.sendMessage(message)
            Toast.makeText(context, "File attached", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.messages) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        uiState.user?.let {
            TopAppBar(
                title = { Text(it.name, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00A884)
                ), navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Video call */ }) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Call */ }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            // background missing
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00A884))
                    }
                }

                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                uiState.messages.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No messages yet",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        items(uiState.messages) { message ->
                            MessageItem(message = message)
                        }
                    }
                }
            }
        }
        var messageText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Emoji */ }) {
                Icon(imageVector = Icons.Default.EmojiEmotions, contentDescription = "Emoji")
            }
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = MaterialTheme.shapes.medium),
                label = { Text("Message") },
                shape = MaterialTheme.shapes.medium,

                //    change color
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            IconButton(onClick = { filePicker.launch("*/*") }) {
                Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Attach")
            }
            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        val message = Message(
                            userId = userId,
                            senderId = userId.toString(),
                            text = messageText,
                            timestamp = System.currentTimeMillis(),
                            isSent = true
                        )
                        viewModel.sendMessage(message)
                        messageText = ""
                        viewModel.sendMessage(
                            Message(
                                userId = userId,
                                senderId = (userId + 1).toString(),
                                text = "Reply: ${messageText.take(10)}...",
                                timestamp = System.currentTimeMillis() + 1000,
                                isSent = false
                            )
                        )
                    }
                },
                enabled = messageText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (messageText.isNotBlank()) Color(0xFF00A884) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500),
        label = "MessageFade"
    )
    val bubbleColor = if (message.isSent) Color(0xFFDCF8C6) else Color.White
    val alignment = if (message.isSent) Arrangement.End else Arrangement.Start
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .alpha(alpha),
        horizontalArrangement = alignment
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).format(Date(message.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.End
                )
                if (message.isSent) {
                    Text(
                        text = "✓✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Blue,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}