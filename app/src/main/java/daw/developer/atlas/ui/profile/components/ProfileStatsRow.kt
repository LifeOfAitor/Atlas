package daw.developer.atlas.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileStatsRow(
    countries: String,
    cities: String,
    trips: String,
    photos: String,
    modifier: Modifier = Modifier
) {
    // Tarjetas de estadisticas principales.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(label = "Paises", value = countries, modifier = Modifier.weight(1f))
        StatCard(label = "Ciudades", value = cities, modifier = Modifier.weight(1f))
        StatCard(label = "Viajes", value = trips, modifier = Modifier.weight(1f))
        StatCard(label = "Fotos", value = photos, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFFDFBF8), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF1B1B1B)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF8C8C8C)
        )
    }
}
