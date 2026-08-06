package com.aurastream.mobile.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurastream.mobile.domain.usecase.GetPlaylistsUseCase
import com.aurastream.mobile.domain.usecase.GetRecommendedSongsUseCase
import com.aurastream.mobile.domain.usecase.SearchSongsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRecommendedSongsUseCase: GetRecommendedSongsUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val recResult = getRecommendedSongsUseCase()
            val playlistResult = getPlaylistsUseCase()
            val recentResult = searchSongsUseCase(null)

            if (recResult.isSuccess && playlistResult.isSuccess && recentResult.isSuccess) {
                _uiState.value = HomeUiState.Success(
                    recommendedSongs = recResult.getOrDefault(emptyList()),
                    smartPlaylists = playlistResult.getOrDefault(emptyList()),
                    recentSongs = recentResult.getOrDefault(emptyList())
                )
            } else {
                _uiState.value = HomeUiState.Error("Error al cargar la música. Verifica tu conexión.")
            }
        }
    }
}
