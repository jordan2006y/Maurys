package com.example.maurys.presentation.initial

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun InitialScreen(
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a Maurys", style = MaterialTheme.typography.headlineLarge)
        Button(onClick = { navigateToLogin() }) {
            Text("Ir a Login")
        }
    }
}