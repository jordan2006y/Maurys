package com.example.maurys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.maurys.ui.theme.MaurysTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos Auth
        auth = Firebase.auth

        setContent {
            MaurysTheme {
                // 1. Aquí creamos el controlador UNICA VEZ
                val navHostController = rememberNavController()

                // 2. Se lo pasamos al Wrapper
                NavigationWrapper(
                    navHostController = navHostController,
                    auth = auth
                )
            }
        }
    }
}