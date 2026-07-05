package cl.giancarlo.especialista.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.giancarlo.especialista.models.Especialista
import cl.giancarlo.especialista.ui.components.TarjetaEspecialista
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel
import cl.giancarlo.especialista.viewmodel.UiState

// Entra desde el Menú -> cada tarjeta trae su propio ícono de eliminar (con confirmación),
// sin pasar por Detalle ni por Formulario.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEliminarEspecialista(
    onVolver: () -> Unit,
    viewModel: EspecialistaViewModel = viewModel()
) {
    val estado by viewModel.listaState.collectAsState()
    val eliminarState by viewModel.eliminarState.collectAsState()
    var especialistaAEliminar by remember { mutableStateOf<Especialista?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarEspecialistas()
    }

    LaunchedEffect(eliminarState) {
        if (eliminarState is UiState.Success) {
            viewModel.resetEliminarState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar especialista") },
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
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (eliminarState is UiState.Error) {
                                Text(
                                    text = (eliminarState as UiState.Error).mensaje,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(estadoActual.data) { especialista ->
                                    TarjetaEspecialista(
                                        especialista = especialista,
                                        onClick = { especialistaAEliminar = especialista },
                                        accionTrailing = {
                                            IconButton(onClick = { especialistaAEliminar = especialista }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    )
                                }
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

    especialistaAEliminar?.let { especialista ->
        AlertDialog(
            onDismissRequest = { especialistaAEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que quieres eliminar a ${especialista.nombre} ${especialista.apellido}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarEspecialista(especialista.id)
                    especialistaAEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { especialistaAEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}