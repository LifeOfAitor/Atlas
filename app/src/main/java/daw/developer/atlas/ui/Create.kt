package daw.developer.atlas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import daw.developer.atlas.TCPConnection
import daw.developer.atlas.Trip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create(
    onNavigateHome: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onCreateTrip: (Trip) -> Unit = {}
) {
    // Formulario visual para crear un viaje (solo UI).
    var tripName by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf("friends") }
    val scope = rememberCoroutineScope()

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    val startPickerState = rememberDatePickerState()
    val endPickerState = rememberDatePickerState()

    fun formatDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EF))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text("Crear", color = Color(0xFF1B1B1B), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2E8DE))
                ) {
                    Text("Nuevo Viaje", color = Color(0xFF1B1B1B))
                }
                TextButton(onClick = {}) {
                    Text("Subida rapida", color = Color(0xFF8C8C8C))
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color(0xFFE5DCD3), RoundedCornerShape(12.dp))
                    .background(Color(0xFFFDFBF8))
                    .clickable { /* subir foto */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFD97942), modifier = Modifier.size(32.dp))
                    Text("Anadir foto de portada", color = Color(0xFF8C8C8C))
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = tripName,
                onValueChange = { tripName = it },
                label = { Text("Nombre del Viaje", color = Color(0xFF8C8C8C)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE5DCD3),
                    unfocusedBorderColor = Color(0xFFE5DCD3),
                    cursorColor = Color(0xFF1B1B1B),
                    focusedLabelColor = Color(0xFF1B1B1B),
                    unfocusedLabelColor = Color(0xFF8C8C8C),
                    focusedTextColor = Color(0xFF1B1B1B),
                    unfocusedTextColor = Color(0xFF1B1B1B)
                )
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { },
                    label = { Text("Fecha de inicio", color = Color(0xFF8C8C8C)) },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showStartPicker = true }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha de inicio")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE5DCD3),
                        unfocusedBorderColor = Color(0xFFE5DCD3),
                        cursorColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFF1B1B1B),
                        unfocusedLabelColor = Color(0xFF8C8C8C),
                        focusedTextColor = Color(0xFF1B1B1B),
                        unfocusedTextColor = Color(0xFF1B1B1B)
                    )
                )
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { },
                    label = { Text("Fin del Viaje", color = Color(0xFF8C8C8C)) },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showEndPicker = true }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha de fin")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE5DCD3),
                        unfocusedBorderColor = Color(0xFFE5DCD3),
                        cursorColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFF1B1B1B),
                        unfocusedLabelColor = Color(0xFF8C8C8C),
                        focusedTextColor = Color(0xFF1B1B1B),
                        unfocusedTextColor = Color(0xFF1B1B1B)
                    )
                )
            }
            if (showStartPicker) {
                DatePickerDialog(
                    onDismissRequest = { showStartPicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            startPickerState.selectedDateMillis?.let { millis ->
                                startDate = formatDate(millis)
                            }
                            showStartPicker = false
                        }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartPicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = startPickerState)
                }
            }
            if (showEndPicker) {
                DatePickerDialog(
                    onDismissRequest = { showEndPicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            endPickerState.selectedDateMillis?.let { millis ->
                                endDate = formatDate(millis)
                            }
                            showEndPicker = false
                        }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndPicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = endPickerState)
                }
            }
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino", color = Color(0xFF8C8C8C)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE5DCD3),
                    unfocusedBorderColor = Color(0xFFE5DCD3),
                    cursorColor = Color(0xFF1B1B1B),
                    focusedLabelColor = Color(0xFF1B1B1B),
                    unfocusedLabelColor = Color(0xFF8C8C8C),
                    focusedTextColor = Color(0xFF1B1B1B),
                    unfocusedTextColor = Color(0xFF1B1B1B)
                )
            )
            Spacer(Modifier.height(16.dp))
            Text("¿Quien puede ver el viaje?", color = Color(0xFF8C8C8C), fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val options = listOf(
                    "Privado" to "private",
                    "Seguidores" to "friends",
                    "Publico" to "public"
                )
                options.forEach { (label, value) ->
                    Button(
                        onClick = { visibility = value },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (visibility == value) Color(0xFFF2E8DE) else Color(0xFFFDFBF8),
                            contentColor = if (visibility == value) Color(0xFFD97942) else Color(0xFF8C8C8C)
                        ),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    ) {
                        Text(label)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDFBF8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Invitar amigos", color = Color(0xFF8C8C8C))
            }
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = {
                    val newTrip = Trip(
                        id = UUID.randomUUID().toString(),
                        name = tripName.trim(),
                        startDate = startDate,
                        endDate = endDate,
                        destination = destination.trim(),
                        visibility = visibility
                    )
                    onCreateTrip(newTrip)
                    scope.launch(Dispatchers.IO) {
                        try {
                            TCPConnection.send(
                                "NEWTRIP:${newTrip.id}:${newTrip.name}:${newTrip.startDate}:${newTrip.endDate}:${newTrip.destination}:${newTrip.visibility}"
                            )
                        } catch (e: Exception) {
                            TCPConnection.newLog("Error enviando viaje: ${e.message}", TCPConnection.LogType.ERROR)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD97942),
                    contentColor = Color.White
                )
            ) {
                Text("Crear Viaje")
            }
        }
        // Footer con navegacion global.
        Footer(
            selected = "Crear",
            onSelect = { label ->
                when (label) {
                    "Inicio" -> onNavigateHome()
                    "Perfil" -> onNavigateProfile()
                }
            },
            backgroundColor = Color(0xFFF7F3EF),
            selectedColor = Color(0xFFD97942),
            unselectedColor = Color(0xFF8C8C8C)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    Create()
}