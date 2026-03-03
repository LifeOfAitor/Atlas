package daw.developer.atlas.ui.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daw.developer.atlas.Trip
import daw.developer.atlas.ui.profile.components.PhotoGridPlaceholder
import daw.developer.atlas.ui.theme.AtlasTheme

@Composable
fun TripGalleryScreen(
    trip: Trip,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onUploadPhoto: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EF))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color(0xFF1B1B1B))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Galeria",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF1B1B1B)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = trip.name.ifBlank { "Nombre viaje" },
            color = Color(0xFF8C8C8C),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUploadPhoto,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDFBF8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B1B1B))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Subir foto", color = Color(0xFF1B1B1B))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Fotos conjuntas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B1B1B))
        Spacer(modifier = Modifier.height(12.dp))

        PhotoGridPlaceholder(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TripGalleryScreenPreview() {
    AtlasTheme {
        TripGalleryScreen(
            trip = Trip(
                id = "1",
                name = "Nombre Viaje",
                startDate = "07/03/2026",
                endDate = "13/07/2026",
                destination = "Milan",
                visibility = "public"
            )
        )
    }
}
