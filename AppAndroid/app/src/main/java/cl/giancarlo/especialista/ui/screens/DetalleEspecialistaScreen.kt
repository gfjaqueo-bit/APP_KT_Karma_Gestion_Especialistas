package cl.giancarlo.especialista.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel
import cl.giancarlo.especialista.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleEspecialista(
    idEspecialista: Int,
    onEditar: (Int) -> Unit,
    onEliminado: () -> Unit,
    onVolver: () -> Unit,
    viewModel: EspecialistaViewModel = viewModel()
) {
    val detalleState by viewModel.detalleState.collectAsState()
    val eliminarState by viewModel.eliminarState.collectAsState()
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    LaunchedEffect(idEspecialista) {
        viewModel.obtenerEspecialista(idEspecialista)
    }

    LaunchedEffect(eliminarState) {
        if (eliminarState is UiState.Success) {
            viewModel.resetEliminarState()
            onEliminado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del especialista") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingInterno ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingInterno),
            contentAlignment = Alignment.Center
        ) {
            when (val estado = detalleState) {
                is UiState.Loading -> CircularProgressIndicator()

                is UiState.Success -> {
                    val especialista = estado.data
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        FilaDetalle("Nombre", "${especialista.nombre} ${especialista.apellido}")
                        FilaDetalle("Especialidad", especialista.especialidad)
                        FilaDetalle("Email", especialista.email)
                        FilaDetalle("Teléfono", especialista.telefono)
                        FilaDetalle("Modalidad", especialista.modalidad.name)
                        FilaDetalle("Tarifa por hora", "$${especialista.tarifaHora}")

                        Spacer(modifier = Modifier.height(24.dp))

                        if (eliminarState is UiState.Error) {
                            Text(
                                text = (eliminarState as UiState.Error).mensaje,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = { onEditar(especialista.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { mostrarDialogoConfirmacion = true },
                            enabled = eliminarState !is UiState.Loading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (eliminarState is UiState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            } else {
                                Text("Eliminar")
                            }
                        }
                    }
                }

                is UiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(estado.mensaje)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.obtenerEspecialista(idEspecialista) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoConfirmacion = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que quieres eliminar este especialista? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoConfirmacion = false
                    viewModel.eliminarEspecialista(idEspecialista)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoConfirmacion = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = etiqueta, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = valor, style = MaterialTheme.typography.bodyLarge)
    }
}