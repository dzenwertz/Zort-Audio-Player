package com.aurastream.mobile.ui.search

import com.aurastream.mobile.domain.model.Song

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Success(val songs: List<Song>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}
