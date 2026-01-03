package com.example.maurys.presentation.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.maurys.R
import com.example.maurys.presentation.components.GlassCard
import com.example.maurys.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    navController: NavController
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // --- CONFIGURACIÓN DE GOOGLE ---
    // Importante: R.string.default_web_client_id se genera automáticamente por el plugin de google-services
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Launcher para abrir la ventanita de Google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                // Autenticar en Firebase con la credencial de Google
                isLoading = true
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        isLoading = false
                        if (authTask.isSuccessful) {
                            // Éxito: Vamos a crear PIN o Perfil
                            // Podrías chequear si es usuario nuevo, pero por ahora vamos a completar perfil
                            navController.navigate("complete_profile") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Error Firebase: ${authTask.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: ApiException) {
                isLoading = false
                Toast.makeText(context, "Error Google: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            isLoading = false
        }
    }

    // --- UI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo o Título
            Text(
                text = "MAURYS",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = BeautyPink,
                letterSpacing = 2.sp
            )
            Text(
                text = "Gestión Profesional",
                style = MaterialTheme.typography.bodyLarge,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(60.dp))

            GlassCard {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Inicia sesión para acceder a tu salón",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = BeautyPink)
                } else {
                    // BOTÓN GOOGLE
                    Button(
                        onClick = {
                            isLoading = true
                            launcher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TextWhite, // Botón Blanco clásico de Google o...
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Necesitas un icono de google en drawable, o usar un texto simple si no tienes
                            // Icon(painterResource(id = R.drawable.ic_google), contentDescription = null, tint = Color.Unspecified)
                            Text(
                                text = "Continuar con Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Opcional: Separador
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Divider(modifier = Modifier.weight(1f), color = SalonSurfaceLight)
                        Text(" O ", color = TextGray, modifier = Modifier.padding(horizontal = 8.dp))
                        Divider(modifier = Modifier.weight(1f), color = SalonSurfaceLight)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón Secundario (Correo) - Opcional
                    OutlinedButton(
                        onClick = { /* Lógica de correo si quieres mantenerla */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SalonSurfaceLight),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Usar Correo", fontSize = 16.sp)
                    }
                }
            }
        }

        // Footer
        Text(
            text = "Protegido por seguridad bancaria",
            style = MaterialTheme.typography.labelSmall,
            color = TextGray.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}