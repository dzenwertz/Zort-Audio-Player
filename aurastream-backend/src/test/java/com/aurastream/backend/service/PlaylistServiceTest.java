package com.aurastream.backend.service;

import com.aurastream.backend.dto.AddSongToPlaylistRequest;
import com.aurastream.backend.dto.CreatePlaylistRequest;
import com.aurastream.backend.dto.PlaylistDto;
import com.aurastream.backend.dto.SongDto;
import com.aurastream.backend.exception.DuplicateResourceException;
import com.aurastream.backend.exception.ResourceNotFoundException;
import com.aurastream.backend.model.Artist;
import com.aurastream.backend.model.Playlist;
import com.aurastream.backend.model.PlaylistItem;
import com.aurastream.backend.model.Song;
import com.aurastream.backend.repository.PlaylistItemRepository;
import com.aurastream.backend.repository.PlaylistRepository;
import com.aurastream.backend.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistItemRepository playlistItemRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private SongService songService;

    @InjectMocks
    private PlaylistService playlistService;

    private Playlist testPlaylist;
    private Song testSong;

    @BeforeEach
    void setUp() {
        testPlaylist = Playlist.builder()
                .id(1L)
                .name("Focus Beats")
                .description("Study tracks")
                .items(new ArrayList<>())
                .build();

        Artist artist = Artist.builder().id(1L).name("Lo-Fi Artist").build();
        testSong = Song.builder().id(5L).title("Study Track").artist(artist).build();
    }

    @Test
    @DisplayName("Should create playlist successfully")
    void testCreatePlaylist_Success() {
        CreatePlaylistRequest request = CreatePlaylistRequest.builder()
                .name("New Workout List")
                .description("Gym hype")
                .build();

        when(playlistRepository.findByNameIgnoreCase("New Workout List")).thenReturn(Optional.empty());
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> {
            Playlist p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        PlaylistDto result = playlistService.createPlaylist(request);

        assertNotNull(result);
        assertEquals("New Workout List", result.getName());
        assertEquals("Gym hype", result.getDescription());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when creating duplicate playlist name")
    void testCreatePlaylist_DuplicateName() {
        CreatePlaylistRequest request = CreatePlaylistRequest.builder().name("Focus Beats").build();

        when(playlistRepository.findByNameIgnoreCase("Focus Beats")).thenReturn(Optional.of(testPlaylist));

        assertThrows(DuplicateResourceException.class, () -> playlistService.createPlaylist(request));
        verify(playlistRepository, never()).save(any(Playlist.class));
    }

    @Test
    @DisplayName("Should add song to playlist successfully")
    void testAddSongToPlaylist_Success() {
        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder().songId(5L).build();

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        when(songRepository.findById(5L)).thenReturn(Optional.of(testSong));
        when(playlistItemRepository.findByPlaylistIdAndSongId(1L, 5L)).thenReturn(Optional.empty());
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));
        when(songService.mapToDto(testSong)).thenReturn(SongDto.builder().id(5L).title("Study Track").build());

        PlaylistDto result = playlistService.addSongToPlaylist(1L, request);

        assertNotNull(result);
        assertEquals(1, result.getTotalSongs());
        verify(playlistRepository).save(testPlaylist);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when adding duplicate song to playlist")
    void testAddSongToPlaylist_DuplicateSong() {
        AddSongToPlaylistRequest request = AddSongToPlaylistRequest.builder().songId(5L).build();
        PlaylistItem existingItem = PlaylistItem.builder().id(100L).playlist(testPlaylist).song(testSong).build();

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        when(songRepository.findById(5L)).thenReturn(Optional.of(testSong));
        when(playlistItemRepository.findByPlaylistIdAndSongId(1L, 5L)).thenReturn(Optional.of(existingItem));

        assertThrows(DuplicateResourceException.class, () -> playlistService.addSongToPlaylist(1L, request));
    }

    @Test
    @DisplayName("Should remove song from playlist")
    void testRemoveSongFromPlaylist_Success() {
        PlaylistItem item = PlaylistItem.builder().id(100L).playlist(testPlaylist).song(testSong).build();
        testPlaylist.addItem(item);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        when(playlistItemRepository.findByPlaylistIdAndSongId(1L, 5L)).thenReturn(Optional.of(item));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));

        PlaylistDto result = playlistService.removeSongFromPlaylist(1L, 5L);

        assertEquals(0, result.getTotalSongs());
        verify(playlistItemRepository).delete(item);
    }

    @Test
    @DisplayName("Should delete playlist by ID")
    void testDeletePlaylist_Success() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));

        playlistService.deletePlaylist(1L);

        verify(playlistRepository).delete(testPlaylist);
    }
}
