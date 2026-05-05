package com.nammayantra.app.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammayantra.app.data.model.Equipment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    equipment: Equipment,
    onBack: () -> Unit,
    onRequestSent: () -> Unit
) {
    var isHourly by remember { mutableStateOf(true) }
    var duration by remember { mutableStateOf(4) }
    var selectedDate by remember { mutableStateOf("Oct 25, 2026") } // Simulated date selection MVP

    val rate = if (isHourly) equipment.hourlyRate else equipment.dailyRate
    val unit = if (isHourly) "hours" else "days"
    val total = duration * rate

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request ${equipment.type}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(equipment.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Owned by ${equipment.ownerName} • ${equipment.distanceKm} km away", color = MaterialTheme.colorScheme.secondary)
            
            Spacer(modifier = Modifier.height(16.dp))

            // Machine Health Section (Simulated)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Machine Health", fontWeight = FontWeight.Bold)
                    Text("Condition Rating: ${equipment.healthCondition}")
                    Text("Last Serviced: Sep 10, 2026")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Rental Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            // Date Selection
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = { Text("Select Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = false // Kept editable for MVP simulation
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Booking Type (Hourly or Daily)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = isHourly, onClick = { isHourly = true; duration = 4 })
                Text("Hourly (₹${equipment.hourlyRate}/hr)")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = !isHourly, onClick = { isHourly = false; duration = 1 })
                Text("Daily (₹${equipment.dailyRate}/day)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration Slider/Counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Duration: $duration $unit")
                Row {
                    Button(onClick = { if (duration > 1) duration-- }) { Text("-") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { duration++ }) { Text("+") }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Price Calculator (As strictly defined in the prompt)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "$duration $unit x ₹${rate.toInt()} = Total ₹${total.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRequestSent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Send Request to Owner", fontSize = 16.sp)
            }
        }
    }
}