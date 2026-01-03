package com.example.maurys.presentation.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maurys.ui.theme.*

@Composable
fun InitialScreen(
    navigateToLogin: () -> Unit // <--- ESTO SOLUCIONA EL ERROR EN NAVIGATION WRAPPER
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(MainBlack, MainBlue.copy(alpha = 0.4f), MainBlack))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("Maurys", color = MainWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Text("Tu negocio, bajo control", color = TextGray, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { navigateToLogin() }, // Llamamos a la función que nos pasó el Wrapper
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text("Comenzar", color = MainWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}