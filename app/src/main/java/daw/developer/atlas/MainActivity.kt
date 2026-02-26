package daw.developer.atlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import daw.developer.atlas.ui.Create
import daw.developer.atlas.ui.dashboard.DashboardScreen
import daw.developer.atlas.ui.dashboard.components.JoinTripDialog
import daw.developer.atlas.ui.profile.ProfileScreen
import daw.developer.atlas.ui.theme.AtlasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtlasTheme {
                var showCreate by remember { mutableStateOf(false) }
                var showJoinDialog by remember { mutableStateOf(false) }
                var showProfile by remember { mutableStateOf(false) }

                when {
                    showCreate -> {
                        Create(
                            onNavigateHome = {
                                showCreate = false
                                showProfile = false
                            },
                            onNavigateProfile = {
                                showCreate = false
                                showProfile = true
                            }
                        )
                    }
                    showProfile -> {
                        ProfileScreen(
                            userName = "danel",
                            onNavigateHome = { showProfile = false },
                            onNavigateCreate = {
                                showProfile = false
                                showCreate = true
                            }
                        )
                    }
                    else -> {
                        DashboardScreen(
                            userName = "danel",
                            onCreateTrip = { showCreate = true },
                            onJoinTrip = { showJoinDialog = true },
                            onNavigateCreate = { showCreate = true },
                            onNavigateProfile = {
                                showProfile = true
                                showCreate = false
                            }
                        )
                    }
                }

                if (showJoinDialog && !showCreate && !showProfile) {
                    JoinTripDialog(
                        onDismiss = { showJoinDialog = false },
                        onJoin = { showJoinDialog = false }
                    )
                }
            }
        }
    }
}
