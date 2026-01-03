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
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    val firestore = FirebaseFirestore.getInstance()

    NavHost(navController = navHostController, startDestination = "initial") {

        composable("initial") {
            InitialScreen(navigateToLogin = { navHostController.navigate("login") })
        }

        composable("login") {
            LoginScreen(auth = auth, navigateToHome = {
                // Al loguearse, vamos a completar perfil
                navHostController.navigate("complete_profile") { popUpTo("initial") { inclusive = true } }
            })
        }

        composable("complete_profile") {
            CompleteProfileScreen(onContinue = { name ->
                navHostController.navigate("security_pin/$name")
            })
        }

        composable(
            route = "security_pin/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            PinScreen(auth, firestore, name, onSavedSuccess = {
                navHostController.navigate("home") { popUpTo("login") { inclusive = true } }
            })
        }

        composable("home") {
            // Pasamos el navController para que pueda navegar al POS
            HomeScreen(auth = auth, navController = navHostController)

        }
    }
}