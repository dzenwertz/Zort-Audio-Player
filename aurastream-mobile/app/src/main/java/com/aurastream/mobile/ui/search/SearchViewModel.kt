package com.aurastream.mobile.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurastream.mobile.domain.usecase.SearchSongsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        performSearch(null)
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        performSearch(newQuery)
    }

    fun performSearch(q: String?) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            searchSongsUseCase(q)
                .onSuccess { songs ->
                    _uiState.value = SearchUiState.Success(songs)
                }
                .onFailure { ex ->
                    _uiState.value = SearchUiState.Error(ex.localizedMessage ?: "Error en la búsqueda")
                }
        }
    }
}
