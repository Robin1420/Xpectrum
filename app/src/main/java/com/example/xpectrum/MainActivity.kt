package com.example.xpectrum

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.xpectrum.ui.theme.XpectrumTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Importar BoletoScreen y pantallas de pasajeros y ticket info
import com.example.xpectrum.BoletoScreen
import com.example.xpectrum.TicketInfoScreen

// Actividad principal de la app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo edge-to-edge para la UI
        setContent {
            // Aplicamos el tema personalizado
            XpectrumTheme {
                // Controlador de navegación para cambiar de pantalla
                val navController = rememberNavController()
                // Definimos el grafo de navegación con dos pantallas
                NavHost(navController = navController, startDestination = "bienvenida") {
                    // Pantalla de bienvenida
                    composable("bienvenida") { BienvenidaScreen(navController) }
                    // Pantalla de lista de vuelos
                    composable("vuelos") { ListaVuelosScreen(navController) }
                    // Pantalla de boleto (QR)
                    composable("boleto") { BoletoScreen(navController) }
                    // Pantalla de pasajeros por código de vuelo
                    composable("pasajeros/{codigoVuelo}") { backStackEntry ->
                        val codigoVuelo = backStackEntry.arguments?.getString("codigoVuelo") ?: ""
                        ( codigoVuelo)
                    }
                    // Pantalla de detalle de boleto
                    composable(
                        "ticketInfo/{nombre}/{email}/{telefono}/{codigoVuelo}/{fechaReserva}/{fechaSalida}/{horaSalida}/{fechaLlegada}/{horaLlegada}/{precioUSD}/{precioPEN}/{tipoPago}"
                    ) { backStackEntry ->
                        TicketInfoScreen(
                            navController = navController,
                            nombre = backStackEntry.arguments?.getString("nombre"),
                            email = backStackEntry.arguments?.getString("email"),
                            telefono = backStackEntry.arguments?.getString("telefono"),
                            codigoVuelo = backStackEntry.arguments?.getString("codigoVuelo"),
                            fechaSalida = backStackEntry.arguments?.getString("fechaSalida"),
                            horaSalida = backStackEntry.arguments?.getString("horaSalida"),
                            fechaLlegada = backStackEntry.arguments?.getString("fechaLlegada"),
                            horaLlegada = backStackEntry.arguments?.getString("horaLlegada"),
                            precioUSD = backStackEntry.arguments?.getString("precioUSD")?.toDoubleOrNull(),
                            precioPEN = backStackEntry.arguments?.getString("precioPEN")?.toDoubleOrNull(),
                            tipoPago = backStackEntry.arguments?.getString("tipoPago"),
                            fechaReserva = backStackEntry.arguments?.getString("fechaReserva")
                        )
                    }
                }
            }
        }
    }
}

