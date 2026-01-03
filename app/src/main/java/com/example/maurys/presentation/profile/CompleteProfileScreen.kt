package com.example.maurys.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.maurys.presentation.components.GlassCard
import com.example.maurys.ui.theme.*

@Composable
fun CompleteProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para abrir galería (Solo imágenes)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Crea tu Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = TextWhite,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Para personalizar tu salón",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- SELECTOR DE IMAGEN ---
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .border(2.dp, BeautyPink, CircleShape) // Borde Neón
                .padding(4.dp)
                .clip(CircleShape)
                .background(SalonSurface)
                .clickable {
                    // Abrir galería
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null,
                        tint = BeautyPink,
                        modifier = Modifier.size(32.dp)
                    )
                    Text("Foto", color = TextGray, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- FORMULARIO EN CRISTAL ---
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del Salón / Estilista") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BeautyPink) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BeautyPink,
                    unfocusedBorderColor = TextGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedLabelColor = BeautyPink,
                    unfocusedLabelColor = TextGray
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Aquí guardarías en Firebase (Storage + Firestore)
                // Por ahora navegamos al Home
                navController.navigate("home") {
                    popUpTo("initial") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BeautyPink),
            shape = RoundedCornerShape(16.dp),
            enabled = name.isNotEmpty() // Solo habilita si hay nombre
        ) {
            Text("Guardar y Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}