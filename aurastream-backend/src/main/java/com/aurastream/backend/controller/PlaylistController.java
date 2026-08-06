package com.aurastream.backend.controller;

import com.aurastream.backend.dto.AddSongToPlaylistRequest;
import com.aurastream.backend.dto.CreatePlaylistRequest;
import com.aurastream.backend.dto.PlaylistDto;
import com.aurastream.backend.dto.UpdatePlaylistRequest;
import com.aurastream.backend.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<PlaylistDto>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getPlaylistById(id));
    }

    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(@Valid @RequestBody CreatePlaylistRequest request) {
        PlaylistDto created = playlistService.createPlaylist(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDto> updatePlaylist(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlaylistRequest request) {
        return ResponseEntity.ok(playlistService.updatePlaylist(id, request));
    }

    @PostMapping("/{id}/songs")
    public ResponseEntity<PlaylistDto> addSongToPlaylist(
            @PathVariable Long id,
            @Valid @RequestBody AddSongToPlaylistRequest request) {
        return ResponseEntity.ok(playlistService.addSongToPlaylist(id, request));
    }

    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<PlaylistDto> removeSongFromPlaylist(
            @PathVariable Long id,
            @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.removeSongFromPlaylist(id, songId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }
}
