package daw.developer.atlas.ui
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Login(
    loginError: String?,
    onLogin: (server: String, username: String, password: String) -> Unit,
    onNavigateRegister: () -> Unit
) {
    var server by remember { mutableStateOf("10.14.0.111:13000") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(90.dp))

        Icon(
            painter = painterResource(android.R.drawable.ic_menu_camera),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = server,
            onValueChange = { server = it },
            label = { Text("Servidor IP:Puerto") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (!loginError.isNullOrBlank()) {
            Text(text = loginError, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = { onLogin(server, username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222))
        ) {
            Text("INICIAR SESION", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Crear una cuenta",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onNavigateRegister() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(
        loginError = null,
        onLogin = { _, _, _ -> },
        onNavigateRegister = {}
    )
}
