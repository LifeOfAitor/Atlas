package daw.developer.atlas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Footer(
    selected: String = "",
    onSelect: (String) -> Unit = {},
    backgroundColor: Color = Color(0xFF181B23),
    selectedColor: Color = Color(0xFFFF8C3B),
    unselectedColor: Color = Color(0xFFB0B3C6)
) {
    val items = listOf(
        "Inicio" to Icons.Default.Home,
        "Mapa" to Icons.Default.Place,
        "Crear" to Icons.Default.Add,
        "Perfil" to Icons.Default.Person
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (label, icon) ->
            val isSelected = label == selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelect(label) }
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = if (isSelected) selectedColor else unselectedColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    label,
                    fontSize = 12.sp,
                    color = if (isSelected) selectedColor else unselectedColor
                )
                if (isSelected) {
                    Spacer(Modifier.height(2.dp))
                    Box(
                        Modifier
                            .height(2.dp)
                            .width(32.dp)
                            .background(selectedColor)
                    )
                } else {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview() {
    var selected by remember { mutableStateOf("") }
    Footer(selected = selected, onSelect = { selected = it })
}