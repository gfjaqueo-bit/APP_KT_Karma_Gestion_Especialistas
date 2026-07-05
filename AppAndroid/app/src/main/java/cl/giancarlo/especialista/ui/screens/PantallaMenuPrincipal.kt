package cl.giancarlo.especialista.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel
import cl.giancarlo.especialista.viewmodel.UiState

@Composable
fun PantallaMenuPrincipal(
    onListar: () -> Unit,
    onBuscar: () -> Unit,
    onAgregar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    viewModel: EspecialistaViewModel = viewModel()
) {
    val listaState by viewModel.listaState.collectAsState()

    // No hay endpoint de conteo aparte: se reutiliza el mismo listado que usa
    // la pantalla de Lista y se cuenta con .size.
    LaunchedEffect(Unit) {
        viewModel.cargarEspecialistas()
    }

    Scaffold { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp)
        ) {
            Text(
                text = "Centro Médico Karma",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sistema de gestión de las cuentas de los especialistas del centro médico Karma",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Especialistas registrados", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    when (val estado = listaState) {
                        is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        is UiState.Success -> Text(
                            text = estado.data.size.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        is UiState.Error -> Text(text = "—", style = MaterialTheme.typography.displaySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onListar, modifier = Modifier.fillMaxWidth()) {
                Text("Listar todos los especialistas")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBuscar, modifier = Modifier.fillMaxWidth()) {
                Text("Buscar especialista por nombre y apellido")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onAgregar, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar nuevo especialista")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onEditar, modifier = Modifier.fillMaxWidth()) {
                Text("Editar especialista")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onEliminar, modifier = Modifier.fillMaxWidth()) {
                Text("Eliminar especialista")
            }
        }
    }
}