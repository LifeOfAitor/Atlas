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
import daw.developer.atlas.ui.Create
import daw.developer.atlas.ui.Login
import daw.developer.atlas.ui.dashboard.DashboardScreen
import daw.developer.atlas.ui.dashboard.components.JoinTripDialog
import daw.developer.atlas.ui.profile.ProfileScreen
import daw.developer.atlas.ui.theme.AtlasTheme
import daw.developer.atlas.ui.trip.TripDetailScreen
import daw.developer.atlas.ui.trip.TripGalleryScreen
import java.net.InetAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                var selectedTrip by remember { mutableStateOf<Trip?>(null) }
                var tripMembers by remember { mutableStateOf<List<String>>(emptyList()) }
                var awaitingTripMembers by remember { mutableStateOf(false) }

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
                        if (awaitingTripMembers) {
                            val members = payload
                                ?.split(",")
                                ?.mapNotNull { it.trim().takeIf { name -> name.isNotEmpty() } }
                                ?: emptyList()
                            tripMembers = if (members.isEmpty()) {
                                listOf(currentUser.ifBlank { "usuario" })
                            } else {
                                members
                            }
                            awaitingTripMembers = false
                        } else if (payload != null && count != null) {
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

                LaunchedEffect(currentScreen, selectedTrip?.id) {
                    if (currentScreen == "TripDetail") {
                        val tripId = selectedTrip?.id
                        if (!tripId.isNullOrBlank()) {
                            tripMembers = listOf(currentUser.ifBlank { "usuario" })
                            awaitingTripMembers = true
                            withContext(Dispatchers.IO) {
                                TCPConnection.send("GETTRIPMEMBERS:$tripId")
                            }
                        }
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
                                onNavigateCreate = { currentScreen = "Create" },
                                onTripClick = { trip ->
                                    selectedTrip = trip
                                    currentScreen = "TripDetail"
                                }
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
                                onNavigateHome = {
                                    selectedTrip = null
                                    currentScreen = "Dashboard"
                                },
                                onNavigateProfile = {
                                    selectedTrip = null
                                    currentScreen = "Profile"
                                },
                                onCreateTrip = {
                                    selectedTrip = null
                                    currentScreen = "Dashboard"
                                }
                            )
                        }
                        "TripDetail" -> {
                            val trip = selectedTrip
                            if (trip == null) {
                                LaunchedEffect(Unit) {
                                    currentScreen = "Dashboard"
                                }
                            } else {
                                TripDetailScreen(
                                    trip = trip,
                                    creatorName = currentUser.ifBlank { "usuario" },
                                    participants = if (tripMembers.isEmpty()) {
                                        listOf(currentUser.ifBlank { "usuario" })
                                    } else {
                                        tripMembers
                                    },
                                    onInviteTraveler = { username, tripId ->
                                        scope.launch(Dispatchers.IO) {
                                            TCPConnection.send("ADDUSERTOTRIP:$username:$tripId")
                                        }
                                    },
                                    onOpenGallery = {
                                        currentScreen = "TripGallery"
                                    },
                                    onBack = {
                                        selectedTrip = null
                                        currentScreen = "Dashboard"
                                    },
                                    onNavigateHome = {
                                        selectedTrip = null
                                        currentScreen = "Dashboard"
                                    },
                                    onNavigateCreate = {
                                        selectedTrip = null
                                        currentScreen = "Create"
                                    },
                                    onNavigateProfile = {
                                        selectedTrip = null
                                        currentScreen = "Profile"
                                    }
                                )
                            }
                        }
                        "TripGallery" -> {
                            val trip = selectedTrip
                            if (trip == null) {
                                LaunchedEffect(Unit) {
                                    currentScreen = "Dashboard"
                                }
                            } else {
                                TripGalleryScreen(
                                    trip = trip,
                                    onBack = { currentScreen = "TripDetail" }
                                )
                            }
                        }
                        else -> {
                            ProfileScreen(
                                userName = currentUser.ifBlank { "usuario" },
                                onNavigateHome = {
                                    selectedTrip = null
                                    currentScreen = "Dashboard"
                                },
                                onNavigateCreate = {
                                    selectedTrip = null
                                    currentScreen = "Create"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
