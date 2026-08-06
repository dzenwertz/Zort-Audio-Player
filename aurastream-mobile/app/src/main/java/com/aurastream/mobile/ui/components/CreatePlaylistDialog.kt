package com.aurastream.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aurastream.mobile.ui.theme.SpotifyDark
import com.aurastream.mobile.ui.theme.SpotifyGreen
import com.aurastream.mobile.ui.theme.SpotifySurfaceVariant

@Composable
fun CreatePlaylistDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, description: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SpotifySurfaceVariant,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Nueva Lista de Reproducción",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorText = null
                    },
                    label = { Text("Nombre de la lista") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SpotifyGreen)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción (Opcional)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SpotifyGreen)
                )

                if (errorText != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorText!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorText = "Por favor ingresa un nombre"
                    } else {
                        onCreate(name, description.ifBlank { null })
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen)
            ) {
                Text("Crear", color = SpotifyDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
