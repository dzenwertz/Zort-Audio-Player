package com.aurastream.mobile.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.ui.components.ShimmerLoadingEffect
import com.aurastream.mobile.ui.components.SongItemRow
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant
import com.aurastream.mobile.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    currentPlayingSong: Song?,
    onSongClick: (Song, List<Song>) -> Unit,
    onSongOptionClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Buscar",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { viewModel.onQueryChanged(it) },
                    placeholder = { Text("Canciones, artistas o álbumes...", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = TextSecondary) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpotifyGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = SpotifySurfaceVariant,
                        unfocusedContainerColor = SpotifySurfaceVariant
                    )
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
                is SearchUiState.Loading -> ShimmerLoadingEffect()

                is SearchUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = TextSecondary)
                    }
                }

                is SearchUiState.Success -> {
                    if (state.songs.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No se encontraron resultados para '$query'",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.songs) { song ->
                                SongItemRow(
                                    song = song,
                                    isPlaying = currentPlayingSong?.id == song.id,
                                    onSongClick = { onSongClick(song, state.songs) },
                                    onOptionClick = onSongOptionClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
