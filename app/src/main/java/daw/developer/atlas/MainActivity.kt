package daw.developer.atlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import daw.developer.atlas.Trip
import daw.developer.atlas.ui.Create
import daw.developer.atlas.ui.Login
import daw.developer.atlas.ui.dashboard.DashboardScreen
import daw.developer.atlas.ui.dashboard.components.JoinTripDialog
import daw.developer.atlas.ui.profile.ProfileScreen
import daw.developer.atlas.ui.theme.AtlasTheme
import java.net.InetAddress
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtlasTheme {
                val scope = rememberCoroutineScope()
                var isLoggedIn by remember { mutableStateOf(false) }
                var loginError by remember { mutableStateOf<String?>(null) }
                var currentUser by remember { mutableStateOf("") }
                var currentScreen by remember { mutableStateOf("Dashboard") }
                var showJoinDialog by remember { mutableStateOf(false) }
                val trips = remember { mutableStateListOf<Trip>() }

                fun parseTrips(payload: String): List<Trip> {
                    if (payload.isBlank()) return emptyList()
                    return payload.split("|").mapNotNull { rawTrip ->
                        val parts = rawTrip.split(",")
                        if (parts.size < 6) return@mapNotNull null
                        Trip(
                            id = parts[0].trim(),
                            name = parts[1].trim(),
                            startDate = parts[2].trim(),
                            endDate = parts[3].trim(),
                            destination = parts[4].trim(),
                            visibility = parts[5].trim()
                        )
                    }
                }

                LaunchedEffect(Unit) {
                    CommandDecoder.deniedEvent = { args ->
                        loginError = args.reason
                        isLoggedIn = false
                    }
                    CommandDecoder.successEvent = { args ->
                        val payload = args.payload
                        val count = args.count
                        if (payload != null && count != null) {
                            val parsedTrips = parseTrips(payload)
                            trips.clear()
                            trips.addAll(parsedTrips)
                        }
                    }
                    TCPConnection.connectedEvent = {
                        loginError = null
                        isLoggedIn = true
                        currentScreen = "Dashboard"
                    }
                    TCPConnection.disconnectedEvent = {
                        isLoggedIn = false
                        currentScreen = "Dashboard"
                    }
                }

                if (!isLoggedIn) {
                    Login(
                        loginError = loginError,
                        onLogin = { server, username, password ->
                            loginError = null
                            currentUser = username
                            scope.launch {
                                val parts = server.split(":")
                                if (parts.size == 2) {
                                    val host = parts[0]
                                    val port = parts[1].toIntOrNull()
                                    if (port != null) {
                                        TCPConnection.login(
                                            InetAddress.getByName(host),
                                            port,
                                            username,
                                            password
                                        )
                                    } else {
                                        loginError = "Puerto invalido"
                                    }
                                } else {
                                    loginError = "Formato IP:PUERTO"
                                }
                            }
                        },
                        onNavigateRegister = { }
                    )
                } else {
                    when (currentScreen) {
                        "Dashboard" -> {
                            DashboardScreen(
                                userName = currentUser.ifBlank { "usuario" },
                                trips = trips,
                                onCreateTrip = { currentScreen = "Create" },
                                onJoinTrip = { showJoinDialog = true },
                                onNavigateProfile = { currentScreen = "Profile" },
                                onNavigateCreate = { currentScreen = "Create" }
                            )

                            if (showJoinDialog) {
                                JoinTripDialog(
                                    onDismiss = { showJoinDialog = false },
                                    onJoin = { showJoinDialog = false }
                                )
                            }
                        }
                        "Create" -> {
                            Create(
                                onNavigateHome = { currentScreen = "Dashboard" },
                                onNavigateProfile = { currentScreen = "Profile" },
                                onCreateTrip = {
                                    currentScreen = "Dashboard"
                                }
                            )
                        }
                        else -> {
                            ProfileScreen(
                                userName = currentUser.ifBlank { "usuario" },
                                onNavigateHome = { currentScreen = "Dashboard" },
                                onNavigateCreate = { currentScreen = "Create" }
                            )
                        }
                    }
                }
            }
        }
    }
}
