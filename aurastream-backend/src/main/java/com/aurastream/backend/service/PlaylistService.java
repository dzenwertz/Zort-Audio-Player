package com.aurastream.backend.service;

import com.aurastream.backend.dto.*;
import com.aurastream.backend.exception.DuplicateResourceException;
import com.aurastream.backend.exception.ResourceNotFoundException;
import com.aurastream.backend.model.Playlist;
import com.aurastream.backend.model.PlaylistItem;
import com.aurastream.backend.model.Song;
import com.aurastream.backend.repository.PlaylistItemRepository;
import com.aurastream.backend.repository.PlaylistRepository;
import com.aurastream.backend.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistItemRepository playlistItemRepository;
    private final SongRepository songRepository;
    private final SongService songService;

    @Transactional(readOnly = true)
    public List<PlaylistDto> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlaylistDto getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));
        return mapToDto(playlist);
    }

    @Transactional
    public PlaylistDto createPlaylist(CreatePlaylistRequest request) {
        Optional<Playlist> existing = playlistRepository.findByNameIgnoreCase(request.getName().trim());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Playlist with name '" + request.getName() + "' already exists");
        }

        Playlist playlist = Playlist.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .coverUrl(request.getCoverUrl())
                .build();

        Playlist saved = playlistRepository.save(playlist);
        return mapToDto(saved);
    }

    @Transactional
    public PlaylistDto updatePlaylist(Long id, UpdatePlaylistRequest request) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));

        playlist.setName(request.getName().trim());
        if (request.getDescription() != null) {
            playlist.setDescription(request.getDescription());
        }

        Playlist updated = playlistRepository.save(playlist);
        return mapToDto(updated);
    }

    @Transactional
    public PlaylistDto addSongToPlaylist(Long playlistId, AddSongToPlaylistRequest request) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));

        Song song = songRepository.findById(request.getSongId())
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + request.getSongId()));

        Optional<PlaylistItem> existingItem = playlistItemRepository.findByPlaylistIdAndSongId(playlistId, request.getSongId());
        if (existingItem.isPresent()) {
            throw new DuplicateResourceException("Song is already in this playlist");
        }

        int nextPosition = playlist.getItems().size() + 1;

        PlaylistItem item = PlaylistItem.builder()
                .playlist(playlist)
                .song(song)
                .position(nextPosition)
                .build();

        playlist.addItem(item);
        Playlist saved = playlistRepository.save(playlist);
        return mapToDto(saved);
    }

    @Transactional
    public PlaylistDto removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));

        PlaylistItem item = playlistItemRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song with id " + songId + " is not in playlist"));

        playlist.removeItem(item);
        playlistItemRepository.delete(item);

        Playlist saved = playlistRepository.save(playlist);
        return mapToDto(saved);
    }

    @Transactional
    public void deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));
        playlistRepository.delete(playlist);
    }

    public PlaylistDto mapToDto(Playlist playlist) {
        List<PlaylistItemDto> itemDtos = playlist.getItems().stream()
                .map(item -> PlaylistItemDto.builder()
                        .id(item.getId())
                        .song(songService.mapToDto(item.getSong()))
                        .addedAt(item.getAddedAt())
                        .position(item.getPosition())
                        .build())
                .collect(Collectors.toList());

        String cover = playlist.getCoverUrl();
        if (cover == null && !playlist.getItems().isEmpty() && playlist.getItems().get(0).getSong().getAlbum() != null) {
            cover = playlist.getItems().get(0).getSong().getAlbum().getCoverUrl();
        }

        return PlaylistDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .coverUrl(cover)
                .createdAt(playlist.getCreatedAt())
                .updatedAt(playlist.getUpdatedAt())
                .items(itemDtos)
                .totalSongs(itemDtos.size())
                .build();
    }
}
