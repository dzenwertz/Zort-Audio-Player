package com.aurastream.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongsToPlaylistPickerModal(
    availableSongs: List<Song>,
    existingSongIds: Set<Long>,
    onDismissRequest: () -> Unit,
    onConfirmAddSongs: (List<Long>) -> Unit
) {
    val selectedSongIds = remember { mutableStateListOf<Long>() }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = SpotifyDark,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Seleccionar Canciones",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Marca las canciones que deseas agregar a la playlist",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.heightIn(max = 320.dp)) {
                items(availableSongs) { song ->
                    val isAlreadyInPlaylist = existingSongIds.contains(song.id)
                    val isChecked = selectedSongIds.contains(song.id)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = song.title,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isAlreadyInPlaylist) TextSecondary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${song.artistName} • ${song.genre}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        if (isAlreadyInPlaylist) {
                            Text(
                                text = "Agregada",
                                style = MaterialTheme.typography.labelMedium,
                                color = SpotifyGreen
                            )
                        } else {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    if (checked) selectedSongIds.add(song.id)
                                    else selectedSongIds.remove(song.id)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = SpotifyGreen)
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onConfirmAddSongs(selectedSongIds.toList()) },
                enabled = selectedSongIds.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen)
            ) {
                Text(
                    text = "Agregar ${selectedSongIds.size} Canciones",
                    color = SpotifyDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
