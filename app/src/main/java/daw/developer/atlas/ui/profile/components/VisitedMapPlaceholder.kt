package daw.developer.atlas.ui.profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VisitedMapPlaceholder(modifier: Modifier = Modifier) {
    // Mapa demo estilo "been" con continentes simplificados.
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Mapa de viajes",
            fontSize = 13.sp,
            color = Color(0xFF8C8C8C)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFFDFBF8), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(156.dp)) {
                val base = Color(0xFFECE3DA)
                val visited = Color(0xFFD97942)
                val outline = Color(0xFFE5DCD3)

                fun rect(x: Float, y: Float, w: Float, h: Float, color: Color) {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                    drawRoundRect(
                        color = outline,
                        topLeft = Offset(x, y),
                        size = Size(w, h),
                        cornerRadius = CornerRadius(10f, 10f),
                        style = Stroke(width = 1f)
                    )
                }

                val w = size.width
                val h = size.height

                // Continentes simplificados.
                rect(w * 0.04f, h * 0.18f, w * 0.28f, h * 0.22f, visited)
                rect(w * 0.18f, h * 0.48f, w * 0.14f, h * 0.28f, base)
                rect(w * 0.42f, h * 0.20f, w * 0.14f, h * 0.14f, visited)
                rect(w * 0.44f, h * 0.38f, w * 0.16f, h * 0.28f, base)
                rect(w * 0.60f, h * 0.18f, w * 0.30f, h * 0.26f, visited)
                rect(w * 0.72f, h * 0.56f, w * 0.16f, h * 0.16f, base)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color(0xFFF2E8DE), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "Demo", fontSize = 10.sp, color = Color(0xFF8C8C8C))
            }
        }
    }
}
