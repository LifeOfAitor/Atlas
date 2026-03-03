package daw.developer.atlas.ui.trip

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import coil.compose.AsyncImage
import daw.developer.atlas.Trip
import daw.developer.atlas.ui.profile.components.PhotoGridPlaceholder
import daw.developer.atlas.ui.theme.AtlasTheme
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.aspectRatio

@Composable
fun TripGalleryScreen(
    trip: Trip,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPhotoSelected: (Uri) -> Unit = {},
    uploadedUrl: String? = null,
    photoUrls: List<String> = emptyList()
) {
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedPhotoUri = uri
            onPhotoSelected(uri)
        }
    }

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
            onClick = {
                photoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDFBF8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B1B1B))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Subir foto", color = Color(0xFF1B1B1B))
        }

        if (selectedPhotoUri != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Foto seleccionada",
                color = Color(0xFF8C8C8C),
                fontSize = 12.sp
            )
        }

        if (!uploadedUrl.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Subida: $uploadedUrl",
                color = Color(0xFF8C8C8C),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Fotos conjuntas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B1B1B))
        Spacer(modifier = Modifier.height(12.dp))

        if (photoUrls.isEmpty()) {
            Text(
                text = "Sin fotos todavia",
                color = Color(0xFF8C8C8C),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            PhotoGridPlaceholder(modifier = Modifier.fillMaxWidth())
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(photoUrls, key = { it }) { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Foto del viaje",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFDFBF8))
                    )
                }
            }
        }

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
