package com.daveleeds.arrowdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveleeds.arrowdemo.Wrestler
import com.daveleeds.arrowdemo.data.WrestlerRepository
import com.daveleeds.arrowdemo.viewmodel.WrestlerEditStatus.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WrestlerEditUiState(
    val wrestler: Wrestler? = null,
    val exception: Throwable? = null,
    val status: WrestlerEditStatus = START
)

enum class WrestlerEditStatus {
    START, LOADING, LOADED, SAVING, SAVED, ERROR
}

class WrestlerEditViewModel(
    private val repository: WrestlerRepository = WrestlerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WrestlerEditUiState())
    val uiState = _uiState.asStateFlow()

    fun load(id: Int) = viewModelScope.launch {
        _uiState.update { it.copy(status = LOADING) }

        try {
            val wrestler = repository.fetchWrestler(id)
            _uiState.update {
                it.copy(
                    status = LOADED,
                    wrestler = wrestler,
                    exception = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    status = ERROR,
                    exception = e
                )
            }
        }
    }

    fun save() = viewModelScope.launch {
        _uiState.update { it.copy(status = SAVING) }

        try {
            val wrestler = _uiState
                .value
                .wrestler
                ?.let { repository.saveWrestler(it) }

            _uiState.update {
                it.copy(
                    status = SAVED,
                    wrestler = wrestler,
                    exception = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    status = ERROR,
                    exception = e
                )
            }
        }
    }

    fun setName(name: String) = _uiState.update {
        it.copy(
            wrestler = it.wrestler?.copy(
                name = name
            )
        )
    }

    fun setAge(age: Int) = _uiState.update {
        it.copy(
            wrestler = it.wrestler?.copy(
                age = age
            )
        )
    }

    fun setWeight(weight: Int) = _uiState.update {
        it.copy(
            wrestler = it.wrestler?.copy(
                weight = weight
            )
        )
    }

    fun setCity(city: String) = _uiState.update {
        it.copy(
            wrestler = it.wrestler?.copy(
                hometown = it.wrestler.hometown.copy(city = city)
            )
        )
    }

    fun setCountry(country: String) = _uiState.update {
        it.copy(
            wrestler = it.wrestler?.copy(
                hometown = it.wrestler.hometown.copy(
                    country = country
                )
            )
        )
    }
}
