package com.daveleeds.arrowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WrestlerListUiState(
    val wrestlers: List<Wrestler> = emptyList(),
    val loading: Boolean = false
)

class WrestlerListViewModel(
    private val repository: WrestlerRepository = WrestlerRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(WrestlerListUiState())
    val uiState = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val ids = repository.fetchWrestlerIds()

            val wrestlers = ids
                .map { id -> async { repository.fetchWrestler(id) } }
                .awaitAll()

            _uiState.update { it.copy(loading = false, wrestlers = wrestlers) }
        }
    }
}
