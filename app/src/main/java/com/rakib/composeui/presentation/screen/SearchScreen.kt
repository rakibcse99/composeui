package com.rakib.composeui.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rakib.composeui.domain.model.User
import com.rakib.composeui.presentation.viewmodel.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onUserClick: (Int) -> Unit, onBackClick: () -> Unit, viewModel: ChatViewModel = hiltViewModel()) {
    val uiState by viewModel.chatsUiState.collectAsState()
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text("Search Users", color = Color.Black)
            },
            modifier = Modifier
                .fillMaxWidth().background(Color.Gray)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                }
            },
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
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            uiState.users.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No users available",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                val filteredUsers = uiState.users.filter {
                    it.name.contains(searchText.text, ignoreCase = true)
                }
                LazyColumn {
                    items(filteredUsers) { user ->
                        UserItem(user = user, onClick = { onUserClick(user.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    val lastMessage = "Last message..."
    val timestamp = "Today"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
            Text(
                text = timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}