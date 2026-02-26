package daw.developer.atlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import daw.developer.atlas.ui.Login
import daw.developer.atlas.ui.dashboard.DashboardScreen
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
                val isLoggedIn by TCPConnection.connectedState.collectAsState()
                var loginError by remember { mutableStateOf<String?>(null) }
                var currentUser by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    CommandDecoder.deniedEvent = { args ->
                        loginError = args.reason
                    }
                    TCPConnection.connectedEvent = {
                        loginError = null
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
                                        TCPConnection.connect(
                                            InetAddress.getByName(host),
                                            port,
                                            username,
                                            password,
                                            false
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
                    DashboardScreen(
                        userName = currentUser.ifBlank { "usuario" },
                        onCreateTrip = { },
                        onJoinTrip = { },
                        onNavigateProfile = { },
                        onNavigateCreate = { }
                    )
                }
            }
        }
    }
}
