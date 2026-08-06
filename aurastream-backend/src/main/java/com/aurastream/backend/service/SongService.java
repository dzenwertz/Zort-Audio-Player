package com.aurastream.backend.service;

import com.aurastream.backend.dto.SongDto;
import com.aurastream.backend.exception.ResourceNotFoundException;
import com.aurastream.backend.model.Song;
import com.aurastream.backend.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    @Transactional(readOnly = true)
    public List<SongDto> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SongDto getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        return mapToDto(song);
    }

    @Transactional(readOnly = true)
    public List<SongDto> searchSongs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllSongs();
        }
        return songRepository.searchSongs(query.trim()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SongDto> getRecommendedSongs() {
        return songRepository.findTop10ByOrderByPlayCountDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SongDto incrementPlayCount(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
        song.setPlayCount(song.getPlayCount() + 1);
        return mapToDto(songRepository.save(song));
    }

    public SongDto mapToDto(Song song) {
        return SongDto.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artistId(song.getArtist() != null ? song.getArtist().getId() : null)
                .artistName(song.getArtist() != null ? song.getArtist().getName() : "Unknown Artist")
                .albumId(song.getAlbum() != null ? song.getAlbum().getId() : null)
                .albumTitle(song.getAlbum() != null ? song.getAlbum().getTitle() : null)
                .coverUrl(song.getAlbum() != null ? song.getAlbum().getCoverUrl() : null)
                .durationSeconds(song.getDurationSeconds())
                .genre(song.getGenre())
                .audioUrl("/api/v1/songs/" + song.getId() + "/stream")
                .playCount(song.getPlayCount())
                .bpm(song.getBpm())
                .build();
    }
}
