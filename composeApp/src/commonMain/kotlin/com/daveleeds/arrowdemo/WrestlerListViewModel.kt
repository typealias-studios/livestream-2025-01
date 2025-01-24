package com.daveleeds.arrowdemo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class WrestlerUiState(
    val wrestlers: List<Wrestler> = emptyList(),
    val loading: Boolean = false
)

class WrestlerListViewModel(
    private val repository: WrestlerRepository = WrestlerRepository(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(dispatcher)

    private val _uiState = MutableStateFlow(WrestlerUiState())
    val uiState = _uiState.asStateFlow()

    fun refresh() {
        scope.launch {
            _uiState.update { it.copy(loading = true) }

            val ids = repository.fetchWrestlerIds()

            val wrestlers = ids
                .map { id -> async { repository.fetchWrestler(id) } }
                .awaitAll()

            _uiState.update { it.copy(loading = false, wrestlers = wrestlers) }
        }
    }
}
