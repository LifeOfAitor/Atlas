package daw.developer.atlas.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JoinTripDialog(
    onDismiss: () -> Unit,
    onJoin: (String) -> Unit = {}
) {
    // Dialogo para introducir el codigo de invitacion.
    var inviteCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onJoin(inviteCode) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD7A57C))
            ) {
                Text(
                    text = "Unirme al viaje",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Unirse a un viaje",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B1B1B)
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color(0xFF8C8C8C)
                    )
                }
            }
        },
        text = {
            Column {
                Text(
                    text = "Ingresa el codigo de invitacion que te compartieron.",
                    fontSize = 12.sp,
                    color = Color(0xFF8C8C8C)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Codigo de invitacion",
                    fontSize = 12.sp,
                    color = Color(0xFF8C8C8C)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = inviteCode,
                    onValueChange = { inviteCode = it },
                    placeholder = { Text("Ej: A1B2C3", color = Color(0xFFB2A79C)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD97942),
                        unfocusedBorderColor = Color(0xFFE5DCD3),
                        cursorColor = Color(0xFF1B1B1B),
                        focusedTextColor = Color(0xFF1B1B1B),
                        unfocusedTextColor = Color(0xFF1B1B1B)
                    )
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFFDFBF8)
    )
}
