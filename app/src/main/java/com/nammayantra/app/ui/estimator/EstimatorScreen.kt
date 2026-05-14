package com.nammayantra.app.ui.estimator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammayantra.app.ui.components.hardShadow
import com.nammayantra.app.ui.theme.*

private data class Estimate(
    val machine: String,
    val hours: Int,
    val costPerHour: Int,
    val tip: String
)

private fun getEstimate(crop: String, acres: Int, soil: String): Estimate {
    val soilMultiplier = when (soil) {
        "Soft" -> 0.8
        "Hard" -> 1.8
        else -> 1.2
    }
    val (machine, rate) = when (crop) {
        "Paddy" -> "Power Tiller" to 700
        "Wheat" -> "Tractor" to 800
        "Sugarcane" -> "Harvester" to 1200
        "Cotton" -> "Tractor" to 800
        else -> "Power Tiller" to 700
    }
    val hours = maxOf(1, (acres * soilMultiplier).toInt())
    val tip = when (soil) {
        "Hard" -> "Pre-soak the field 24 hrs before operation for best results."
        "Soft" -> "Early morning operation suggested for best soil moisture."
        else -> "Ensure machine is inspected before starting field work."
    }
    return Estimate(machine, hours, rate, tip)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimatorScreen() {
    val crops = listOf("Paddy", "Wheat", "Sugarcane", "Cotton", "Vegetables")
    val soils = listOf("Soft", "Medium", "Hard")

    var selectedCrop by remember { mutableStateOf("Paddy") }
    var acres by remember { mutableStateOf(2) }
    var selectedSoil by remember { mutableStateOf("Medium") }
    var estimate by remember { mutableStateOf<Estimate?>(null) }

    var cropExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Filled.AutoAwesome, null, tint = ForestGreen, modifier = Modifier.size(28.dp))
            Text("Smart Estimator", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = InkBlack)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tell us what you need, and our AI will calculate the best machine and estimated time for your field.",
            fontSize = 14.sp,
            color = NammaOnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Input card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hardShadow()
                .border(2.dp, InkBlack)
                .background(PureWhite)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                // Crop type dropdown
                Column {
                    Text("Crop Type", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = InkBlack)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = cropExpanded,
                        onExpandedChange = { cropExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCrop,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cropExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = MaterialTheme.shapes.extraSmall,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = InkBlack,
                                focusedBorderColor = ForestGreen
                            )
                        )
                        ExposedDropdownMenu(expanded = cropExpanded, onDismissRequest = { cropExpanded = false }) {
                            crops.forEach { crop ->
                                DropdownMenuItem(
                                    text = { Text(crop) },
                                    onClick = { selectedCrop = crop; cropExpanded = false }
                                )
                            }
                        }
                    }
                }

                // Land area stepper
                Column {
                    Text("Land Area (Acres)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = InkBlack)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, InkBlack),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .border(width = 0.dp, color = Color.Transparent)
                                .clickable { if (acres > 1) acres-- },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Remove, null, tint = InkBlack)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(width = 2.dp, color = InkBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(acres.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = InkBlack,
                                modifier = Modifier.padding(vertical = 12.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clickable { if (acres < 50) acres++ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Add, null, tint = InkBlack)
                        }
                    }
                }

                // Soil condition selector
                Column {
                    Text("Soil Condition", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = InkBlack)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, InkBlack),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        soils.forEachIndexed { index, soil ->
                            val isSelected = selectedSoil == soil
                            if (index > 0) Divider(color = InkBlack, thickness = 2.dp)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isSelected) EarthOrange else Color.Transparent)
                                    .clickable { selectedSoil = soil }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    soil,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PureWhite else InkBlack,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                // Estimate button
                Button(
                    onClick = { estimate = getEstimate(selectedCrop, acres, selectedSoil) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .hardShadow(EarthOrange),
                    colors = ButtonDefaults.buttonColors(containerColor = EarthOrange),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Icon(Icons.Filled.Calculate, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GET ESTIMATE", fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
                }
            }
        }

        // Results
        estimate?.let { result ->
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hardShadow(ForestGreenContainer)
                    .border(2.dp, ForestGreenContainer)
                    .background(ForestGreen)
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.Bolt, null, tint = EarthOrangeContainer, modifier = Modifier.size(22.dp))
                        Text("AI Recommendation", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PureWhite)
                    }
                    Divider(color = PureWhite.copy(alpha = 0.3f))

                    EstimateRow(
                        icon = Icons.Filled.Agriculture,
                        label = "RECOMMENDED MACHINE",
                        value = result.machine
                    )
                    EstimateRow(
                        icon = Icons.Filled.Schedule,
                        label = "SUGGESTED HOURS",
                        value = "${result.hours} Hours"
                    )
                    EstimateRow(
                        icon = Icons.Filled.CurrencyRupee,
                        label = "ESTIMATED COST",
                        value = "₹${result.hours * result.costPerHour}"
                    )

                    Divider(color = PureWhite.copy(alpha = 0.3f))
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Lightbulb, null, tint = PureWhite.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                        Text(
                            "*${result.tip}*",
                            fontSize = 12.sp,
                            color = PureWhite.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun EstimateRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite.copy(alpha = 0.15f))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, null, tint = PureWhite, modifier = Modifier.size(24.dp))
            Column {
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PureWhite.copy(alpha = 0.7f), letterSpacing = 0.5.sp)
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PureWhite)
            }
        }
    }
}
