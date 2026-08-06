package com.aurastream.mobile.domain.usecase

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.repository.AuraRepository

class CreatePlaylistUseCase(private val repository: AuraRepository) {
    suspend operator fun invoke(name: String, description: String?): Result<Playlist> {
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Playlist name cannot be empty"))
        }
        return repository.createPlaylist(name, description)
    }
}