// Pantalla de bienvenida con el diseño de la imagen
@Composable
fun BienvenidaScreen(navController: NavHostController) {
    // Definición del degradado de fondo
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF30393E),  // #30393e
            Color(0xFF495053),  // #495053
            Color(0xFF656869),  // #656869
            Color(0xFF8B8D86)   // #8b8d86
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 40.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Ilustración (usando un Box con fondo blanco y esquinas redondeadas)
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(200.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(0.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                // Aquí iría la imagen del logo/ilustración
                // Por ahora mostramos un texto de marcador de posición
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de Xpectrum",

                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(20.dp)),
                )
            }

            // Título de bienvenida
            Text(
                text = "¡Bienvenido a Xpectrum!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 60.dp, bottom = 16.dp)
            )

            // Subtítulo
            Text(
                text = "Tu aplicación para gestionar vuelos y\nescanear boletos de forma rápida y segura",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // Botón de continuar
            Button(
                onClick = { navController.navigate("vuelos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2A3C4F),  // Color #2A3C4F
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Continuar",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// Pantalla que muestra la lista de vuelos obtenidos desde la API
@Composable
fun ListaVuelosScreen(navController: NavHostController) {
    val context = LocalContext.current
    // CoroutineScope para lanzar tareas asíncronas
    val scope = rememberCoroutineScope()
    // Estado que almacena la lista de vuelos
    var vuelos by remember { mutableStateOf<List<Vuelo>>(emptyList()) }
    // Estado de carga
    var cargando by remember { mutableStateOf(true) }
    // Estado para mensajes de error
    var error by remember { mutableStateOf<String?>(null) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF30393E),  // #30393e
            Color(0xFF495053),  // #495053
            Color(0xFF656869),  // #656869
            Color(0xFF8B8D86)   // #8b8d86
        ),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Button(
                    onClick = { navController.navigate("boleto") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A3C4F),  // Color #2A3C4F
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Continuar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Transparent)
        ) {
            LaunchedEffect(Unit) {
                cargando = true
                error = null
                try {
                    vuelos = obtenerVuelos()
                } catch (e: Exception) {
                    error = "Error al cargar vuelos"
                } finally {
                    cargando = false
                }
            }
            // Indicador de carga
            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                // Muestra el mensaje de error si ocurrió alguno
                Text(error ?: "", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            } else if (vuelos.isEmpty()) {
                // Si la lista está vacía, mostramos un mensaje
                Text(
                    "No se encontraron vuelos. Verifica la respuesta de la API en Logcat.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                // Mostrar mensaje cuando no hay vuelos
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron vuelos",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // Contenedor principal con GIF fijo y lista de vuelos desplazable
                Box(modifier = Modifier.fillMaxSize()) {
                    // GIF fijo en la parte superior
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Cargar y mostrar el GIF local usando Coil con soporte para GIF
                        val context = LocalContext.current
                        val imageLoader = ImageLoader.Builder(context)
                            .components {
                                if (Build.VERSION.SDK_INT >= 28) {
                                    add(ImageDecoderDecoder.Factory())
                                } else {
                                    add(GifDecoder.Factory())
                                }
                            }
                            .build()
                            
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context)
                                    .data(R.drawable.vuelos)
                                    .decoderFactory(GifDecoder.Factory())
                                    .build(),
                                imageLoader = imageLoader
                            ),
                            contentDescription = "Animación de avión volando",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                        // Superponer un degradado oscuro para mejor legibilidad del texto
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0x80000000)
                                        ),
                                        startY = 0f,
                                        endY = Float.POSITIVE_INFINITY
                                    )
                                )
                        )
                        // Título sobre la imagen
                        Text(
                            text = "Vuelos\nDisponibles",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                letterSpacing = 1.2.sp
                            ),
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(24.dp)
                        )
                    }

                    // Lista de vuelos desplazable debajo del GIF
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 216.dp) // Ajustar según la altura del GIF + padding
                    ) {
                        items(vuelos) { vuelo ->
                            VueloItem(vuelo = vuelo)
                        }
                    }
                }
            }
        }
    }
}

// Componente que muestra los datos de un vuelo en una tarjeta
@Composable
fun VueloItem(vuelo: Vuelo) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2C3E50), Color(0xFF1A2530))
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Tarjeta del vuelo
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = gradient)
                    .padding(16.dp)
            ) {
                // Código del vuelo
                Text(
                    text = "Vuelo - ${vuelo.codigoVuelo}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Fecha y hora de salida
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.flight_takeoff),
                        contentDescription = "Hora de salida",
                        modifier = Modifier.size(20.dp).padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = "${vuelo.fechaSalidaFormateada} a las ${vuelo.horaSalida}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }

                // Fecha y hora de llegada
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.flight_land),
                        contentDescription = "Hora de llegada",
                        modifier = Modifier.size(20.dp).padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = "${vuelo.fechaLlegadaFormateada} a las ${vuelo.horaLlegada}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }

                // Origen (aeropuerto y país)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.multiple_airports),
                        contentDescription = "Origen",
                        modifier = Modifier.size(16.dp).padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = "${vuelo.aeropuertoOrigen} (${vuelo.paisOrigen})",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }

                // Estado del vuelo con un chip
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(
                            color = when (vuelo.estadoVuelo.lowercase()) {
                                "disponible" -> Color(0xFF4CAF50)
                                "retrasado" -> Color(0xFFFF9800)
                                "cancelado" -> Color(0xFFF44336)
                                else -> Color(0xFF9E9E9E)
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = vuelo.estadoVuelo.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}


// Vista previa para la pantalla de bienvenida (solo para diseño en el IDE)
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun BienvenidaPreview() {
    XpectrumTheme {
        // No navigation en preview
        BienvenidaScreen(navController = androidx.navigation.compose.rememberNavController())
    }
}