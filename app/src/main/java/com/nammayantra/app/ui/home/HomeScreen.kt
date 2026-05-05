package com.nammayantra.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.PrecisionManufacturing
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.data.model.mockEquipments

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userRole: String, onEquipmentClick: (Equipment) -> Unit, onViewRequests: () -> Unit, onProfileClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (userRole == "Owner") "My Machinery" else "Nearby Machinery") },
                actions = {
                    IconButton(onClick = onViewRequests) {
                        Icon(Icons.Filled.List, contentDescription = "View Requests")
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile")
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
            items(mockEquipments) { equipment ->
                EquipmentCard(equipment = equipment, onClick = { onEquipmentClick(equipment) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentCard(equipment: Equipment, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    val icon: ImageVector = when (equipment.type) {
                        "Tractor" -> Icons.Filled.Agriculture
                        "Harvester" -> Icons.Filled.PrecisionManufacturing
                        "Sprayer" -> Icons.Filled.WaterDrop
                        else -> Icons.Filled.Agriculture
                    }
                    Icon(
                        imageVector = icon, 
                        contentDescription = equipment.type,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = equipment.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = if (equipment.isAvailable) "Available" else "In Use",
                    color = if (equipment.isAvailable) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type: ${equipment.type}")
            Text(text = "Owner: ${equipment.ownerName} • Distance: ${equipment.distanceKm} km")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${equipment.hourlyRate}/Hr | ₹${equipment.dailyRate}/Day",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}