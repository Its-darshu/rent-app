package com.nammayantra.app.ui.mylistings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.ui.theme.*

@Composable
fun MyListingsScreen(ownerId: String) {
    val vm: MyListingsViewModel = viewModel()
    val listings by vm.listings.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(ownerId) { vm.load(ownerId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("My Listings", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = InkBlack)
            TextButton(onClick = { vm.load(ownerId) }) {
                Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp), tint = ForestGreen)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Refresh", color = ForestGreen, fontSize = 13.sp)
            }
        }
        Divider(color = NammaOutlineVariant)

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ForestGreen)
            }
        } else if (listings.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.PostAdd, null, modifier = Modifier.size(60.dp), tint = NammaOutlineVariant)
                Spacer(modifier = Modifier.height(12.dp))
                Text("No listings yet", fontSize = 16.sp, color = NammaOnSurfaceVariant, fontWeight = FontWeight.Medium)
                Text("Tap + to post your first equipment", fontSize = 13.sp, color = NammaOutlineVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listings) { equipment ->
                    MyListingCard(
                        equipment = equipment,
                        onDelete = { vm.delete(equipment.id, ownerId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyListingCard(equipment: Equipment, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Listing?") },
            text = { Text("\"${equipment.name}\" will be removed from public listings.") },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false; onDelete() }) {
                    Text("Delete", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.5.dp, NammaOutlineVariant, RoundedCornerShape(8.dp))
            .background(PureWhite)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(SurfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            if (equipment.imageUrls.isNotEmpty()) {
                AsyncImage(
                    model = equipment.imageUrls.first(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Filled.Agriculture, null, tint = NammaOutlineVariant, modifier = Modifier.size(28.dp))
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(equipment.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = InkBlack, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(equipment.category, fontSize = 12.sp, color = ForestGreen)
            Text("₹${equipment.hourlyRate.toInt()}/hr", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = EarthOrange)
            if (equipment.location.isNotBlank()) {
                Text(equipment.location, fontSize = 11.sp, color = NammaOnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }

        IconButton(onClick = { showDeleteDialog = true }) {
            Icon(Icons.Filled.DeleteOutline, null, tint = ErrorRed)
        }
    }
}
