package com.nammayantra.app.ui.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.nammayantra.app.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun LocationPickerField(
    address: String,
    onLocationSelected: (address: String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Default: center of India
    var pinPosition by remember { mutableStateOf(LatLng(20.5937, 78.9629)) }
    var isFetching by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pinPosition, 12f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) fetchLocation(context, scope) { latLng, addr ->
            pinPosition = latLng
            cameraState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
            onLocationSelected(addr)
            showMap = true
            isFetching = false
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Current address display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, InkBlack, RoundedCornerShape(4.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Filled.LocationOn, null, tint = ForestGreen, modifier = Modifier.size(20.dp))
            Text(
                text = address.ifBlank { "No location selected" },
                fontSize = 14.sp,
                color = if (address.isBlank()) NammaOnSurfaceVariant else InkBlack,
                modifier = Modifier.weight(1f)
            )
        }

        // Buttons row
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    isFetching = true
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        fetchLocation(context, scope) { latLng, addr ->
                            pinPosition = latLng
                            cameraState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                            onLocationSelected(addr)
                            showMap = true
                            isFetching = false
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                modifier = Modifier.weight(1f),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreen),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen),
                enabled = !isFetching
            ) {
                if (isFetching) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = ForestGreen, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.MyLocation, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Use My Location", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            OutlinedButton(
                onClick = { showMap = !showMap },
                modifier = Modifier.weight(1f),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NammaOutlineVariant),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = InkBlack)
            ) {
                Text(if (showMap) "Hide Map" else "Pick on Map", fontSize = 13.sp)
            }
        }

        // Map
        if (showMap) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.5.dp, NammaOutlineVariant, RoundedCornerShape(8.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraState,
                    onMapClick = { latLng ->
                        pinPosition = latLng
                        scope.launch {
                            val addr = reverseGeocode(context, latLng)
                            onLocationSelected(addr)
                        }
                    }
                ) {
                    Marker(
                        state = MarkerState(position = pinPosition),
                        draggable = true,
                        onInfoWindowClick = {}
                    )
                }
                Text(
                    "Tap or drag pin to set location",
                    fontSize = 11.sp,
                    color = InkBlack,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .border(0.5.dp, NammaOutlineVariant, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private fun fetchLocation(
    context: Context,
    scope: kotlinx.coroutines.CoroutineScope,
    onResult: (LatLng, String) -> Unit
) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    try {
        client.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                scope.launch {
                    val addr = reverseGeocode(context, latLng)
                    onResult(latLng, addr)
                }
            } else {
                // Request fresh location
                client.getCurrentLocation(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null
                ).addOnSuccessListener { loc ->
                    if (loc != null) {
                        val latLng = LatLng(loc.latitude, loc.longitude)
                        scope.launch {
                            val addr = reverseGeocode(context, latLng)
                            onResult(latLng, addr)
                        }
                    }
                }
            }
        }
    } catch (_: SecurityException) {}
}

private suspend fun reverseGeocode(context: Context, latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val a = addresses[0]
                listOfNotNull(a.subLocality, a.locality, a.adminArea)
                    .distinct().joinToString(", ")
            } else ""
        } catch (_: Exception) { "" }
    }
}
