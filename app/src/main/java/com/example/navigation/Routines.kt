package com.example.navigation

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource

data class Routine(val name: String, val time: String, val recurrence: String)

@Composable
@Preview
fun Routines() {
    val context = LocalContext.current
    val dbHelper = remember { RoutineDatabaseHelper(context) }
    val routines = remember { mutableStateListOf<Routine>() }
    val pickedTime = remember { mutableStateOf(LocalTime.now()) }
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var routineToEditIndex by remember { mutableStateOf(-1) }

    // Load routines from database when composable is first created
    LaunchedEffect(Unit) {
        routines.clear()
        routines.addAll(dbHelper.getAllRoutines())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Routine")
            }
        }
    ) { paddingValues ->
        if (routines.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("No Routines!", fontWeight = FontWeight.Bold)
                Text("Click the '+' button below to get started")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(12.dp)
            ) {
                items(routines.size) { index ->
                    val routine = routines[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                        ) {
                            Text("Task Name: ${routine.name}", fontWeight = FontWeight.Bold)
                            Text("Timing: ${routine.time}")
                            Text("Recurrence: ${routine.recurrence}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(onClick = {
                                    routineToEditIndex = index
                                    showEditDialog = true
                                }) {
                                    Text("Edit")
                                }
                                OutlinedButton(
                                    onClick = {
                                        dbHelper.deleteRoutine(index + 1)
                                        routines.removeAt(index)
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddRoutineDialog(
            onAdd = { name, time, recurrence ->
                val routine = Routine(name, time, recurrence)
                dbHelper.addRoutine(routine)
                routines.add(routine)
                showDialog = false
            },
            onCancel = { showDialog = false }
        )
    }

    if (showEditDialog) {
        val routineToEdit = routines[routineToEditIndex]
        AddRoutineDialog(
            onAdd = { name, time, recurrence ->
                val updatedRoutine = Routine(name, time, recurrence)
                dbHelper.updateRoutine(routineToEditIndex + 1, updatedRoutine)
                routines[routineToEditIndex] = updatedRoutine
                showEditDialog = false
            },
            onCancel = { showEditDialog = false },
            initialName = routineToEdit.name,
            initialTime = routineToEdit.time,
            initialRecurrence = routineToEdit.recurrence
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineDialog(
    onAdd: (String, String, String) -> Unit,
    onCancel: () -> Unit,
    initialName: String = "",
    initialTime: String = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")),
    initialRecurrence: String = "Daily"
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(initialName) }
    var selectedTime by remember { mutableStateOf(initialTime) }

    val recurrenceOptions = listOf("Daily", "Weekdays", "Weekends", "Weekly", "Monthly")
    var selectedRecurrence by remember {
        mutableStateOf(if (initialRecurrence in recurrenceOptions) initialRecurrence else recurrenceOptions[0])
    }
    var expanded by remember { mutableStateOf(false) }

    // Function to show the time picker
    fun showTimePicker() {
        val currentTime = LocalTime.now()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newTime = LocalTime.of(hour, minute)
                selectedTime = newTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            },
            currentTime.hour,
            currentTime.minute,
            true
        ).show()
    }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(if (initialName.isEmpty()) "Add New Routine" else "Edit Routine") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Routine Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Time picker field - make the entire row clickable
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { showTimePicker() })
                ) {
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = {},
                        label = { Text("Pick Time") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedRecurrence,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Recurrence") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        recurrenceOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedRecurrence = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) {
                    onAdd(
                        name,
                        selectedTime,
                        selectedRecurrence
                    )
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
