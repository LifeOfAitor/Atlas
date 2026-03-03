package daw.developer.atlas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Register(
    onNavigateBack: () -> Unit = {},
    onRegister: (username: String, email: String, password: String) -> Unit = { _, _, _ -> },
    registerError: String? = null
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf(registerError) }

    LaunchedEffect(registerError) {
        errorMessage = registerError
    }

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
        Spacer(modifier = Modifier.height(200.dp))
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
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electronico") },
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
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Repetir contraseña") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                errorMessage = when {
                    username.isBlank() -> "El usuario no puede estar vacío"
                    email.isBlank() -> "El correo no puede estar vacío"
                    password.isBlank() -> "La contraseña no puede estar vacía"
                    repeatPassword.isBlank() -> "Debe repetir la contraseña"
                    password != repeatPassword -> "Las contraseñas no coinciden"
                    !email.contains("@") -> "El correo debe ser válido"
                    else -> {
                        errorMessage = null
                        onRegister(username, email, password)
                        null
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222))
        ) {
            Text("CREAR CUENTA", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onNavigateBack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD97942),
                contentColor = Color.White
            )
        ) {
            Text("VOLVER AL LOGIN", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    Register(
        onNavigateBack = {},
        onRegister = { _, _, _ -> },
        registerError = null
    )
}