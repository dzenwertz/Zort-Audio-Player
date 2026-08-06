package com.aurastream.backend.service;

import com.aurastream.backend.dto.SongDto;
import com.aurastream.backend.exception.ResourceNotFoundException;
import com.aurastream.backend.model.Artist;
import com.aurastream.backend.model.Song;
import com.aurastream.backend.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    private Song testSong;
    private Artist testArtist;

    @BeforeEach
    void setUp() {
        testArtist = Artist.builder()
                .id(1L)
                .name("Synth Wave Band")
                .genre("Electronic")
                .build();

        testSong = Song.builder()
                .id(10L)
                .title("Midnight Drive")
                .artist(testArtist)
                .durationSeconds(200)
                .genre("Electronic")
                .playCount(100L)
                .bpm(120)
                .build();
    }

    @Test
    @DisplayName("Should return all songs mapped to DTOs")
    void testGetAllSongs() {
        when(songRepository.findAll()).thenReturn(List.of(testSong));

        List<SongDto> result = songService.getAllSongs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Midnight Drive", result.get(0).getTitle());
        assertEquals("Synth Wave Band", result.get(0).getArtistName());
    }

    @Test
    @DisplayName("Should return single song by ID")
    void testGetSongById_Success() {
        when(songRepository.findById(10L)).thenReturn(Optional.of(testSong));

        SongDto result = songService.getSongById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Midnight Drive", result.getTitle());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when song ID does not exist")
    void testGetSongById_NotFound() {
        when(songRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> songService.getSongById(99L));
    }

    @Test
    @DisplayName("Should filter songs by search query")
    void testSearchSongs() {
        when(songRepository.searchSongs("Midnight")).thenReturn(List.of(testSong));

        List<SongDto> result = songService.searchSongs("Midnight");

        assertEquals(1, result.size());
        assertEquals("Midnight Drive", result.get(0).getTitle());
    }

    @Test
    @DisplayName("Should increment play count of a song")
    void testIncrementPlayCount() {
        when(songRepository.findById(10L)).thenReturn(Optional.of(testSong));
        when(songRepository.save(any(Song.class))).thenAnswer(inv -> inv.getArgument(0));

        SongDto result = songService.incrementPlayCount(10L);

        assertEquals(101L, result.getPlayCount());
        verify(songRepository).save(testSong);
    }
}
