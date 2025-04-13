package com.example.navigation

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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

@Composable
@Preview
fun Routines() {
    val context = LocalContext.current

    // Routine data model
    data class Routine(val name: String, val time: String, val recurrence: String)

    val pickedTime = remember { mutableStateOf(LocalTime.now()) }
    val routines = remember { mutableStateListOf<Routine>() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Add a new routine with dummy data for simplicity
                    routines.add(
                        Routine(
                            name = "Secure Lights On",
                            time = pickedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a")),
                            recurrence = "Weekdays"
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Add Routine")
            }
        }
    ) { paddingValues ->
        if (routines.isEmpty()) {
            // Empty State
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
            // Populated List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(12.dp)
            ) {
                items(routines) { routine ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Task Name: ${routine.name}", fontWeight = FontWeight.Bold)
                            Text("Timing: ${routine.time}")
                            Text("Recurrence: ${routine.recurrence}")
                        }
                    }
                }
            }
        }
    }

    // You can add your Pickers and fields if you want dynamic input before adding
    // Example of using Pickers:
    LaunchedEffect(Unit) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                pickedTime.value = LocalTime.of(hour, minute)
            },
            pickedTime.value.hour,
            pickedTime.value.minute,
            true
        )
    }
}
