package com.aurastream.mobile.data.repository

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.PlaylistItem
import com.aurastream.mobile.domain.model.Song

object MockData {
    val sampleSongs = listOf(
        Song(
            id = 1L,
            title = "Midnight Synthwave",
            artistId = 1L,
            artistName = "Aurawave Studio",
            albumId = 1L,
            albumTitle = "Neon Dreams Vol. 1",
            coverUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?q=80&w=600&auto=format&fit=crop",
            durationSeconds = 215,
            genre = "Electronic",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            playCount = 1420,
            bpm = 120
        ),
        Song(
            id = 2L,
            title = "Chill Lofi Beats",
            artistId = 2L,
            artistName = "Luna Eclipse",
            albumId = 2L,
            albumTitle = "Midnight Focus Session",
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?q=80&w=600&auto=format&fit=crop",
            durationSeconds = 180,
            genre = "Lo-Fi",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            playCount = 3890,
            bpm = 85
        ),
        Song(
            id = 3L,
            title = "Cyberpunk Pulse",
            artistId = 3L,
            artistName = "Neon Overdrive",
            albumId = 3L,
            albumTitle = "Futuristic Horizons",
            coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=600&auto=format&fit=crop",
            durationSeconds = 240,
            genre = "Synthpop",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            playCount = 2750,
            bpm = 128
        ),
        Song(
            id = 4L,
            title = "Acoustic Sunset",
            artistId = 4L,
            artistName = "Solar Horizon",
            albumId = 4L,
            albumTitle = "Unplugged Sessions",
            coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=600&auto=format&fit=crop",
            durationSeconds = 195,
            genre = "Acoustic",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            playCount = 980,
            bpm = 95
        ),
        Song(
            id = 5L,
            title = "Deep Space Ambient",
            artistId = 5L,
            artistName = "Starlight Echo",
            albumId = 5L,
            albumTitle = "Cosmic Journey",
            coverUrl = "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?q=80&w=600&auto=format&fit=crop",
            durationSeconds = 310,
            genre = "Ambient",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            playCount = 4210,
            bpm = 70
        )
    )

    val samplePlaylists = mutableListOf(
        Playlist(
            id = 1L,
            name = "Modo Enfoque",
            description = "Música electrónica y lo-fi suave para concentrarte",
            coverUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?q=80&w=600&auto=format&fit=crop",
            createdAt = "2026-08-06",
            items = listOf(
                PlaylistItem(1L, sampleSongs[0], "2026-08-06", 1),
                PlaylistItem(2L, sampleSongs[1], "2026-08-06", 2),
                PlaylistItem(3L, sampleSongs[4], "2026-08-06", 3)
            ),
            totalSongs = 3
        ),
        Playlist(
            id = 2L,
            name = "Modo Relax",
            description = "Canciones acústicas y ambient para relajarse",
            coverUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=600&auto=format&fit=crop",
            createdAt = "2026-08-06",
            items = listOf(
                PlaylistItem(4L, sampleSongs[3], "2026-08-06", 1),
                PlaylistItem(5L, sampleSongs[4], "2026-08-06", 2)
            ),
            totalSongs = 2
        )
    )
}
