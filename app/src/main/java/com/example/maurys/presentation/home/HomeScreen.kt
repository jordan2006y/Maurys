package com.example.maurys.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.maurys.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    navController: NavHostController // Necesitamos esto para navegar al POS
) {
    var userName by remember { mutableStateOf("Maurys") }
    val scrollState = rememberScrollState()

    // Cargar nombre del usuario
    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnSuccessListener { userName = it.getString("name") ?: "Maurys" }
        }
    }

    Scaffold(
        containerColor = DarkBackground, // Fondo Negro Puro (#000000)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState), // Hacemos la pantalla scrolleable
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. ENCABEZADO (Saludo y Salir)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hola, $userName", color = TextGray, fontSize = 14.sp)
                    Text("Resumen Diario", color = MainWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { auth.signOut() }) {
                    Icon(Icons.Rounded.Logout, contentDescription = "Salir", tint = Color.Red)
                }
            }

            // --- SECCIÓN 1: EL DASHBOARD PRINCIPAL (Estilo Pasos -> Ingresos) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface), // Gris Samsung (#1C1C1E)
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, null, tint = MainBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Ingresos de hoy", color = MainWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // El número grande (Meta del día)
                    Text(text = "S/ 320.00", color = MainBlue, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Meta diaria: S/ 500.00", color = TextGray, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    // El gráfico de la semana (D, L, M, M...) estilo Samsung Health
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val dias = listOf("D", "L", "M", "M", "J", "V", "S")
                        // Simulamos que Lunes y Miércoles cumplimos la meta
                        val diasCumplidos = listOf("L", "M")

                        dias.forEach { dia ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Círculo indicador
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            if (diasCumplidos.contains(dia)) MainBlue else Color.DarkGray,
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = dia, color = TextGray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // --- SECCIÓN 2: ACCIONES RÁPIDAS (Ex-Actividades) ---
            Text(text = "Registrar", color = MainWhite, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Gigante para COBRAR (Ir al POS)
                DashboardActionCard(
                    icon = Icons.Default.Add,
                    title = "Nueva Venta",
                    subtitle = "Ir a Caja",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("pos") } // Navega a la pantalla de cobro
                )

                // Botón para GASTOS
                DashboardActionCard(
                    icon = Icons.Default.AttachMoney,
                    title = "Registrar Gasto",
                    subtitle = "Pago servicios",
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO: Navegar a gastos */ }
                )
            }

            // --- SECCIÓN 3: ESTADÍSTICAS DEL MES ---
            Text(text = "Estadísticas", color = MainWhite, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(title = "Total Mes", value = "S/ 4,500", modifier = Modifier.weight(1f))
                InfoCard(title = "Clientes", value = "12", modifier = Modifier.weight(1f))
            }

            // Espacio extra al final para que no se corte con la navegación
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// --- COMPONENTES AUXILIARES (Estilo Samsung Health) ---

@Composable
fun DashboardActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MainBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MainBlue, modifier = Modifier.size(18.dp))
            }

            Column {
                Text(text = title, color = MainWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = TextGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = value, color = MainBlue, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = TextGray, fontSize = 12.sp)
        }
    }
}