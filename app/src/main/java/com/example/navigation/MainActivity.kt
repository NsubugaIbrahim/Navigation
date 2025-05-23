package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.theme.NavigationTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.navigation.viewmodel.SettingsViewModel
import com.example.navigation.Settings as SettingsScreen
import androidx.compose.ui.platform.LocalLayoutDirection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { MyTopAppBar(navController) },
                    bottomBar = { MyBottomAppBar(navController) }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "favorites",
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        composable("favorites") {
                            Favorites()
                        }
                        composable("things") {
                            Things()
                        }
                        composable("routines") {
                            Routines()
                        }
                        composable("ideas"){
                            Ideas()
                        }
                        composable("settings") {
                            Box(modifier = Modifier.padding(top = 0.dp)) {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()
    val settings by viewModel.settings.collectAsStateWithLifecycle(initialValue = SettingsEntity())
    val currentColor = remember(settings.appColor) { Color(settings.appColor) }

    TopAppBar(
        title = {
            Text(
                text = "My Smart Home",
                style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute == "favorites") {
                IconButton(onClick = { /* TODO: Add action */ }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Add Favorite", tint = Color.White)
                }
            } else if (currentRoute == "things") {
                IconButton(onClick = { /* TODO: Action */ }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Things", tint = Color.White)
                }
                IconButton(onClick = { /* TODO: Action */ }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
            else if ( currentRoute == "routines"){
                IconButton(onClick = { /* TODO: Action */ }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = currentColor)
    )
}

@Composable
fun MyBottomAppBar(navController: NavController) {
    BottomAppBar(contentColor = Color.Gray) {
        val viewModel: SettingsViewModel = viewModel()
        val settings by viewModel.settings.collectAsStateWithLifecycle(initialValue = SettingsEntity())
        val currentColor = remember(settings.appColor) { Color(settings.appColor) }


        val items = listOf(
            BottomNavItem("favorites", Icons.Default.Star, "Favorites"),
            BottomNavItem("things", Icons.Default.List, "Things"),
            BottomNavItem("routines", Icons.Default.Refresh, "Routines"),
            BottomNavItem("ideas", Icons.Default.Notifications, "Ideas"),
            BottomNavItem("settings", Icons.Default.Settings, "Settings")
        )

        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            IconButton(
                onClick = { navController.navigate(item.route) },
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) currentColor else Color.Gray

                    )
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)
