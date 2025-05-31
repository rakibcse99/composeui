package com.rakib.composeui.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rakib.composeui.domain.model.Call
import com.rakib.composeui.domain.model.User
import com.rakib.composeui.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.callsUiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        CustomTopAppBar(
            title = "Calls",
            height = 72.dp,
            onSearchClick = {  },
            onMenuClick = {  }
        )

        // Calls Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Theme-aware background
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
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                uiState.calls.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No calls available",
                            color = Color(0xFF8696A0), // WhatsApp gray
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        item {
                            Text(
                                text = "Favourites",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            )
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = "Add favourite",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF00A884)
                                    )
                                },
                                leadingContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color(0xFF00A884), shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        item {
                            Text(
                                text = "Recent",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            )
                        }
                        items(uiState.calls) { call ->
                            CallItem(
                                call = call,
                                userName = uiState.users.find { it.id == call.userId }?.name ?: "Unknown"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CallItem(call: Call, userName: String) {
    val timestamp = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(call.timestamp))
    val callIcon = when {
        !call.isOutgoing && !call.isVideo -> Icons.Default.CallReceived // Incoming voice call
        !call.isOutgoing && call.isVideo -> Icons.Default.Videocam // Incoming video call
        call.isOutgoing && call.isVideo -> Icons.Default.Videocam // Outgoing video call
        call.isOutgoing && !call.isVideo -> Icons.Default.Call // Outgoing voice call
        else -> Icons.Default.CallMissed // Missed call
    }
    val callIconColor = when {
        call.isOutgoing -> Color(0xFF00A884) // WhatsApp green for outgoing
        !call.isOutgoing && call.isVideo -> Color(0xFF00A884) // Green for video calls
        else -> Color(0xFFE57373) // Red for missed/incoming voice calls
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
//        shape = RoundedCornerShape(16.dp), // WhatsApp squircle
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF8696A0), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8696A0) // WhatsApp gray
                )
            }
            Icon(
                imageVector = callIcon,
                contentDescription = null,
                tint = callIconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun CustomTopAppBar(
    title: String = "Calls",
    backgroundColor: Color = Color(0xFFF8F7F7),
    height: Dp = 72.dp, // You can change this to any height you want
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }

            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }
        }
    }
}
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun CallsScreenPreview() {
//    MaterialTheme {
//        CallsScreen(
//            viewModel = object : ChatViewModel() {
//                override val callsUiState: StateFlow<CallsUiState> = MutableStateFlow(
//                    CallsUiState(
//                        calls = listOf(
//                            Call(userId = 1, timestamp = System.currentTimeMillis(), isOutgoing = true, isVideo = false),
//                            Call(userId = 2, timestamp = System.currentTimeMillis() - 1000, isOutgoing = false, isVideo = true)
//                        ),
//                        users = listOf(User(id = 1, name = "Alice"), User(id = 2, name = "Bob"))
//                    )
//                )
//            }
//        )
//    }
//}