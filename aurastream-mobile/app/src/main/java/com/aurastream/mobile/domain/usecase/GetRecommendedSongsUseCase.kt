package com.aurastream.mobile.domain.usecase

import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.domain.repository.AuraRepository

class GetRecommendedSongsUseCase(private val repository: AuraRepository) {
    suspend operator fun invoke(): Result<List<Song>> = repository.getRecommendedSongs()
}
