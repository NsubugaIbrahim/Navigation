package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.theme.NavigationTheme

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
                        modifier = Modifier.padding(paddingValues)) {
                        composable("favorites") {
                            Favorites()
                        }

                        composable("things") {
                            Things()
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
        actions = { // Use actions to place icons on the right
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            if (currentRoute == "favorites") {
                IconButton(onClick = { /* Add action if needed */ }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Add Favorite", tint = Color.White)
                }
            } else if (currentRoute == "things") {
                IconButton(onClick = { /* Action */ }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Things", tint = Color.White)
                }
                IconButton(onClick = { /* Action */ }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFC107))
    )
}

@Composable
fun MyBottomAppBar(navController: NavController) {
    BottomAppBar(contentColor = Color.Gray) {
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {  // ✅ Align content vertically
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) Color(0xFFFFC107) else Color.Gray
                    )
                    Text(
                        text = item.label,
                        fontSize = 12.sp,  // ✅ Reduce font size to fit properly
                        color = Color.Black
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)


