package com.daveleeds.arrowdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Companion.catch
import arrow.optics.optics
import arrow.optics.updateCopy
import com.daveleeds.arrowdemo.Hometown
import com.daveleeds.arrowdemo.Wrestler
import com.daveleeds.arrowdemo.age
import com.daveleeds.arrowdemo.city
import com.daveleeds.arrowdemo.country
import com.daveleeds.arrowdemo.data.WrestlerRepository
import com.daveleeds.arrowdemo.hometown
import com.daveleeds.arrowdemo.name
import com.daveleeds.arrowdemo.viewmodel.WrestlerEditStatus.*
import com.daveleeds.arrowdemo.viewmodel.wrestler
import com.daveleeds.arrowdemo.weight
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

enum class WrestlerEditStatus {
    START, LOADING, LOADED, SAVING, SAVED, ERROR
}

class WrestlerEditViewModel(
    private val repository: WrestlerRepository = WrestlerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WrestlerEditUiState())
    val uiState = _uiState.asStateFlow()

    fun load(id: Int) = viewModelScope.launch {
        _uiState.updateCopy {
            WrestlerEditUiState.status set LOADING
        }

        try {
            val wrestler = repository.fetchWrestler(id)
            _uiState.updateCopy {
                WrestlerEditUiState.status set LOADED
                WrestlerEditUiState.wrestler set wrestler
                WrestlerEditUiState.exception set null
            }
        } catch (e: Exception) {
            _uiState.updateCopy {
                WrestlerEditUiState.status set ERROR
                WrestlerEditUiState.exception set e
            }
        }
    }

    fun save() = viewModelScope.launch {
        _uiState.updateCopy {
            WrestlerEditUiState.status set SAVING
        }

        try {
            val wrestler = _uiState
                .value
                .wrestler
                .let { repository.saveWrestler(it) }

            _uiState.updateCopy {
                WrestlerEditUiState.status set SAVED
                WrestlerEditUiState.wrestler set wrestler
                WrestlerEditUiState.exception set null
            }
        } catch (e: Exception) {
            _uiState.updateCopy {
                WrestlerEditUiState.status set ERROR
                WrestlerEditUiState.exception set e
            }
        }
    }

    fun setName(name: String) = _uiState.updateCopy {
        WrestlerEditUiState.wrestler.name set name
    }

    fun setAge(age: Int) = _uiState.updateCopy {
        WrestlerEditUiState.wrestler.age set age
    }

    fun setWeight(weight: Int) = _uiState.updateCopy {
        WrestlerEditUiState.wrestler.weight set weight
    }

    fun setCity(city: String) = _uiState.updateCopy {
        WrestlerEditUiState.wrestler.hometown.city set city
    }

    fun setCountry(country: String) = _uiState.updateCopy {
        WrestlerEditUiState.wrestler.hometown.country set country
    }
}
