package com.nammayantra.app.ui.mylistings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.data.repository.EquipmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyListingsViewModel : ViewModel() {

    private val _listings = MutableStateFlow<List<Equipment>>(emptyList())
    val listings: StateFlow<List<Equipment>> = _listings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun load(ownerId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _listings.value = EquipmentRepository.listByOwner(ownerId)
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun delete(id: String, ownerId: String) {
        viewModelScope.launch {
            try {
                EquipmentRepository.deleteListing(id)
                load(ownerId)
            } catch (_: Exception) {}
        }
    }
}
