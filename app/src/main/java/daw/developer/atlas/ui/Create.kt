package daw.developer.atlas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Create() {
    var tripName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf("Followers") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10131A))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text("Crear", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF232733))
                ) {
                    Text("Nuevo Viaje", color = Color.White)
                }
                TextButton(onClick = {}) {
                    Text("Subida rapida", color = Color(0xFFB0B3C6))
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color(0xFF232733), RoundedCornerShape(12.dp))
                    .background(Color(0xFF181B23))
                    .clickable { /* Acción para subir foto */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFB0B3C6), modifier = Modifier.size(32.dp))
                    Text("Añadir foto de portada", color = Color(0xFFB0B3C6))
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = tripName,
                onValueChange = { tripName = it },
                label = { Text("Nombre del Viaje", color = Color(0xFFB0B3C6)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF232733),
                    unfocusedBorderColor = Color(0xFF232733),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color(0xFFB0B3C6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripcion", color = Color(0xFFB0B3C6)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF232733),
                    unfocusedBorderColor = Color(0xFF232733),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color(0xFFB0B3C6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Fecha de inicio", color = Color(0xFFB0B3C6)) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF232733),
                        unfocusedBorderColor = Color(0xFF232733),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color(0xFFB0B3C6),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Fin del Viaje", color = Color(0xFFB0B3C6)) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF232733),
                        unfocusedBorderColor = Color(0xFF232733),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color(0xFFB0B3C6),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino", color = Color(0xFFB0B3C6)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF232733),
                    unfocusedBorderColor = Color(0xFF232733),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color(0xFFB0B3C6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.height(16.dp))
            Text("¿Quien puede ver el viaje?", color = Color(0xFFB0B3C6), fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val options = listOf("Privado", "Seguidores", "Publico")
                options.forEach { option ->
                    Button(
                        onClick = { visibility = option },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (visibility == option) Color(0xFF232733) else Color(0xFF181B23),
                            contentColor = if (visibility == option) Color(0xFFFFC107) else Color(0xFFB0B3C6)
                        ),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    ) {
                        Text(option)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF181B23)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Invitar amigos", color = Color(0xFFB0B3C6))
            }
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF8C3B),
                    contentColor = Color.White
                )
            ) {
                Text("Crear Viaje")
            }
        }
        Footer(selected = "Crear")
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    Create()
}