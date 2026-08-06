package com.aurastream.mobile.ui.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.ui.components.AddSongsToPlaylistPickerModal
import com.aurastream.mobile.ui.components.ShimmerLoadingEffect
import com.aurastream.mobile.ui.components.SongItemRow
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant
import com.aurastream.mobile.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    viewModel: PlaylistViewModel,
    currentPlayingSong: Song?,
    onBackClick: () -> Unit,
    onSongClick: (Song, List<Song>) -> Unit,
    onSongOptionClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    val detailUiState by viewModel.detailUiState.collectAsState()
    var showAddSongsPicker by remember { mutableStateOf(false) }

    LaunchedEffect(playlistId) {
        viewModel.loadPlaylistDetail(playlistId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpotifyDark)
            )
        },
        containerColor = SpotifyDark
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = detailUiState) {
                is PlaylistDetailUiState.Loading -> ShimmerLoadingEffect()

                is PlaylistDetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = TextSecondary)
                    }
                }

                is PlaylistDetailUiState.Success -> {
                    val playlist = state.playlist
                    val songs = playlist.items.map { it.song }

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Playlist Header
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(160.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SpotifySurfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QueueMusic,
                                        contentDescription = null,
                                        tint = SpotifyGreen,
                                        modifier = Modifier.size(72.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = playlist.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                if (!playlist.description.isNull_or_blank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = playlist.description!!,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (songs.isNotEmpty()) {
                                        Button(
                                            onClick = { onSongClick(songs.first(), songs) },
                                            colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen)
                                        ) {
                                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = SpotifyDark)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Reproducir Todo", color = SpotifyDark, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    OutlinedButton(
                                        onClick = { showAddSongsPicker = true },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SpotifyGreen)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Agregar Canciones")
                                    }
                                }
                            }
                        }

                        // Empty State in detail screen
                        if (songs.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Esta playlist está vacía",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Presiona el botón de arriba para agregar canciones desde tu catálogo.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }
                        } else {
                            items(songs) { song ->
                                SongItemRow(
                                    song = song,
                                    isPlaying = currentPlayingSong?.id == song.id,
                                    onSongClick = { onSongClick(song, songs) },
                                    onOptionClick = onSongOptionClick
                                )
                            }
                        }
                    }

                    // Add Songs Picker Modal
                    if (showAddSongsPicker) {
                        AddSongsToPlaylistPickerModal(
                            availableSongs = state.availableSongs,
                            existingSongIds = songs.map { it.id }.toSet(),
                            onDismissRequest = { showAddSongsPicker = false },
                            onConfirmAddSongs = { songIds ->
                                songIds.forEach { songId ->
                                    viewModel.addSongToPlaylist(playlist.id, songId) { _, _ -> }
                                }
                                showAddSongsPicker = false
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun String?.isNull_or_blank(): Boolean = this == null || this.isBlank()
