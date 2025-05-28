package com.rakib.composeui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rakib.composeui.presentation.screen.CallsScreen
import com.rakib.composeui.presentation.screen.ChatScreen
import com.rakib.composeui.presentation.screen.ChatsScreen
import com.rakib.composeui.presentation.screen.StatusScreen
import com.rakib.composeui.ui.theme.WhatsAppCloneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val imagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false
        val userSelectedGranted = permissions[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] ?: false
        if (!imagesGranted && !userSelectedGranted) {
            Toast.makeText(this, "Media access denied. File picking may be limited.", Toast.LENGTH_SHORT).show()
        } else if (userSelectedGranted && !imagesGranted) {
            Toast.makeText(this, "Limited media access granted. Select files manually.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkStoragePermissions()
        setContent {
            WhatsAppCloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    private fun checkStoragePermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Chats : Screen("chats", "Chats", Icons.Default.Chat)
    data object Updates : Screen("updates", "Updates", Icons.Default.Update)
    data object Communities : Screen("communities", "Communities", Icons.Default.Groups)
    data object Calls : Screen("calls", "Calls", Icons.Default.Call)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navItems = listOf(Screen.Chats, Screen.Updates, Screen.Communities, Screen.Calls)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WhatsApp", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00A884)
                ) , actions = {
                    IconButton(onClick = { /* Camera */ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Camera", tint = Color.White)
                    }
                    IconButton(onClick = { /* Search */ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF00A884),
                            selectedTextColor = Color(0xFF00A884),
                            indicatorColor = Color(0xFFE0F2F1)
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = currentRoute in listOf("chats", "calls")) {
                FloatingActionButton(
                    onClick = { /* New chat or call */ },
                    containerColor = Color(0xFF00A884),
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Chats.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Chats.route) {
                ChatsScreen(
                    onUserClick = { userId ->
                        navController.navigate("chat/$userId")
                    }
                )
            }
            composable(Screen.Updates.route) {
                StatusScreen()
            }
            composable(Screen.Communities.route) {
                Text("Communities Screen")
            }
            composable(Screen.Calls.route) {
                CallsScreen()
            }
            composable("chat/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
                ChatScreen(userId = userId, onBackClick = { navController.popBackStack() })
            }
        }
    }
}