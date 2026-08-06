# AuraStream Mobile — Smart Audio Streaming System 🎵🎧

Open-source smart audio streaming system designed for high-performance audio delivery and playlist management. Features a **Spring Boot 3.x REST & HTTP Byte-Range Audio Streaming API** on the backend and a **Native Android Application** in Kotlin with **Jetpack Compose** and **AndroidX Media3 / ExoPlayer** on the mobile client.

## Project Structure

```
├── aurastream-backend/    → REST & HTTP Streaming API (Java 17, Spring Boot 3.2)
└── aurastream-mobile/     → Android Client (Kotlin, Jetpack Compose, Media3)
```

## Backend — Spring Boot REST & Audio Streaming API

### Stack
- Java 17 · Spring Boot 3.2 · Spring Data JPA · H2 Database (In-Memory Seed Data)
- Jakarta Validation · Lombok · JUnit 5 · Mockito

### Data Model

| Entity | Key Fields |
|---|---|
| `Song` | id, title, artist, album, durationSeconds, genre, audioUrl, playCount, bpm |
| `Artist` | id, name, genre, bio, imageUrl |
| `Album` | id, title, artist, coverUrl, releaseYear |
| `Playlist` | id, name, description, coverUrl, createdAt, updatedAt |
| `PlaylistItem` | id, playlist, song, addedAt, position |

### REST & Streaming Endpoints (OpenAPI Specification)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/songs` | List all songs in the catalog |
| `GET` | `/api/v1/songs/{id}` | Single song metadata by ID |
| `GET` | `/api/v1/songs/search?query=` | Real-time search by title, artist, or album |
| `GET` | `/api/v1/songs/recommended` | Smart recommendation algorithm (ranked by play count and BPM) |
| `POST` | `/api/v1/songs/{id}/play` | Increment song play count |
| `GET` | `/api/v1/songs/{id}/stream` | **HTTP Byte-Range Audio Streaming** (`206 Partial Content`) |
| `GET` | `/api/v1/playlists` | List all user playlists |
| `GET` | `/api/v1/playlists/{id}` | Get playlist details with ordered songs |
| `POST` | `/api/v1/playlists` | Create new playlist |
| `PUT` | `/api/v1/playlists/{id}` | Update playlist name/description |
| `POST` | `/api/v1/playlists/{id}/songs` | Add song to playlist (Duplicate protection enforced) |
| `DELETE` | `/api/v1/playlists/{id}/songs/{songId}` | Remove song from playlist |
| `DELETE` | `/api/v1/playlists/{id}` | Delete playlist |

### Features & Business Rules
- **HTTP 206 Partial Content Audio Streaming**: Supports `Range: bytes=X-Y` headers for smooth seeking and instant buffer loading in ExoPlayer.
- **Duplicate Prevention**: Rejects duplicate song additions with HTTP 409 Conflict.
- **Preloaded Seed Data**: Includes sample artists, albums, songs, and smart playlists ("Modo Enfoque", "Modo Relax").
- **Global Error Handling**: Standardized JSON exception responses via `@RestControllerAdvice`.

### Running the Backend

```bash
cd aurastream-backend
./mvnw spring-boot:run        # Starts server on port 8080
./mvnw test                   # Runs unit tests (JUnit 5 + Mockito)
```

H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:aurastreamdb`, User: `sa`).

---

## Mobile — Native Android App (Kotlin)

### Stack
- Kotlin · Jetpack Compose · Material Design 3 (Spotify Dark Theme `#121212` background, `#1DB954` Neon Green accent)
- AndroidX Media3 / ExoPlayer (Background audio playback & MediaSession integration)
- Retrofit 2 · OkHttp3 · Gson · Coroutines · StateFlow · MVVM + Clean Architecture

### UX Flows & Screen Modules

```
app/src/main/java/com/aurastream/mobile/
├── data/           → Retrofit service, DTOs, repository impl
├── domain/         → Domain models, repository interface, use cases
└── ui/
    ├── components/ → MiniPlayer, ExpandedPlayerModal, EmptyPlaylistState, Dialogs
    ├── home/       → Recommended Carousel, Smart Playlists, Recent songs
    ├── player/     → AudioPlayerManager (Media3 / ExoPlayer state machine)
    ├── playlists/  → Playlist Library, Empty State, Detail & Song Picker
    ├── search/     → Real-time search bar (Song, Artist, Genre)
    └── theme/      → Spotify Dark palette, Typography, Custom shapes
```

1. **Home / Dashboard Screen**:
   - "Recomendadas para ti" horizontal song carousel.
   - Quick access to Smart Playlists ("Modo Enfoque", "Modo Relax").
   - Recent songs list with 3-dot context menu.
2. **Playlist Management Module**:
   - **Empty State**: Shown when no playlists exist, with illustration, message ("Aún no tienes listas"), and "Crear Playlist" button.
   - **Create Playlist Dialog**: Modal to set playlist name and optional description.
   - **Playlist View**: Header cover, "Reproducir Todo" button, and "Agregar Canciones" picker modal with checkable songs.
   - **2-Way Song Addition**:
     - *In-playlist*: "Agregar canciones" button opening a song picker modal with checkable items.
     - *3-Dot Context Menu*: Tapping 3 dots on any song anywhere opens an "Agregar a Playlist" BottomSheet displaying user playlists.
3. **Mini-Player & Full Expanded Player**:
   - Persistent mini-player at the bottom with live playback progress bar and play/pause controls.
   - Expanded full-screen player with large artwork, interactive progress bar (SeekBar), shuffle, repeat, favorite (like) toggle, and 3-dot menu.
4. **Search Screen**:
   - Real-time search filtering songs by title, artist, or genre.

---

### Connecting App to Local Server

By default, Retrofit targets `http://10.0.2.2:8080/` (Android Studio Emulator loopback). For physical devices on the same Wi-Fi network, update `RetrofitClient.kt`:

```kotlin
RetrofitClient.setBaseUrl("http://<YOUR_LOCAL_IP>:8080/")
```

---

## License

MIT
