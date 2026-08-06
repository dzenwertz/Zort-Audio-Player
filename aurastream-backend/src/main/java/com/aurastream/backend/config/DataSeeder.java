package com.aurastream.backend.config;

import com.aurastream.backend.model.*;
import com.aurastream.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    @Override
    public void run(String... args) {
        if (artistRepository.count() > 0) {
            return;
        }

        log.info("Seeding initial AuraStream music data...");

        // Artists
        Artist synthwaveArtist = artistRepository.save(Artist.builder()
                .name("Neon Horizon")
                .genre("Synthwave")
                .bio("Futuristic retrowave soundscapes and analog synths.")
                .imageUrl("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4")
                .build());

        Artist lofiArtist = artistRepository.save(Artist.builder()
                .name("Luna Chill")
                .genre("Lo-Fi Hip Hop")
                .bio("Relaxing vinyl beats for study and focus.")
                .imageUrl("https://images.unsplash.com/photo-1470225620780-dba8ba36b745")
                .build());

        Artist acousticArtist = artistRepository.save(Artist.builder()
                .name("Aria Vance")
                .genre("Indie Folk")
                .bio("Warm acoustic guitars and serene vocals.")
                .imageUrl("https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f")
                .build());

        // Albums
        Album album1 = albumRepository.save(Album.builder()
                .title("Cyberpunk Odyssey")
                .artist(synthwaveArtist)
                .coverUrl("https://images.unsplash.com/photo-1514525253161-7a46d19cd819")
                .releaseYear(2024)
                .build());

        Album album2 = albumRepository.save(Album.builder()
                .title("Midnight Study Sessions")
                .artist(lofiArtist)
                .coverUrl("https://images.unsplash.com/photo-1518609878373-06d740f60d8b")
                .releaseYear(2023)
                .build());

        Album album3 = albumRepository.save(Album.builder()
                .title("Golden Hour Echoes")
                .artist(acousticArtist)
                .coverUrl("https://images.unsplash.com/photo-1459749411175-04bf5292ceea")
                .releaseYear(2024)
                .build());

        // Songs
        Song s1 = songRepository.save(Song.builder()
                .title("Neon City Drive")
                .artist(synthwaveArtist)
                .album(album1)
                .durationSeconds(214)
                .genre("Synthwave")
                .playCount(1540L)
                .bpm(124)
                .build());

        Song s2 = songRepository.save(Song.builder()
                .title("Digital Rain")
                .artist(synthwaveArtist)
                .album(album1)
                .durationSeconds(198)
                .genre("Synthwave")
                .playCount(890L)
                .bpm(118)
                .build());

        Song s3 = songRepository.save(Song.builder()
                .title("Coffee & Study Beats")
                .artist(lofiArtist)
                .album(album2)
                .durationSeconds(165)
                .genre("Lo-Fi")
                .playCount(2300L)
                .bpm(85)
                .build());

        Song s4 = songRepository.save(Song.builder()
                .title("Rainy Window Waves")
                .artist(lofiArtist)
                .album(album2)
                .durationSeconds(182)
                .genre("Lo-Fi")
                .playCount(1780L)
                .bpm(80)
                .build());

        Song s5 = songRepository.save(Song.builder()
                .title("Sunset Acoustic Breeze")
                .artist(acousticArtist)
                .album(album3)
                .durationSeconds(245)
                .genre("Indie Folk")
                .playCount(1120L)
                .bpm(92)
                .build());

        // Smart Playlists
        Playlist focusPlaylist = Playlist.builder()
                .name("Modo Enfoque")
                .description("Música diseñada para maximizar tu concentración y productividad.")
                .coverUrl("https://images.unsplash.com/photo-1518609878373-06d740f60d8b")
                .build();
        focusPlaylist.addItem(PlaylistItem.builder().song(s3).position(1).build());
        focusPlaylist.addItem(PlaylistItem.builder().song(s4).position(2).build());
        playlistRepository.save(focusPlaylist);

        Playlist relaxPlaylist = Playlist.builder()
                .name("Modo Relax")
                .description("Melodías tranquilas para desconectar y descansar.")
                .coverUrl("https://images.unsplash.com/photo-1459749411175-04bf5292ceea")
                .build();
        relaxPlaylist.addItem(PlaylistItem.builder().song(s5).position(1).build());
        relaxPlaylist.addItem(PlaylistItem.builder().song(s1).position(2).build());
        playlistRepository.save(relaxPlaylist);

        log.info("AuraStream seed data populated successfully.");
    }
}
