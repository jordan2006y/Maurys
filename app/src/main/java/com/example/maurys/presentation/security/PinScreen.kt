package com.example.maurys.presentation.security

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maurys.presentation.components.GlassCard
import com.example.maurys.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PinScreen(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    nameRecibido: String,
    onSavedSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(MainBlue.copy(alpha = 0.25f), MainBlack),
                    radius = 900f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            GlassCard {
                Text(
                    "Seguridad",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MainWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Crea un PIN de 6 dígitos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextHint,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Input de PIN
                OutlinedTextField(
                    value = pin,
                    onValueChange = { newValue ->
                        if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                            pin = newValue
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.width(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        color = MainWhite,
                        fontSize = 32.sp,
                        letterSpacing = 8.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MainBlue,
                        unfocusedBorderColor = GlassBorder,
                        cursorColor = MainBlue,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = MainBlue)
                } else {
                    Button(
                        onClick = {
                            isLoading = true
                            val userId = auth.currentUser?.uid
                            val userEmail = auth.currentUser?.email ?: ""

                            if (userId != null) {
                                val userData = hashMapOf(
                                    "id" to userId,
                                    "name" to nameRecibido,
                                    "email" to userEmail,
                                    "pin" to pin,
                                    "role" to "admin",
                                    "createdAt" to System.currentTimeMillis()
                                )

                                firestore.collection("users").document(userId)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Log.d("PinScreen", "Usuario guardado con éxito")
                                        isLoading = false
                                        onSavedSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("PinScreen", "Error al guardar: ${e.message}")
                                        isLoading = false
                                        // Mostramos el error exacto en pantalla
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            } else {
                                isLoading = false
                                Toast.makeText(context, "Error: Sesión no válida", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainBlue,
                            disabledContainerColor = MainBlue.copy(alpha = 0.3f)
                        ),
                        enabled = pin.length == 6
                    ) {
                        Text("Finalizar Configuración", fontWeight = FontWeight.Bold, color = MainWhite)
                    }
                }
            }
        }
    }
}