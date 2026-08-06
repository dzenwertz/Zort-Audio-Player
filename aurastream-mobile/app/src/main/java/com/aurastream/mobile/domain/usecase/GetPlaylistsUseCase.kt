package com.aurastream.mobile.domain.usecase

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.repository.AuraRepository

class GetPlaylistsUseCase(private val repository: AuraRepository) {
    suspend operator fun invoke(): Result<List<Playlist>> = repository.getPlaylists()
}
