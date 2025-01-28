package com.daveleeds.arrowdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.fx.coroutines.parMap
import com.daveleeds.arrowdemo.Wrestler
import com.daveleeds.arrowdemo.data.WrestlerRepository
import com.daveleeds.arrowdemo.viewmodel.WrestlerListStatus.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WrestlerListUiState(
    val wrestlers: List<Wrestler> = emptyList(),
    val exception: Throwable? = null,
    val status: WrestlerListStatus = START
)

enum class WrestlerListStatus {
    START, LOADING, LOADED, ERROR
}

class WrestlerListViewModel(
    private val repository: WrestlerRepository = WrestlerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WrestlerListUiState())
    val uiState = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(status = LOADING) }

            try {
                val wrestlers = repository
                    .fetchWrestlerIds()
                    .parMap { id -> repository.fetchWrestler(id) }

                _uiState.update {
                    it.copy(
                        status = LOADED,
                        wrestlers = wrestlers,
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
    }
}
