package com.aurastream.mobile.ui.home

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val recommendedSongs: List<Song>,
        val smartPlaylists: List<Playlist>,
        val recentSongs: List<Song>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
