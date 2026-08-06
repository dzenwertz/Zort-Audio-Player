package com.aurastream.mobile.domain.usecase

import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.domain.repository.AuraRepository

class SearchSongsUseCase(private val repository: AuraRepository) {
    suspend operator fun invoke(query: String?): Result<List<Song>> = repository.searchSongs(query)
}
