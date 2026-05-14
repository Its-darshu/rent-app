package com.nammayantra.app.ui.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.data.repository.EquipmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarketplaceViewModel : ViewModel() {

    private val _listings = MutableStateFlow<List<Equipment>>(emptyList())
    val listings: StateFlow<List<Equipment>> = _listings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf("All", "Tractor", "Harvester", "Sprayer", "Tiller", "Seeder", "Pump", "Other")

    init {
        load("All")
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        load(category)
    }

    fun refresh() = load(_selectedCategory.value)

    private fun load(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _listings.value = if (category == "All") {
                    EquipmentRepository.listAll()
                } else {
                    EquipmentRepository.listByCategory(category)
                }
            } catch (_: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}
