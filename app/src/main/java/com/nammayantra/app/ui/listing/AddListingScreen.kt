package com.nammayantra.app.ui.listing

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nammayantra.app.data.model.UserProfile
import com.nammayantra.app.ui.auth.KrishiLabel
import com.nammayantra.app.ui.auth.KrishiTextField
import com.nammayantra.app.ui.location.LocationPickerField
import com.nammayantra.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListingScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val vm: AddListingViewModel = viewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tractor") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf(profile.phone) }
    var hourlyRate by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var categoryExpanded by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imageUris = (imageUris + uris).take(5)
    }

    LaunchedEffect(uiState) {
        if (uiState is AddListingViewModel.UiState.Success) {
            onSuccess()
            vm.resetState()
        }
    }

    val isLoading = uiState is AddListingViewModel.UiState.Loading
    val error = (uiState as? AddListingViewModel.UiState.Error)?.message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ForestGreen)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, null, tint = PureWhite)
            }
            Text("Post a Listing", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PureWhite)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image picker
            Text("Photos", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = InkBlack)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Add photo button
                item {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, NammaOutlineVariant, RoundedCornerShape(8.dp))
                            .background(SurfaceContainer)
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.AddAPhoto, null, tint = NammaOnSurfaceVariant, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Add\n${imageUris.size}/5", fontSize = 11.sp, color = NammaOnSurfaceVariant)
                        }
                    }
                }
                itemsIndexed(imageUris) { index, uri ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { imageUris = imageUris.toMutableList().also { it.removeAt(index) } },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                                .background(InkBlack.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Filled.Close, null, tint = PureWhite, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Name
            KrishiLabel("EQUIPMENT NAME *")
            KrishiTextField(value = name, onValueChange = { name = it }, placeholder = "e.g. Mahindra 575 DI Tractor")

            // Category
            KrishiLabel("CATEGORY *")
            ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = it }) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = InkBlack,
                        focusedBorderColor = ForestGreen
                    )
                )
                ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                    vm.categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = { selectedCategory = cat; categoryExpanded = false }
                        )
                    }
                }
            }

            // Price
            KrishiLabel("PRICE PER HOUR (₹) *")
            KrishiTextField(value = hourlyRate, onValueChange = { hourlyRate = it }, placeholder = "e.g. 500")

            // Location
            KrishiLabel("LOCATION")
            LocationPickerField(
                address = location,
                onLocationSelected = { location = it }
            )

            // Phone
            KrishiLabel("CONTACT PHONE")
            KrishiTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Your phone number",
                leadingIcon = { Icon(Icons.Filled.Phone, null, tint = NammaOnSurfaceVariant) }
            )

            // Description
            KrishiLabel("DESCRIPTION")
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Describe condition, availability, special features…", color = NammaOnSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = MaterialTheme.shapes.extraSmall,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = InkBlack,
                    focusedBorderColor = ForestGreen
                )
            )

            error?.let {
                Text(it, color = ErrorRed, fontSize = 13.sp)
            }

            Button(
                onClick = {
                    vm.submit(
                        context = context,
                        name = name,
                        category = selectedCategory,
                        description = description,
                        location = location,
                        phone = phone,
                        hourlyRate = hourlyRate,
                        imageUris = imageUris,
                        ownerId = profile.id,
                        ownerName = profile.name
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = PureWhite, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Filled.CloudUpload, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("POST LISTING", fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 0.5.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
