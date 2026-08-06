package com.aurastream.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant
import com.aurastream.mobile.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedPlayerModal(
    song: Song,
    isPlaying: Boolean,
    currentPositionMs: Long,
    durationMs: Long,
    isShuffle: Boolean,
    isRepeat: Boolean,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onDismissRequest: () -> Unit,
    onOptionClick: (Song) -> Unit
) {
    var isLiked by remember { mutableStateOf(song.isFavorite) }
    val progress = if (durationMs > 0) (currentPositionMs.toFloat() / durationMs.toFloat()) else 0f

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = SpotifyDark,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Cerrar")
                }
                Text(
                    text = "REPRODUCIENDO DE TU BIBLIOTECA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                IconButton(onClick = { onOptionClick(song) }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Big Album Artwork Card
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SpotifySurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = SpotifyGreen,
                    modifier = Modifier.size(96.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title & Artist with Like Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = song.artistName,
                        fontSize = 16.sp,
                        color = TextSecondary
                    )
                }

                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Me gusta",
                        tint = if (isLiked) SpotifyGreen else TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Progress Bar / Slider
            Slider(
                value = progress,
                onValueChange = { newFraction ->
                    onSeekTo((newFraction * durationMs).toLong())
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.onBackground,
                    activeTrackColor = SpotifyGreen,
                    inactiveTrackColor = SpotifySurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMs(currentPositionMs),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Text(
                    text = formatMs(durationMs),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Playback Controls Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleShuffle) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Aleatorio",
                        tint = if (isShuffle) SpotifyGreen else TextSecondary
                    )
                }

                IconButton(onClick = onPrevClick) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Anterior",
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier
                        .size(64.dp)
                        .background(SpotifyGreen, shape = RoundedCornerShape(32.dp))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                        tint = SpotifyDark,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onNextClick) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Siguiente",
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onToggleRepeat) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Repetir",
                        tint = if (isRepeat) SpotifyGreen else TextSecondary
                    )
                }
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val totalSecs = ms / 1000
    val mins = totalSecs / 60
    val secs = totalSecs % 60
    return String.format("%d:%02d", mins, secs)
}
