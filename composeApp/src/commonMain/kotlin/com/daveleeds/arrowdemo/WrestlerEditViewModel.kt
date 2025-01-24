package com.daveleeds.arrowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveleeds.arrowdemo.WrestlerEditStatus.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WrestlerEditUiState(
    val wrestler: Wrestler? = null,
    val status: WrestlerEditStatus = FRESH
)

enum class WrestlerEditStatus {
    FRESH, LOADING, LOADED, SAVING, SAVED
}

class WrestlerEditViewModel(
    private val repository: WrestlerRepository = WrestlerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WrestlerEditUiState())
    val uiState = _uiState.asStateFlow()

    fun load(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = LOADING) }
            val wrestler = async { repository.fetchWrestler(id) }
            _uiState.update { it.copy(status = LOADED, wrestler = wrestler.await()) }
        }
    }

    fun save() {
        viewModelScope.launch {
            _uiState.update { it.copy(status = SAVING) }
            _uiState.value.wrestler?.let { repository.saveWrestler(it) }
            _uiState.update { it.copy(status = SAVED) }
        }
    }

    fun setName(name: String) = _uiState.update {
        it.copy(wrestler = it.wrestler?.copy(name = name))
    }

    fun setAge(age: Int) = _uiState.update {
        it.copy(wrestler = it.wrestler?.copy(age = age))
    }

    fun setWeight(weight: Int) = _uiState.update {
        it.copy(wrestler = it.wrestler?.copy(weight = weight))
    }

    fun setCity(city: String) = _uiState.update {
        it.copy(wrestler = it.wrestler?.copy(hometown = it.wrestler.hometown.copy(city = city)))
    }

    fun setCountry(country: String) = _uiState.update {
        it.copy(wrestler = it.wrestler?.copy(hometown = it.wrestler.hometown.copy(country = country)))
    }
}
