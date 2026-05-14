package com.nammayantra.app.ui.listing

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.ui.theme.*

@Composable
fun ListingDetailScreen(
    equipment: Equipment,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PureWhite)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, null, tint = InkBlack)
            }
            Text(
                equipment.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.weight(1f)
            )
        }
        Divider(color = NammaOutlineVariant)

        // Images
        if (equipment.imageUrls.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(SurfaceContainer),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(equipment.imageUrls) { _, url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(260.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(SurfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Agriculture, null, tint = NammaOutlineVariant, modifier = Modifier.size(56.dp))
                    Text("No photos", fontSize = 14.sp, color = NammaOnSurfaceVariant)
                }
            }
        }

        // Main content card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Price + category
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "₹${equipment.hourlyRate.toInt()}/hr",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EarthOrange
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ForestGreen.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f))
                ) {
                    Text(
                        equipment.category,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        fontSize = 13.sp,
                        color = ForestGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(equipment.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = InkBlack)

            if (equipment.location.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.LocationOn, null, tint = NammaOnSurfaceVariant, modifier = Modifier.size(16.dp))
                    Text(equipment.location, fontSize = 14.sp, color = NammaOnSurfaceVariant)
                }
            }

            if (equipment.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = NammaOutlineVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Description", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = InkBlack)
                Spacer(modifier = Modifier.height(8.dp))
                Text(equipment.description, fontSize = 14.sp, color = NammaOnSurfaceVariant, lineHeight = 22.sp)
            }

            // Owner card
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = NammaOutlineVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Listed by", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = InkBlack)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.5.dp, NammaOutlineVariant, RoundedCornerShape(8.dp))
                    .background(PureWhite)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ForestGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        equipment.ownerName.take(1).uppercase(),
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(equipment.ownerName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = InkBlack)
                    if (equipment.ownerPhone.isNotBlank()) {
                        Text(equipment.ownerPhone, fontSize = 13.sp, color = NammaOnSurfaceVariant)
                    }
                }
            }

            // Contact buttons
            if (equipment.ownerPhone.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Call button
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${equipment.ownerPhone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f).height(52.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, ForestGreen),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
                    ) {
                        Icon(Icons.Filled.Call, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call", fontWeight = FontWeight.Bold)
                    }

                    // WhatsApp button
                    Button(
                        onClick = {
                            val number = equipment.ownerPhone.replace("[^0-9]".toRegex(), "")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/91$number"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Filled.Message, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontWeight = FontWeight.Bold, color = PureWhite)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
