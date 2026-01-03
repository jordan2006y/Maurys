package com.example.maurys

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.maurys.presentation.home.HomeScreen
import com.example.maurys.presentation.initial.InitialScreen
import com.example.maurys.presentation.login.LoginScreen
import com.example.maurys.presentation.profile.CompleteProfileScreen
import com.example.maurys.presentation.security.PinScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth
) {
    val firestore = FirebaseFirestore.getInstance()

    // Lógica de inicio: Si hay usuario, al Home. Si no, al Initial.
    val startDestination = if (auth.currentUser != null) "home" else "initial"

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {

        // 1. Pantalla Inicial (Bienvenida)
        composable("initial") {
            InitialScreen(
                navigateToLogin = { navHostController.navigate("login") } //
            )
        }

        // 2. Login (Google / Correo)
        composable("login") {
            LoginScreen(
                auth = auth,
                navigateToHome = {
                    // AQUÍ ESTÁ LA CLAVE: Al loguearse, no vamos al home,
                    // vamos a configurar el perfil primero.
                    navHostController.navigate("complete_profile") {
                        // Borramos 'initial' y 'login' del historial para no volver atrás
                        popUpTo("initial") { inclusive = true }
                    }
                }
            )
        }

        // 3. Completar Perfil (Nombre del Negocio)
        composable("complete_profile") {
            CompleteProfileScreen(
                onContinue = { name ->
                    // Pasamos el nombre a la siguiente pantalla (PIN)
                    // Usamos una ruta segura
                    navHostController.navigate("security_pin/$name")
                }
            )
        }

        // 4. Configurar PIN
        composable(
            route = "security_pin/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            // Recuperamos el nombre, si viene vacío ponemos uno por defecto
            val name = backStackEntry.arguments?.getString("name") ?: "Mi Negocio"

            PinScreen(
                auth = auth,
                firestore = firestore,
                nameRecibido = name,
                onSavedSuccess = {
                    // Cuando se guarda el PIN y el Usuario en Firestore:
                    // Vamos al HOME y borramos todo el historial anterior.
                    navHostController.navigate("home") {
                        popUpTo(0) { inclusive = true } // Borra TODO el historial
                    }
                }
            )
        }

        // 5. Pantalla Principal
        composable("home") {
            HomeScreen(
                auth = auth,
                navController = navHostController // Pasamos el controller por si el Home necesita navegar
            )
        }
    }
}