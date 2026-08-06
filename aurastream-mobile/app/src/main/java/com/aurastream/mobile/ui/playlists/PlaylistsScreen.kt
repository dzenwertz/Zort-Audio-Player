package com.aurastream.mobile.ui.playlists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.aurastream.mobile.ui.components.CreatePlaylistDialog
import com.aurastream.mobile.ui.components.EmptyPlaylistState
import com.aurastream.mobile.ui.components.ShimmerLoadingEffect
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant
import com.aurastream.mobile.ui.theme.TextSecondary

@Composable
fun PlaylistsScreen(
    viewModel: PlaylistViewModel,
    onPlaylistClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tu Biblioteca",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Playlist", tint = SpotifyGreen)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = SpotifyGreen,
                contentColor = SpotifyDark
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Lista")
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
                is PlaylistsUiState.Loading -> ShimmerLoadingEffect()

                is PlaylistsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = TextSecondary)
                    }
                }

                is PlaylistsUiState.Success -> {
                    if (state.playlists.isEmpty()) {
                        EmptyPlaylistState(
                            onCreatePlaylistClick = { showCreateDialog = true }
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.playlists) { playlist ->
                                PlaylistItemRow(
                                    playlist = playlist,
                                    onClick = { onPlaylistClick(playlist) }
                                )
                            }
                        }
                    }
                }
            }

            if (showCreateDialog) {
                CreatePlaylistDialog(
                    onDismiss = { showCreateDialog = false },
                    onCreate = { name, desc ->
                        viewModel.createNewPlaylist(name, desc) { success, _ ->
                            showCreateDialog = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PlaylistItemRow(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SpotifySurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QueueMusic,
                contentDescription = null,
                tint = SpotifyGreen,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = playlist.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${playlist.totalSongs} canciones • ${playlist.description ?: "Lista de reproducción"}",
                fontSize = 14.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}
