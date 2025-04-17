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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(modifier: Modifier = Modifier) {
    // State variables for user settings
    var userName by remember { mutableStateOf("Kayiwa Rahim") }
    var userEmail by remember { mutableStateOf("kayiwa.rahim@students.mak.ac.ug") }
    var selectedColor by remember { mutableStateOf(Color.Yellow) }
    var autoArmSecurityEnabled by remember { mutableStateOf(true) }
    var appNotificationsEnabled by remember { mutableStateOf(false) }

    // State for edit dialog
    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val colorPreference = remember { ColorPreference(context) }
    val selectedColorState by colorPreference.appColor.collectAsStateWithLifecycle(initialValue = 0xFFFFC107)
    val scope = rememberCoroutineScope()

    // Available app colors
    val appColors = listOf(
        0xFFFFC107L, // Amber
        0xFF2196F3L, // Blue
        0xFF4CAF50L, // Green
        0xFFE91E63L, // Pink
        0xFF9C27B0L  // Purple
    )

    Scaffold(
        
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFFDFCFC))
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Yellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = Color.White
                    )
                }

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

                IconButton(onClick = { showEditUserDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit User",
                        tint = Color.Gray
                    )
                }
            }

            Divider()

            // App Color Setting
            SettingItem(
                title = "App Color:",
                content = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(selectedColorState))
                            .padding(4.dp)
                    )
                },
                onClick = { showColorPicker = true }
            )

            // Auto Arm Security Alarm Setting
            SettingItem(
                title = "Auto Arm Security Alarm",
                content = {
                    Switch(
                        checked = autoArmSecurityEnabled,
                        onCheckedChange = { autoArmSecurityEnabled = it },
                        thumbContent = if (autoArmSecurityEnabled) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.Yellow,
                            checkedIconColor = Color.Yellow
                        )
                    )
                }
            )

            // App Notifications Setting
            SettingItem(
                title = "App Notifications",
                content = {
                    Switch(
                        checked = appNotificationsEnabled,
                        onCheckedChange = { appNotificationsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.Yellow,
                            checkedIconColor = Color.Yellow
                        )
                    )
                }
            )

            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Voice",
                    tint = Color.Yellow
                )

                Text(
                    text = "Voice Assistants",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray
                )
            }

            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Permissions",
                    tint = Color.Gray
                )

                Text(
                    text = "Notifications & Permissions",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray
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
                        appColors.take(3).forEach { color ->
                            ColorOption(
                                color = Color(color),
                                isSelected = color == selectedColorState,
                                onClick = {
                                    scope.launch {
                                        colorPreference.saveAppColor(color)
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        appColors.takeLast(3).forEach { color ->
                            ColorOption(
                                color = Color(color),
                                isSelected = color == selectedColorState,
                                onClick = {
                                    scope.launch {
                                        colorPreference.saveAppColor(color)
                                    }
                                }
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
