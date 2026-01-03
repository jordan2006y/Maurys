package com.example.maurys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.maurys.ui.theme.MaurysTheme // Asegúrate que coincida con tu tema
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    // Inicializamos Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        setContent {
            MaurysTheme {
                // Creamos el controlador de navegación
                val navHostController = rememberNavController()

                // Llamamos a nuestro Wrapper
                NavigationWrapper(
                    navHostController = navHostController,
                    auth = auth
                )
            }
        }
    }
}