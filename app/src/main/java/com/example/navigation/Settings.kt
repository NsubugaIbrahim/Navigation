package com.example.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navigation.SettingsEntity
import com.example.navigation.viewmodel.SettingsViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
    val viewModel: SettingsViewModel = viewModel()
    val settings by viewModel.settings.collectAsStateWithLifecycle(initialValue = SettingsEntity())

    var showEditUserDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(top = 116.dp)
    ) {
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

        SectionHeader(title = "App Settings")

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
                    .background(Color(settings.appColor))
            )
        }

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
                onCheckedChange = { newValue ->
                    if (newValue != settings.autoArm) {
                        viewModel.updateAutoArm(newValue)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(settings.appColor),
                    checkedIconColor = Color(settings.appColor)
                )
            )
        }

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
                onCheckedChange = { newValue ->
                    if (newValue != settings.notifications) {
                        viewModel.updateAppNotifications(newValue)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(settings.appColor),
                    checkedIconColor = Color(settings.appColor)
                )
            )
        }

        SectionHeader(title = "Voice")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
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
                        viewModel.updateSettings(
                            settings.copy(
                                name = tempName,
                                email = tempEmail
                            )
                        )
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
                            0xFFFFC107L,
                            0xFF2196F3L,
                            0xFF4CAF50L,
                            0xFFE91E63L,
                            0xFF9C27B0L
                        )
                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        if (color != settings.appColor) {
                                            viewModel.updateAppColor(color)
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
