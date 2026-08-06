package com.aurastream.mobile.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurastream.mobile.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val removeSongFromPlaylistUseCase: RemoveSongFromPlaylistUseCase,
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlaylistsUiState>(PlaylistsUiState.Loading)
    val uiState: StateFlow<PlaylistsUiState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<PlaylistDetailUiState>(PlaylistDetailUiState.Loading)
    val detailUiState: StateFlow<PlaylistDetailUiState> = _detailUiState.asStateFlow()

    init {
        loadPlaylists()
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            _uiState.value = PlaylistsUiState.Loading
            getPlaylistsUseCase()
                .onSuccess { playlists ->
                    _uiState.value = PlaylistsUiState.Success(playlists)
                }
                .onFailure { ex ->
                    _uiState.value = PlaylistsUiState.Error(ex.localizedMessage ?: "Error al cargar playlists")
                }
        }
    }

    fun loadPlaylistDetail(playlistId: Long) {
        viewModelScope.launch {
            _detailUiState.value = PlaylistDetailUiState.Loading
            val playlistsRes = getPlaylistsUseCase()
            val songsRes = searchSongsUseCase(null)

            if (playlistsRes.isSuccess && songsRes.isSuccess) {
                val playlist = playlistsRes.getOrNull()?.find { it.id == playlistId }
                if (playlist != null) {
                    _detailUiState.value = PlaylistDetailUiState.Success(
                        playlist = playlist,
                        availableSongs = songsRes.getOrDefault(emptyList())
                    )
                } else {
                    _detailUiState.value = PlaylistDetailUiState.Error("Playlist no encontrada")
                }
            } else {
                _detailUiState.value = PlaylistDetailUiState.Error("Error al cargar la playlist")
            }
        }
    }

    fun createNewPlaylist(name: String, description: String?, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            createPlaylistUseCase(name, description)
                .onSuccess {
                    loadPlaylists()
                    onResult(true, null)
                }
                .onFailure { ex ->
                    onResult(false, ex.localizedMessage ?: "Error al crear la playlist")
                }
        }
    }

    fun addSongToPlaylist(playlistId: Long, songId: Long, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            addSongToPlaylistUseCase(playlistId, songId)
                .onSuccess {
                    loadPlaylists()
                    loadPlaylistDetail(playlistId)
                    onResult(true, null)
                }
                .onFailure { ex ->
                    onResult(false, ex.localizedMessage ?: "Error al agregar la canción")
                }
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            removeSongFromPlaylistUseCase(playlistId, songId)
                .onSuccess {
                    loadPlaylists()
                    loadPlaylistDetail(playlistId)
                }
        }
    }
}
