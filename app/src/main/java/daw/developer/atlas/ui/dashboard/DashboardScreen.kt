package daw.developer.atlas.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daw.developer.atlas.ui.Footer
import daw.developer.atlas.ui.dashboard.components.ActionButtonsRow
import daw.developer.atlas.ui.dashboard.components.DashboardTopBar
import daw.developer.atlas.ui.dashboard.components.EmptyStateCard
import daw.developer.atlas.ui.theme.AtlasTheme

@Composable
fun DashboardScreen(
    userName: String,
    modifier: Modifier = Modifier,
    onCreateTrip: () -> Unit = {},
    onJoinTrip: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onNavigateCreate: () -> Unit = {}
) {
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

                EmptyStateCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    AtlasTheme {
        DashboardScreen(userName = "danel")
    }
}
