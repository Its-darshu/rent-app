package com.nammayantra.app.ui.marketplace

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun MarketplaceHomeScreen(
    onListingClick: (Equipment) -> Unit
) {
    val vm: MarketplaceViewModel = viewModel()
    val listings by vm.listings.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val selectedCategory by vm.selectedCategory.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            // Tagline banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(ForestGreen, Color(0xFF1B8C2A)))
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Column {
                    Text(
                        "Rent Farm Equipment",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PureWhite
                    )
                    Text(
                        "Tractors • Harvesters • Sprayers & more",
                        fontSize = 13.sp,
                        color = PureWhite.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PureWhite.copy(alpha = 0.15f))
                            .border(1.dp, PureWhite.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Search, null, tint = PureWhite.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Search tractors, harvesters…", fontSize = 14.sp, color = PureWhite.copy(alpha = 0.7f))
                    }
                }
            }
        }

        // Categories
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.categories) { category ->
                    val isSelected = category == selectedCategory
                    Surface(
                        onClick = { vm.selectCategory(category) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ForestGreen else PureWhite,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.5.dp, NammaOutlineVariant)
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) PureWhite else InkBlack
                        )
                    }
                }
            }
            Divider(color = NammaOutlineVariant, thickness = 1.dp)
        }

        // Section header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${if (selectedCategory == "All") "All" else selectedCategory} Equipment",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkBlack
                )
                TextButton(onClick = { vm.refresh() }) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp), tint = ForestGreen)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Refresh", fontSize = 13.sp, color = ForestGreen)
                }
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ForestGreen)
                }
            }
        } else if (listings.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Filled.Agriculture, null, modifier = Modifier.size(56.dp), tint = NammaOutlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No listings yet", fontSize = 16.sp, color = NammaOnSurfaceVariant, fontWeight = FontWeight.Medium)
                    Text("Be the first to post!", fontSize = 13.sp, color = NammaOutlineVariant)
                }
            }
        } else {
            items(listings) { equipment ->
                ListingCard(equipment = equipment, onClick = { onListingClick(equipment) })
            }
        }
    }
}

@Composable
fun ListingCard(equipment: Equipment, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.5.dp, NammaOutlineVariant, RoundedCornerShape(8.dp))
            .background(PureWhite)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Image
        Box(
            modifier = Modifier
                .size(100.dp)
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
                Icon(Icons.Filled.Agriculture, null, tint = NammaOutlineVariant, modifier = Modifier.size(36.dp))
            }
        }

        // Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                equipment.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = ForestGreen.copy(alpha = 0.1f)
            ) {
                Text(
                    equipment.category,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 11.sp,
                    color = ForestGreen,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "₹${equipment.hourlyRate.toInt()}/hr",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = EarthOrange
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Filled.LocationOn, null, tint = NammaOnSurfaceVariant, modifier = Modifier.size(13.dp))
                Text(equipment.location.ifBlank { "Location N/A" }, fontSize = 12.sp, color = NammaOnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Filled.Person, null, tint = NammaOnSurfaceVariant, modifier = Modifier.size(13.dp))
                Text(equipment.ownerName, fontSize = 12.sp, color = NammaOnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
