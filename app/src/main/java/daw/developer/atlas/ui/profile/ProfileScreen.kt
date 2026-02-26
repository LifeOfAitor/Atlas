package daw.developer.atlas.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import daw.developer.atlas.ui.Footer
import daw.developer.atlas.ui.profile.components.LibraryTabsRow
import daw.developer.atlas.ui.profile.components.PhotoGridPlaceholder
import daw.developer.atlas.ui.profile.components.ProfileHeader
import daw.developer.atlas.ui.profile.components.ProfileStatsRow
import daw.developer.atlas.ui.profile.components.VisitedMapPlaceholder

@Composable
fun ProfileScreen(
    userName: String,
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit = {},
    onNavigateCreate: () -> Unit = {}
) {
    var selectedLibraryTab by remember { mutableStateOf("Grid") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3EF))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            ProfileHeader(
                userName = userName,
                bio = "Viajero, cafe y fotos.",
                followers = "2.4k",
                following = "180",
                photos = "1.2k"
            )

            Spacer(modifier = Modifier.height(16.dp))
            ProfileStatsRow(
                countries = "27",
                cities = "84",
                trips = "32",
                photos = "1.2K"
            )

            Spacer(modifier = Modifier.height(16.dp))
            VisitedMapPlaceholder()

            Spacer(modifier = Modifier.height(16.dp))
            LibraryTabsRow(
                selected = selectedLibraryTab,
                onSelect = { selectedLibraryTab = it }
            )

            Spacer(modifier = Modifier.height(12.dp))
            PhotoGridPlaceholder()

            Spacer(modifier = Modifier.height(16.dp))
        }

        Footer(
            selected = "Perfil",
            onSelect = { label ->
                when (label) {
                    "Inicio" -> onNavigateHome()
                    "Crear" -> onNavigateCreate()
                }
            },
            backgroundColor = Color(0xFFF7F3EF),
            selectedColor = Color(0xFFD97942),
            unselectedColor = Color(0xFF8C8C8C)
        )
    }
}

