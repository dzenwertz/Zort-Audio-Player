package com.aurastream.mobile.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.ui.components.ShimmerLoadingEffect
import com.aurastream.mobile.ui.components.SongItemRow
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant
import com.aurastream.mobile.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    currentPlayingSong: Song?,
    onSongClick: (Song, List<Song>) -> Unit,
    onSongOptionClick: (Song) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AuraStream",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpotifyGreen
                )
            }
        },
        containerColor = SpotifyDark
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> ShimmerLoadingEffect()

                is HomeUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = state.message, color = TextSecondary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadHomeData() },
                            colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen)
                        ) {
                            Text("Reintentar", color = SpotifyDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                is HomeUiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Recommended Carousel
                        item {
                            Column {
                                Text(
                                    text = "Recomendadas para ti",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(state.recommendedSongs) { song ->
                                        RecommendedSongCard(
                                            song = song,
                                            onClick = { onSongClick(song, state.recommendedSongs) }
                                        )
                                    }
                                }
                            }
                        }

                        // Smart Playlists Shortcuts
                        item {
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                Text(
                                    text = "Playlists Inteligentes",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    state.smartPlaylists.take(2).forEach { playlist ->
                                        SmartPlaylistCard(
                                            playlist = playlist,
                                            onClick = { onPlaylistClick(playlist) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        // Recent Tracks
                        item {
                            Text(
                                text = "Escuchadas Recientemente",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }

                        items(state.recentSongs) { song ->
                            SongItemRow(
                                song = song,
                                isPlaying = currentPlayingSong?.id == song.id,
                                onSongClick = { onSongClick(song, state.recentSongs) },
                                onOptionClick = onSongOptionClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendedSongCard(
    song: Song,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SpotifySurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SpotifyGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = SpotifyGreen,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artistName,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SmartPlaylistCard(
    playlist: Playlist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SpotifySurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SpotifyGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = SpotifyDark)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = playlist.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "${playlist.totalSongs} canciones", fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}
