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
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences(private val context: Context) {
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val AUTO_ARM_KEY = booleanPreferencesKey("auto_arm")
    private val APP_NOTIFICATIONS_KEY = booleanPreferencesKey("app_notifications")

    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY] ?: "Kayiwa Rahim"
        }

    val userEmail: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL_KEY] ?: "kayiwa.rahim@students.mak.ac.ug"
        }

    val autoArm: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTO_ARM_KEY] ?: true
        }

    val appNotifications: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[APP_NOTIFICATIONS_KEY] ?: false
        }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    suspend fun saveAutoArm(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_ARM_KEY] = enabled
        }
    }

    suspend fun saveAppNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APP_NOTIFICATIONS_KEY] = enabled
        }
    }

}

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
    val context = LocalContext.current
    val database = remember { SettingsDatabaseHelper(context) }
    var settings by remember { mutableStateOf(database.getSettings()) }
    var selectedColor by remember { mutableStateOf(Color.Yellow) }
    val colorPreference = remember { ColorPreference(context) }
    val selectedColorState by colorPreference.appColor.collectAsStateWithLifecycle(initialValue = 0xFFFFC107)
    val scope = rememberCoroutineScope()


    // State for dialogs
    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    // Available app colors
    val appColors = listOf(
        0xFFFFC107L, // Amber
        0xFF2196F3L, // Blue
        0xFF4CAF50L, // Green
        0xFFE91E63L, // Pink
        0xFF9C27B0L  // Purple
    )

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
                        text = settings.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = settings.email,
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
                    checked = settings.autoArm,
                    onCheckedChange = { 
                        settings = settings.copy(autoArm = it)
                        database.updateSettings(settings)
                    },
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
                    checked = settings.notifications,
                    onCheckedChange = { 
                        settings = settings.copy(notifications = it)
                        database.updateSettings(settings)
                    },
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
        var tempName by remember { mutableStateOf(settings.name) }
        var tempEmail by remember { mutableStateOf(settings.email) }

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
                        settings = settings.copy(
                            name = tempName,
                            email = tempEmail
                        )
                        database.updateSettings(settings)
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
}




