package com.aurastream.mobile.ui.playlists

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song

sealed interface PlaylistsUiState {
    data object Loading : PlaylistsUiState
    data class Success(val playlists: List<Playlist>) : PlaylistsUiState
    data class Error(val message: String) : PlaylistsUiState
}

sealed interface PlaylistDetailUiState {
    data object Loading : PlaylistDetailUiState
    data class Success(val playlist: Playlist, val availableSongs: List<Song>) : PlaylistDetailUiState
    data class Error(val message: String) : PlaylistDetailUiState
}
