package com.daveleeds.arrowdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Companion.catch
import arrow.optics.optics
import com.daveleeds.arrowdemo.*
import com.daveleeds.arrowdemo.data.WrestlerRepository
import com.daveleeds.arrowdemo.viewmodel.WrestlerEditStatus.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@optics data class WrestlerEditUiState(
    val wrestler: Wrestler = Wrestler(-1, "", -1, -1, Hometown("", "")),
    val exception: Throwable? = null,
    val status: WrestlerEditStatus = START
) {
    companion object
}

enum class WrestlerEditStatus { START, LOADING, LOADED, SAVING, SAVED, ERROR }

class WrestlerEditViewModel(
    private val repository: WrestlerRepository = WrestlerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WrestlerEditUiState())
    val uiState = _uiState.asStateFlow()

    fun load(id: Int) = viewModelScope.launch {
        _uiState.update { it.copy(status = LOADING) }

        val result = catch { repository.fetchWrestler(id) }

        _uiState.update { state ->
            result.fold(
                ifLeft = { state.copy(status = ERROR, exception = it) },
                ifRight = { state.copy(status = LOADED, wrestler = it, exception = null) }
            )
        }
    }

    fun save() = viewModelScope.launch {
        _uiState.update { it.copy(status = SAVING) }

        val result = catch { _uiState.value.wrestler.let { repository.saveWrestler(it) } }

        _uiState.update { state ->
            result.fold(
                ifLeft = { state.copy(status = ERROR, exception = it) },
                ifRight = { state.copy(status = SAVED, wrestler = it, exception = null) }
            )
        }
    }

    fun setName(name: String) = _uiState.update {
        WrestlerEditUiState.wrestler.name.modify(it) { name }
    }

    fun setAge(age: Int) = _uiState.update {
        WrestlerEditUiState.wrestler.age.modify(it) { age }
    }

    fun setWeight(weight: Int) = _uiState.update {
        WrestlerEditUiState.wrestler.weight.modify(it) { weight }
    }

    fun setCity(city: String) = _uiState.update {
        WrestlerEditUiState.wrestler.hometown.city.modify(it) { city }
    }

    fun setCountry(country: String) = _uiState.update {
        WrestlerEditUiState.wrestler.hometown.country.modify(it) { country }
    }
}
