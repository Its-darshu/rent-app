package com.nammayantra.app.ui.requests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.PrecisionManufacturing
import com.nammayantra.app.data.model.RentalRequest
import com.nammayantra.app.data.model.RequestStatus
import com.nammayantra.app.data.model.mockRequests

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
    userRole: String,
    onBack: () -> Unit
) {
    // In a real app, this state would come from a ViewModel observing Supabase Realtime
    var requests by remember { mutableStateOf(mockRequests.toList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (userRole == "Owner") "Incoming Requests" else "My Rental Requests") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(requests) { request ->
                RequestCard(
                    request = request,
                    userRole = userRole,
                    onStatusChange = { newStatus ->
                        // Simulate updating the request status locally
                        request.status = newStatus
                        requests = requests.toList() // Trigger recomposition
                    }
                )
            }
        }
    }
}

@Composable
fun RequestCard(
    request: RentalRequest,
    userRole: String,
    onStatusChange: (RequestStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                val icon: ImageVector = when {
                    request.equipmentName.contains("Harvester", ignoreCase = true) || request.equipmentName.contains("Kubota", ignoreCase = true) -> Icons.Filled.PrecisionManufacturing
                    request.equipmentName.contains("Sprayer", ignoreCase = true) || request.equipmentName.contains("Aspee", ignoreCase = true) -> Icons.Filled.WaterDrop
                    else -> Icons.Filled.Agriculture
                }
                Icon(
                    imageVector = icon, 
                    contentDescription = "Machinery Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = request.equipmentName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            if (userRole == "Owner") {
                Text(text = "Requested by: ${request.farmerName}")
            } else {
                Text(text = "Owner: ${request.ownerName}")
            }
            
            Text(text = "Date: ${request.date}")
            Text(text = "Duration: ${request.durationText}")
            Text(text = "Total Price: ₹${request.totalPrice.toInt()}", fontWeight = FontWeight.Medium)
            
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusBadge(status = request.status)

                // If logged in as Owner and status is PENDING, show Accept/Decline actions
                if (userRole == "Owner" && request.status == RequestStatus.PENDING) {
                    Row {
                        OutlinedButton(onClick = { onStatusChange(RequestStatus.DECLINED) }) {
                            Text("Decline", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { onStatusChange(RequestStatus.ACCEPTED) }) {
                            Text("Accept")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: RequestStatus) {
    val (color, text) = when (status) {
        RequestStatus.PENDING -> Color(0xFFF57C00) to "Pending"
        RequestStatus.ACCEPTED -> Color(0xFF2E7D32) to "Accepted"
        RequestStatus.DECLINED -> Color(0xFFC62828) to "Declined"
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}