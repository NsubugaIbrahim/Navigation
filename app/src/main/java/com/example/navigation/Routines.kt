package com.example.navigation

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navigation.viewmodel.RoutineViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun Routines() {
    val viewModel: RoutineViewModel = viewModel()
    val routines by viewModel.allRoutines.collectAsStateWithLifecycle(initialValue = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var routineToEdit by remember { mutableStateOf<RoutineEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (routines.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text("No Routines!", fontWeight = FontWeight.Bold ,fontSize = 25.sp)
                Text("Click the '+' button below to get started" , fontSize = 20.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(routines) { routine ->
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
                                    routineToEdit = routine
                                    showEditDialog = true
                                }) {
                                    Text("Edit")
                                }
                                OutlinedButton(
                                    onClick = {
                                        viewModel.deleteRoutine(routine.id)
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Routine",
                modifier = Modifier
                    .size(80.dp)
                    .clickable { showDialog = true },
                tint = Color(0xFF03A9F4)
            )
        }
    }

    if (showDialog) {
        AddRoutineDialog(
            onAdd = { name, time, recurrence ->
                val routine = RoutineEntity(
                    name = name,
                    time = time,
                    recurrence = recurrence
                )
                viewModel.insertRoutine(routine)
                showDialog = false
            },
            onCancel = { showDialog = false }
        )
    }

    if (showEditDialog && routineToEdit != null) {
        AddRoutineDialog(
            onAdd = { name, time, recurrence ->
                val updatedRoutine = routineToEdit!!.copy(
                    name = name,
                    time = time,
                    recurrence = recurrence
                )
                viewModel.updateRoutine(updatedRoutine)
                showEditDialog = false
                routineToEdit = null
            },
            onCancel = {
                showEditDialog = false
                routineToEdit = null
            },
            initialName = routineToEdit!!.name,
            initialTime = routineToEdit!!.time,
            initialRecurrence = routineToEdit!!.recurrence
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
    var time by remember {
        mutableStateOf(
            try {
                LocalTime.parse(initialTime, DateTimeFormatter.ofPattern("hh:mm a"))
            } catch (e: Exception) {
                LocalTime.now()
            }
        )
    }
    var showTimePicker by remember { mutableStateOf(false) }

    val recurrenceOptions = listOf("Daily", "Weekdays", "Weekends", "Weekly", "Monthly" ,"Yearly")
    var selectedRecurrence by remember {
        mutableStateOf(if (initialRecurrence in recurrenceOptions) initialRecurrence else recurrenceOptions[0])
    }
    var expanded by remember { mutableStateOf(false) }

    if (showTimePicker) {
        LaunchedEffect(Unit) {
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    time = LocalTime.of(hour, minute)
                    showTimePicker = false
                },
                time.hour,
                time.minute,
                true
            ).show()
        }
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
                OutlinedTextField(
                    value = time.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    onValueChange = {},
                    label = { Text("Pick Time") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true }
                )
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
                        time.format(DateTimeFormatter.ofPattern("hh:mm a")),
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
