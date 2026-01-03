// Archivo: app/src/main/java/com/example/maurys/presentation/home/HomeScreen.kt
package com.example.maurys.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.maurys.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavHostController, auth: FirebaseAuth) {
    // ESTADO DE EJEMPLO (Esto se reinicia al salir de la app)
    var ventasHoy by remember { mutableDoubleStateOf(0.0) }
    var clientesCount by remember { mutableIntStateOf(0) }
    val metaDiaria = 800.0 // Meta realista para ejemplo

    // Función auxiliar para simular venta
    fun agregarVenta(monto: Double) {
        ventasHoy += monto
        clientesCount += 1
    }

    Scaffold(
        containerColor = SalonBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Abrir menú completo */ },
                containerColor = BeautyPink, // Botón Rosa
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Nuevo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp) // Margen lateral cómodo
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. ENCABEZADO
            SalonHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // 2. EL ANILLO DE PROGRESO (Centro de atención)
            EarningsRing(current = ventasHoy, goal = metaDiaria)

            Spacer(modifier = Modifier.height(32.dp))

            // 3. ACCESOS RÁPIDOS (Botones grandes)
            Text(
                text = "Registrar Servicio",
                style = MaterialTheme.typography.titleMedium,
                color = TextGrayLight,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // CORTE (Azul/Cian para diferenciar)
                ServiceButton("Corte", 35.0, Icons.Default.ContentCut, Color(0xFF00E5FF)) { agregarVenta(35.0) }
                // TINTE (Rosa/Magenta - el servicio estrella)
                ServiceButton("Tinte", 120.0, Icons.Default.Brush, BeautyPink) { agregarVenta(120.0) }
                // PEINADO (Púrpura)
                ServiceButton("Peinado", 60.0, Icons.Default.Face, BeautyPurple) { agregarVenta(60.0) }
                // OTRO (Gris/Blanco)
                ServiceButton("Otro", 20.0, Icons.Default.MoreHoriz, Color.Gray) { agregarVenta(20.0) }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. TARJETAS DE DATOS (Métricas)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta de Clientes
                MetricCard(
                    label = "Clientes",
                    value = clientesCount.toString(),
                    icon = Icons.Default.People,
                    accentColor = BeautyPurple,
                    modifier = Modifier.weight(1f)
                )
                // Tarjeta de Promedio Ticket
                val promedio = if (clientesCount > 0) ventasHoy / clientesCount else 0.0
                MetricCard(
                    label = "Promedio",
                    value = "$${promedio.toInt()}",
                    icon = Icons.Default.TrendingUp,
                    accentColor = MoneyGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- COMPONENTES VISUALES ---

@Composable
fun SalonHeader() {
    val date = SimpleDateFormat("dd MMM, yyyy", Locale("es", "ES")).format(Date())
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Hola, Maury", color = TextWhite, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(date, color = TextGray, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.weight(1f))
        // Avatar con borde Rosa
        Box(
            modifier = Modifier
                .size(48.dp)
                .border(2.dp, BeautyPink, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(SalonSurfaceLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = TextWhite)
        }
    }
}

@Composable
fun EarningsRing(current: Double, goal: Double) {
    val progress = (current / goal).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000), label = "progress"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(260.dp)) {
        // Fondo del anillo (Gris oscuro sutil)
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = SalonSurfaceLight,
            strokeWidth = 20.dp,
            trackColor = SalonSurfaceLight,
        )
        // Progreso (Verde Dinero porque es Ingreso)
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = MoneyGreen,
            strokeWidth = 20.dp,
            strokeCap = StrokeCap.Round,
            trackColor = Color.Transparent,
        )

        // Texto Central
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Ganancia Hoy", color = TextGray, fontSize = 14.sp)
            Text(
                text = "$${current.toInt()}",
                color = TextWhite,
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp
            )
            // Barra de meta pequeña
            Text(
                text = "Meta: $${goal.toInt()}",
                color = BeautyPink, // Meta en Rosa para motivar
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ServiceButton(text: String, price: Double, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp) // Botones grandes
                .clip(RoundedCornerShape(22.dp)) // Forma "Squircle" moderna
                .background(SalonSurface)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MetricCard(label: String, value: String, icon: ImageVector, accentColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SalonSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, color = TextGray, fontSize = 13.sp)
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Text(value, color = TextWhite, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }
    }
}