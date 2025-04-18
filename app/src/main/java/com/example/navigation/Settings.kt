package com.example.navigation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ColorPreference(private val context: Context) {
    private val APP_COLOR_KEY = longPreferencesKey("app_color")

    val appColor: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[APP_COLOR_KEY] ?: 0xFFFFC107 // Default color
        }

    suspend fun saveAppColor(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[APP_COLOR_KEY] = color
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(modifier: Modifier = Modifier) {
    // State variables for user settings
    var userName by remember { mutableStateOf("Kayiwa Rahim") }
    var userEmail by remember { mutableStateOf("kayiwa.rahim@students.mak.ac.ug") }
    var autoArmSecurityEnabled by remember { mutableStateOf(true) }
    var appNotificationsEnabled by remember { mutableStateOf(false) }

    // State for edit dialog
    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val colorPreference = remember { ColorPreference(context) }
    val selectedColorState by colorPreference.appColor.collectAsStateWithLifecycle(initialValue = 0xFFFFC107)
    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // User Settings Section
            SectionHeader(title = "User Settings")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEditUserDialog = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(24.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = userEmail,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            // App Settings Section
            SectionHeader(title = "App Settings")

            // App Color Setting
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showColorPicker = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "App Color:",
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(selectedColorState))
                )
            }

            // Auto Arm Security Setting
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Auto Arm Security Alarm",
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = autoArmSecurityEnabled,
                    onCheckedChange = { autoArmSecurityEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFC107),
                        checkedIconColor = Color(0xFFFFC107)
                    )
                )
            }

            // App Notifications Setting
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "App Notifications",
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = appNotificationsEnabled,
                    onCheckedChange = { appNotificationsEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFC107),
                        checkedIconColor = Color(0xFFFFC107)
                    )
                )
            }

            // Voice Section
            SectionHeader(title = "Voice")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {  }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Voice",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Voice Assistants",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            // App Permissions Section
            SectionHeader(title = "App Permissions")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {  }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Permissions",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Notifications & Permissions",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Edit User Dialog
    if (showEditUserDialog) {
        var tempName by remember { mutableStateOf(userName) }
        var tempEmail by remember { mutableStateOf(userEmail) }

        AlertDialog(
            onDismissRequest = { showEditUserDialog = false },
            title = { Text("Edit User") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        userName = tempName
                        userEmail = tempEmail
                        showEditUserDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditUserDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Color Picker Dialog
    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Select App Color") },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val colors = listOf(
                            0xFFFFC107L, // Amber
                            0xFF2196F3L, // Blue
                            0xFF4CAF50L, // Green
                            0xFFE91E63L, // Pink
                            0xFF9C27B0L  // Purple
                        )
                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        scope.launch {
                                            colorPreference.saveAppColor(color)
                                        }
                                        showColorPicker = false
                                    }
                                    .background(Color(color))
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showColorPicker = false }) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    content: @Composable () -> Unit,
    onClick: () -> Unit = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium
            )

            content()
        }
    }

@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
        onClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp)
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, CircleShape)
                    .then(
                        if (isSelected) {
                            Modifier.border(2.dp, Color.Black, CircleShape)
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
