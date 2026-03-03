package daw.developer.atlas.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daw.developer.atlas.Trip
import daw.developer.atlas.ui.Footer
import daw.developer.atlas.ui.dashboard.components.ActionButtonsRow
import daw.developer.atlas.ui.dashboard.components.DashboardTopBar
import daw.developer.atlas.ui.dashboard.components.EmptyStateCard
import daw.developer.atlas.ui.theme.AtlasTheme

@Composable
fun DashboardScreen(
    userName: String,
    trips: List<Trip>,
    modifier: Modifier = Modifier,
    onCreateTrip: () -> Unit = {},
    onJoinTrip: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onNavigateCreate: () -> Unit = {},
    onTripClick: (Trip) -> Unit = {}
) {
    // Pantalla principal del dashboard (estado vacio).
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EF))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            DashboardTopBar(userName = userName)

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Mis viajes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1B1B)
                )

                ActionButtonsRow(
                    modifier = Modifier.fillMaxWidth(),
                    onJoinClick = onJoinTrip,
                    onCreateClick = onCreateTrip
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (trips.isEmpty()) {
                    EmptyStateCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        trips.forEach { trip ->
                            TripCard(trip = trip, onClick = { onTripClick(trip) })
                        }
                    }
                }
            }
        }

        // Footer con navegacion global.
        Footer(
            selected = "Inicio",
            onSelect = { label ->
                when (label) {
                    "Crear" -> onNavigateCreate()
                    "Perfil" -> onNavigateProfile()
                }
            },
            backgroundColor = Color(0xFFF7F3EF),
            selectedColor = Color(0xFFD97942),
            unselectedColor = Color(0xFF8C8C8C)
        )
    }
}

@Composable
private fun TripCard(trip: Trip, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFBF8))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = trip.name.ifBlank { "Viaje" }, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = trip.startDate.ifBlank { "Sin fecha" }, color = Color(0xFF8C8C8C), fontSize = 12.sp)
                Text(text = "-", color = Color(0xFF8C8C8C), fontSize = 12.sp)
                Text(text = trip.endDate.ifBlank { "Sin fecha" }, color = Color(0xFF8C8C8C), fontSize = 12.sp)
            }
            Text(text = trip.destination.ifBlank { "Sin destino" }, color = Color(0xFF1B1B1B))
            Text(text = trip.visibility, color = Color(0xFF8C8C8C), fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    AtlasTheme {
        DashboardScreen(
            userName = "danel",
            trips = listOf(
                Trip(
                    id = "preview",
                    name = "Roma",
                    startDate = "10/03/2026",
                    endDate = "15/03/2026",
                    destination = "Italia",
                    visibility = "Publico"
                )
            )
        )
    }
}
