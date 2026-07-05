package cl.giancarlo.especialista.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.giancarlo.especialista.models.Especialista
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel
import cl.giancarlo.especialista.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBusquedaEspecialistas(
    onVerDetalle: (Int) -> Unit,
    onVolver: () -> Unit,
    viewModel: EspecialistaViewModel = viewModel()
) {
    var texto by remember { mutableStateOf("") }
    val busquedaState by viewModel.busquedaState.collectAsState()

    fun ejecutarBusqueda() {
        if (texto.isNotBlank()) viewModel.buscarEspecialistas(texto)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar especialista") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingInterno ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingInterno).padding(16.dp)) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Nombre o apellido") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { ejecutarBusqueda() }),
                trailingIcon = {
                    IconButton(onClick = { ejecutarBusqueda() }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (val estado = busquedaState) {
                null -> Text("Escribe un nombre o apellido y presiona buscar.")
                is UiState.Loading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is UiState.Success -> {
                    if (estado.data.isEmpty()) {
                        Text("No se encontraron especialistas con ese nombre o apellido.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(estado.data) { especialista ->
                                TarjetaResultadoBusqueda(especialista, onClick = { onVerDetalle(especialista.id) })
                            }
                        }
                    }
                }
                is UiState.Error -> Text(estado.mensaje)
            }
        }
    }
}

@Composable
private fun TarjetaResultadoBusqueda(especialista: Especialista, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${especialista.nombre} ${especialista.apellido}", style = MaterialTheme.typography.titleMedium)
            Text(text = especialista.especialidad)
        }
    }
}