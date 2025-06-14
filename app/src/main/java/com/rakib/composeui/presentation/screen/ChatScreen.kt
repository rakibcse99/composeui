package com.rakib.composeui.presentation.screen

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PictureAsPdf
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rakib.composeui.R
import com.rakib.composeui.domain.model.Message
import com.rakib.composeui.domain.model.User
import com.rakib.composeui.presentation.viewmodel.ChatViewModel
import com.rakib.composeui.presentation.viewmodel.ChatViewModel.ChatUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: Int,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
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
            val fileName = getFileNameFromUri(context, uri) ?: "Document"
            val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
            val message = Message(
                userId = userId,
                senderId = userId.toString(),
                text = fileName,
                fileUri = uri.toString(),
                timestamp = System.currentTimeMillis(),
                isSent = true
            )
            viewModel.sendMessage(message)
            Toast.makeText(context, "File attached: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.messages) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(Color(0xFFF3F6F6))
                .padding(horizontal = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                uiState.user?.let {
                    Text(
                        text = it.name,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge
                    )
                } ?: Text("Chat", color = Color.Black)

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { /* Video call */ }) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = { /* Call */ }) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = { /* Menu */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.Black
                    )
                }
            }
        }

        // Chat Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_chat),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
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
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                uiState.messages.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No messages yet",
                            color = Color(0xFF8696A0),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.messages) { message ->
                            MessageItem(message = message)
                        }
                    }
                }
            }
        }

        // Input Row
        var messageText by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Emoji */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEmotions,
                    contentDescription = "Emoji",
                    tint = Color(0xFF00A884),
                    modifier = Modifier.size(24.dp)
                )
            }
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(24.dp)
                    ),
                placeholder = {
                    Text("Message", color = Color(0xFF8696A0))
                },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF00A884),
                    focusedPlaceholderColor = Color(0xFF8696A0),
                    unfocusedPlaceholderColor = Color(0xFF8696A0),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )

            IconButton(
                onClick = { filePicker.launch("application/pdf,image/*") }, // Restrict to PDFs and images
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach",
                    tint = Color(0xFF00A884),
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        val message = Message(
                            userId = userId,
                            senderId = userId.toString(),
                            text = messageText,
                            fileUri = null,
                            timestamp = System.currentTimeMillis(),
                            isSent = true
                        )
                        viewModel.sendMessage(message)
                        messageText = ""
                        viewModel.sendMessage(
                            Message(
                                userId = userId,
                                senderId = (userId + 1).toString(),
                                text = "Reply: ${message.text.take(10)}...",
                                fileUri = null,
                                timestamp = System.currentTimeMillis() + 1000,
                                isSent = false
                            )
                        )
                    }
                },
                enabled = messageText.isNotBlank(),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (messageText.isNotBlank()) Color(0xFF00A884) else Color(0xFF8696A0),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "MessageFade"
    )
    val bubbleColor = if (message.isSent) Color(0xFFDCF8C6) else Color.White
    val alignment = if (message.isSent) Arrangement.End else Arrangement.Start
    val cornerRadius = 16.dp
    val context = LocalContext.current

    // Determine file type based on MIME type or URI
    val isImage = message.fileUri?.let { uriString ->
        val uri = Uri.parse(uriString)
        val mimeType = context.contentResolver.getType(uri) ?: ""
        mimeType.startsWith("image/")
    } ?: false

    val isPdf = message.fileUri?.let { uriString ->
        val uri = Uri.parse(uriString)
        val mimeType = context.contentResolver.getType(uri) ?: ""
        mimeType == "application/pdf"
    } ?: false

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .alpha(alpha),
        horizontalArrangement = alignment
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = RoundedCornerShape(
                topStart = if (message.isSent) cornerRadius else 4.dp,
                topEnd = if (message.isSent) 4.dp else cornerRadius,
                bottomStart = cornerRadius,
                bottomEnd = cornerRadius
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.clickable {
                message.fileUri?.let { uriString ->
                    try {
                        val uri = Uri.parse(uriString)
                        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, mimeType)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(context, Intent.createChooser(intent, "Open with"), null)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Unable to open file: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .widthIn(max = 300.dp)
            ) {
                when {
                    isImage -> {
                        // Display image preview
                        Image(
                            painter = rememberAsyncImagePainter(model = message.fileUri),
                            contentDescription = "Image Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp) // WhatsApp-style image size
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    isPdf -> {
                        // Display PDF
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "PDF Icon",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = message.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    else -> {
                        // Display text message
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF8696A0),
                        modifier = Modifier.padding(end = if (message.isSent) 4.dp else 0.dp)
                    )
                    if (message.isSent) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read Status",
                            tint = Color(0xFF53BDEB),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// Helper function to get file name from URI
fun getFileNameFromUri(context: android.content.Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) it.getString(displayNameIndex) else null
        } else {
            null
        }
    }
}