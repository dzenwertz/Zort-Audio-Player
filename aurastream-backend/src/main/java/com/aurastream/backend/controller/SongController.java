package com.aurastream.backend.controller;

import com.aurastream.backend.dto.SongDto;
import com.aurastream.backend.service.AudioStreamService;
import com.aurastream.backend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SongController {

    private final SongService songService;
    private final AudioStreamService audioStreamService;

    @GetMapping
    public ResponseEntity<List<SongDto>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SongDto>> searchSongs(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(songService.searchSongs(query));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<SongDto>> getRecommendedSongs() {
        return ResponseEntity.ok(songService.getRecommendedSongs());
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<SongDto> incrementPlayCount(@PathVariable Long id) {
        return ResponseEntity.ok(songService.incrementPlayCount(id));
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<byte[]> streamAudio(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        return audioStreamService.streamAudio(rangeHeader);
    }
}
