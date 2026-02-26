package daw.developer.atlas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import daw.developer.atlas.ui.theme.AtlasTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtlasTheme {
                MainContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AtlasTheme {
        MainContent()
    }
}

@Composable
fun MainContent() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var helbidea by remember { mutableStateOf("10.14.0.111:13000") }
    var erabiltzailea by remember { mutableStateOf("") }
    var pasahitza by remember { mutableStateOf("") }
    var log by remember { mutableStateOf("") }

    // Listener-ak konfiguratu
    LaunchedEffect(Unit) {
        TCPConnection.logSentEvent = { event ->
            log = "[${event.mota}] ${event.log}"
        }

        TCPConnection.connectedEvent = {
            log = "[INFO] Konektatuta!"
        }

        TCPConnection.disconnectedEvent = {
            log = "[INFO] Deskonektatuta"
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Zerbitzari Konexioa")

        TextField(
            value = helbidea,
            onValueChange = { helbidea = it }
        )
        TextField(
            value = erabiltzailea,
            onValueChange = { erabiltzailea = it }
        )
        TextField(
            value = pasahitza,
            onValueChange = { pasahitza = it }
        )

        Button(
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.INTERNET
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Konexioa korutina batean egin
                    scope.launch {
                        try {
                            val parts = helbidea.split(":")
                            if (parts.size == 2) {
                                TCPConnection.connect(
                                    InetAddress.getByName(parts[0]),
                                    parts[1].toInt(),
                                    erabiltzailea,
                                    pasahitza,
                                    false
                                )
                            } else {
                                log = "Helbide formatua: IP:PORTU"
                            }
                        } catch (e: Exception) {
                            log = "Errorea: ${e.message}"
                        }
                    }
                } else {
                    Toast.makeText(context, "Internet baimena behar da", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        ) {
            Text(text = "Konektatu")
        }

        Text(
            text = log,
            color = Color.Red
        )
    }
}