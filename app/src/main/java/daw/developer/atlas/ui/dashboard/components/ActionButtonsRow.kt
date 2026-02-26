package daw.developer.atlas.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButtonsRow(
    modifier: Modifier = Modifier,
    onJoinClick: () -> Unit = {},
    onCreateClick: () -> Unit = {}
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.End) {
        OutlinedButton(
            onClick = onJoinClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Unirme",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1B1B1B)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onCreateClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97942)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "+  Nuevo viaje",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
