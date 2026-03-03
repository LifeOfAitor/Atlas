package daw.developer.atlas.ui.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daw.developer.atlas.Trip
import daw.developer.atlas.ui.Footer
import daw.developer.atlas.ui.theme.AtlasTheme

private data class TripAction(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
data class TripStat(val value: String, val label: String)

@Composable
fun TripDetailScreen(
    trip: Trip,
    modifier: Modifier = Modifier,
    creatorName: String,
    participants: List<String> = listOf(creatorName),
    stats: List<TripStat> = listOf(
        TripStat("0", "Paises"),
        TripStat("0", "Ciudades"),
        TripStat("0", "Fotos")
    ),
    onInviteTraveler: (String, String) -> Unit = { _, _ -> },
    onAddStat: () -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onNavigateCreate: () -> Unit = {},
    onNavigateProfile: () -> Unit = {}
) {
    var showInviteDialog by remember { mutableStateOf(false) }
    var inviteUsername by remember { mutableStateOf("") }

    val actions = listOf(
        TripAction("Bandeja", Icons.Default.Email),
        TripAction("Galeria", Icons.Default.Add),
        TripAction("Cuentas", Icons.Default.Person),
        TripAction("Mapa", Icons.Default.Place)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EF))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color(0xFF1B1B1B))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = trip.name.ifBlank { "Nombre viaje" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B1B1B)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF8C8C8C), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${trip.startDate.ifBlank { "Sin fecha" }} - ${trip.endDate.ifBlank { "Sin fecha" }}",
                    color = Color(0xFF8C8C8C),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = trip.destination.ifBlank { "Sin destino" },
                color = Color(0xFF1B1B1B),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                actions.forEach { action ->
                    TripActionButton(action = action)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Viajeros", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B1B1B))
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                participants.forEach { name ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFBF8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF8C8C8C))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(name.ifBlank { creatorName }, color = Color(0xFF1B1B1B))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showInviteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDFBF8))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B1B1B))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invitar viajero", color = Color(0xFF1B1B1B))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Estadisticas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B1B1B))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                stats.forEach { stat ->
                    StatCard(stat = stat, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddStat,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDFBF8))
            ) {
                Text("+ Anadir estadistica", color = Color(0xFF1B1B1B))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Footer(
            selected = "",
            onSelect = { label ->
                when (label) {
                    "Inicio" -> onNavigateHome()
                    "Crear" -> onNavigateCreate()
                    "Perfil" -> onNavigateProfile()
                }
            },
            backgroundColor = Color(0xFFF7F3EF),
            selectedColor = Color(0xFFD97942),
            unselectedColor = Color(0xFF8C8C8C)
        )
    }

    if (showInviteDialog) {
        AlertDialog(
            onDismissRequest = { showInviteDialog = false },
            title = { Text("Invitar viajero") },
            text = {
                Column {
                    Text("Introduce el username que quieres anadir al viaje.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = inviteUsername,
                        onValueChange = { inviteUsername = it },
                        singleLine = true,
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val username = inviteUsername.trim()
                        if (username.isNotEmpty()) {
                            onInviteTraveler(username, trip.id)
                        }
                        inviteUsername = ""
                        showInviteDialog = false
                    }
                ) {
                    Text("Invitar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        inviteUsername = ""
                        showInviteDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TripActionButton(action: TripAction, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(78.dp)
            .border(1.dp, Color(0xFFE5DCD3), RoundedCornerShape(16.dp))
            .background(Color(0xFFFDFBF8), RoundedCornerShape(16.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(action.icon, contentDescription = action.label, tint = Color(0xFF1B1B1B))
        Spacer(modifier = Modifier.height(6.dp))
        Text(action.label, fontSize = 12.sp, color = Color(0xFF1B1B1B))
    }
}

@Composable
private fun StatCard(stat: TripStat, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFBF8))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stat.value, fontWeight = FontWeight.Bold, color = Color(0xFF1B1B1B))
            Text(stat.label, fontSize = 12.sp, color = Color(0xFF8C8C8C))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TripDetailScreenPreview() {
    AtlasTheme {
        TripDetailScreen(
            trip = Trip(
                id = "1",
                name = "Nombre Viaje",
                startDate = "07/03/2026",
                endDate = "13/07/2026",
                destination = "Milan",
                visibility = "public"
            ),
            creatorName = "Usuario1"
        )
    }
}
