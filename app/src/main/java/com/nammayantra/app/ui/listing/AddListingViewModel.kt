package com.nammayantra.app.ui.listing

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.data.repository.EquipmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddListingViewModel : ViewModel() {

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val categories = listOf("Tractor", "Harvester", "Sprayer", "Tiller", "Seeder", "Pump", "Other")

    fun submit(
        context: Context,
        name: String,
        category: String,
        description: String,
        location: String,
        phone: String,
        hourlyRate: String,
        imageUris: List<Uri>,
        ownerId: String,
        ownerName: String
    ) {
        val rate = hourlyRate.toDoubleOrNull()
        if (name.isBlank() || rate == null || rate <= 0) {
            _uiState.value = UiState.Error("Please fill name and a valid hourly rate.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val imageUrls = if (imageUris.isNotEmpty()) {
                    EquipmentRepository.uploadImages(context, ownerId, imageUris)
                } else emptyList()

                val equipment = Equipment(
                    name = name.trim(),
                    category = category,
                    description = description.trim(),
                    hourlyRate = rate,
                    imageUrls = imageUrls,
                    ownerId = ownerId,
                    ownerName = ownerName,
                    ownerPhone = phone.trim(),
                    location = location.trim(),
                    isAvailable = true
                )
                EquipmentRepository.addListing(equipment)
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to post listing.")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}
