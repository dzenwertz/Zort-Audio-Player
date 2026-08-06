package com.aurastream.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant
import com.aurastream.mobile.ui.theme.TextMuted

@Composable
fun SongItemRow(
    song: Song,
    isPlaying: Boolean,
    onSongClick: (Song) -> Unit,
    onOptionClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSongClick(song) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SpotifySurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = song.title.take(1),
                color = if (isPlaying) SpotifyGreen else MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPlaying) SpotifyGreen else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${song.artistName} • ${song.genre}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = formatDuration(song.durationSeconds),
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(end = 8.dp)
        )

        IconButton(onClick = { onOptionClick(song) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", mins, secs)
}
