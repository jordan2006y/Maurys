package com.example.maurys.presentation.login

import android.app.Activity
import android.util.Log
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maurys.R
import com.example.maurys.presentation.components.GlassCard // <--- IMPORTANTE
import com.example.maurys.ui.theme.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // ID Cliente Web
    val webClientId = "837470663548-snqtdki4ifqfnq0kfh4d7g925o9r5bi3.apps.googleusercontent.com"

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    isLoading = true
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { authTask ->
                            isLoading = false
                            if (authTask.isSuccessful) navigateToHome()
                            else Toast.makeText(context, "Error: ${authTask.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                }
            } catch (e: ApiException) {
                isLoading = false
                Log.e("Login", "Error: ${e.statusCode}")
            }
        } else { isLoading = false }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(MainBlue.copy(alpha = 0.3f), MainBlack, MainBlack))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text("Maurys", style = MaterialTheme.typography.displayLarge, color = MainWhite, fontWeight = FontWeight.Bold)
            Text("Gestión Profesional", style = MaterialTheme.typography.titleMedium, color = MainBlue.copy(alpha = 0.8f), letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(60.dp))

            // Usamos el Componente Reutilizable
            GlassCard {
                Text("Bienvenida de nuevo", color = MainWhite, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(24.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = MainBlue)
                } else {
                    Button(
                        onClick = { isLoading = true; launcher.launch(googleSignInClient.signInIntent) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MainWhite),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Image(painter = painterResource(id = R.drawable.google), contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Continuar con Google", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}