package cl.giancarlo.especialista.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.giancarlo.especialista.ui.components.TarjetaEspecialista
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel
import cl.giancarlo.especialista.viewmodel.UiState

// Entra desde el Menú -> tocar un especialista aquí lleva DIRECTO al Formulario en modo
// edición, sin pasar por el Detalle (a diferencia de "Listar todos", que sí pasa por Detalle).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarEspecialista(
    onSeleccionar: (Int) -> Unit,
    onVolver: () -> Unit,
    viewModel: EspecialistaViewModel = viewModel()
) {
    val estado by viewModel.listaState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEspecialistas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar especialista") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingInterno ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingInterno), contentAlignment = Alignment.Center) {
            when (val estadoActual = estado) {
                is UiState.Loading -> CircularProgressIndicator()

                is UiState.Success -> {
                    if (estadoActual.data.isEmpty()) {
                        Text("Todavía no hay especialistas registrados.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(estadoActual.data) { especialista ->
                                TarjetaEspecialista(especialista = especialista, onClick = { onSeleccionar(especialista.id) })
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(estadoActual.mensaje)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.cargarEspecialistas() }) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}