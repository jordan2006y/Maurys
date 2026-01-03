package com.example.maurys.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.maurys.presentation.components.GlassCard // <--- IMPORTANTE
import com.example.maurys.ui.theme.*

@Composable
fun CompleteProfileScreen(
    onContinue: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(MainBlack, MainBlue.copy(alpha = 0.2f), MainBlack))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "Configuración",
                style = MaterialTheme.typography.headlineMedium,
                color = MainWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp).align(Alignment.CenterHorizontally)
            )

            GlassCard {
                Text("¿Cómo se llama tu estética?", color = MainWhite, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Negocio") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MainBlue,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = MainWhite,
                        unfocusedTextColor = MainWhite,
                        focusedLabelColor = MainBlue,
                        unfocusedLabelColor = TextHint,
                        cursorColor = MainBlue,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onContinue(name) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue, disabledContainerColor = MainBlue.copy(alpha = 0.3f)),
                    enabled = name.isNotBlank()
                ) {
                    Text("Continuar", fontWeight = FontWeight.Bold, color = MainWhite)
                }
            }
        }
    }
}