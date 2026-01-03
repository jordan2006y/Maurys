package com.example.maurys.presentation.security

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maurys.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PinScreen(
    onPinSuccess: () -> Unit,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    nameRecibido: String
) {
    var pinInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) } // Estado de error

    // --- CONFIGURACIÓN ---
    val maxPinLength = 6
    val correctPin = "123456" // (OJO: En producción esto debería venir de tus Preferencias guardadas)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Animación de sacudida (Shake) para el error
    val offsetX = remember { Animatable(0f) }

    // Función para vibrar
    fun vibrateError() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

    // Efecto: Verificar PIN automáticamente
    LaunchedEffect(pinInput) {
        if (pinInput.length == maxPinLength) {
            if (pinInput == correctPin) {
                isError = false
                onPinSuccess()
            } else {
                // PIN INCORRECTO
                isError = true
                vibrateError()

                // Animación de Shake
                launch {
                    offsetX.animateTo(
                        targetValue = 0f,
                        animationSpec = keyframes {
                            durationMillis = 400
                            0f at 0
                            (-20f) at 50
                            20f at 100
                            (-10f) at 150
                            10f at 200
                            0f at 400
                        }
                    )
                }

                // Esperar un momento para que el usuario vea el error rojo antes de borrar
                delay(1000)
                pinInput = ""
                isError = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono (Cambia a Rojo si hay error)
        Icon(
            imageVector = if (isError) Icons.Default.Lock else Icons.Default.Fingerprint,
            contentDescription = null,
            tint = if (isError) BeautyPink else MoneyGreen, // Rojo/Rosa si error, Verde si normal
            modifier = Modifier
                .size(60.dp)
                .offset(x = offsetX.value.dp) // Aplicar sacudida
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isError) "PIN Incorrecto" else "Bienvenido",
            style = MaterialTheme.typography.titleMedium,
            color = if (isError) BeautyPink else TextGray
        )
        Text(
            text = "Ingresa tu PIN",
            style = MaterialTheme.typography.headlineMedium,
            color = TextWhite,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- INDICADORES DE PIN (Puntos) ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.offset(x = offsetX.value.dp) // Sacudir también los puntos
        ) {
            repeat(maxPinLength) { index ->
                val isFilled = index < pinInput.length
                // Si hay error, todos se ponen rojos/rosas
                val color = when {
                    isError -> BeautyPink
                    isFilled -> MoneyGreen // Verde cuando escribes bien
                    else -> SalonSurfaceLight
                }

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // --- TECLADO NUMÉRICO ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9")
            )

            for (row in rows) {
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    for (num in row) {
                        PinButton(num) {
                            if (pinInput.length < maxPinLength && !isError) pinInput += num
                        }
                    }
                }
            }

            // Última fila (0 y Borrar)
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Spacer(modifier = Modifier.size(75.dp)) // Espacio vacío

                PinButton("0") {
                    if (pinInput.length < maxPinLength && !isError) pinInput += "0"
                }

                // Botón Borrar
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clickable {
                            if (pinInput.isNotEmpty() && !isError) pinInput = pinInput.dropLast(1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Backspace,
                        contentDescription = "Borrar",
                        tint = TextGray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PinButton(number: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(75.dp)
            .clip(CircleShape)
            .background(SalonSurface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = TextWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}