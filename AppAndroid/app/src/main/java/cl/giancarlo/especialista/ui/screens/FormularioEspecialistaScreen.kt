package cl.giancarlo.especialista.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.giancarlo.especialista.data.remote.Requests.ActualizarEspecialistaRequest
import cl.giancarlo.especialista.data.remote.Requests.InsertarEspecialistaRequest
import cl.giancarlo.especialista.models.Modalidad
import cl.giancarlo.especialista.util.validation.EspecialistaValidator
import cl.giancarlo.especialista.util.validation.TarifaFormatter
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel
import cl.giancarlo.especialista.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioEspecialista(
    idEspecialista: Int?,
    onGuardadoExitoso: () -> Unit,
    onVolver: () -> Unit,
    viewModel: EspecialistaViewModel = viewModel()
) {
    val esEdicion = idEspecialista != null

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var modalidad by remember { mutableStateOf(Modalidad.PRESENCIAL) }
    var tarifaRaw by remember { mutableStateOf("") }      // fuente de verdad: solo dígitos, esto es lo que se valida y se envía
    var tarifaDisplay by remember { mutableStateOf("") }  // lo que se ve en el campo: formateado sin foco, plano con foco
    var menuModalidadExpandido by remember { mutableStateOf(false) }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorApellido by remember { mutableStateOf<String?>(null) }
    var errorEspecialidad by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorTelefono by remember { mutableStateOf<String?>(null) }
    var errorTarifa by remember { mutableStateOf<String?>(null) }

    // No se muestran errores hasta el primer intento de guardar, para no pintar
    // el formulario entero de rojo apenas se abre (mala UX en un formulario vacío).
    var intentoDeGuardar by remember { mutableStateOf(false) }

    val detalleState by viewModel.detalleState.collectAsState()
    val guardarState by viewModel.guardarState.collectAsState()

    LaunchedEffect(idEspecialista) {
        if (idEspecialista != null) {
            viewModel.obtenerEspecialista(idEspecialista)
        }
    }

    LaunchedEffect(detalleState) {
        val estado = detalleState
        if (esEdicion && estado is UiState.Success) {
            nombre = estado.data.nombre
            apellido = estado.data.apellido
            especialidad = estado.data.especialidad
            email = estado.data.email
            telefono = estado.data.telefono
            modalidad = estado.data.modalidad
            tarifaRaw = estado.data.tarifaHora.toString()
            tarifaDisplay = TarifaFormatter.formatear(tarifaRaw) // arranca sin foco -> se ve formateada
        }
    }

    LaunchedEffect(guardarState) {
        if (guardarState is UiState.Success) {
            viewModel.resetGuardarState()
            onGuardadoExitoso()
        }
    }

    fun validarTodo(): Boolean {
        errorNombre = EspecialistaValidator.validarNombre(nombre)
        errorApellido = EspecialistaValidator.validarApellido(apellido)
        errorEspecialidad = EspecialistaValidator.validarEspecialidad(especialidad)
        errorEmail = EspecialistaValidator.validarEmail(email)
        errorTelefono = EspecialistaValidator.validarTelefono(telefono)
        errorTarifa = EspecialistaValidator.validarTarifa(tarifaRaw)
        return listOf(errorNombre, errorApellido, errorEspecialidad, errorEmail, errorTelefono, errorTarifa)
            .all { it == null }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar especialista" else "Nuevo especialista") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it.take(35)
                    if (intentoDeGuardar) errorNombre = EspecialistaValidator.validarNombre(nombre)
                },
                label = { Text("Nombre") },
                isError = errorNombre != null,
                supportingText = { errorNombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = apellido,
                onValueChange = {
                    apellido = it.take(35)
                    if (intentoDeGuardar) errorApellido = EspecialistaValidator.validarApellido(apellido)
                },
                label = { Text("Apellido") },
                isError = errorApellido != null,
                supportingText = { errorApellido?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = especialidad,
                onValueChange = {
                    especialidad = it.take(35)
                    if (intentoDeGuardar) errorEspecialidad = EspecialistaValidator.validarEspecialidad(especialidad)
                },
                label = { Text("Especialidad") },
                isError = errorEspecialidad != null,
                supportingText = { errorEspecialidad?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.take(100)
                    if (intentoDeGuardar) errorEmail = EspecialistaValidator.validarEmail(email)
                },
                label = { Text("Email") },
                isError = errorEmail != null,
                supportingText = { errorEmail?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it.filter { c -> c.isDigit() }.take(10)
                    if (intentoDeGuardar) errorTelefono = EspecialistaValidator.validarTelefono(telefono)
                },
                label = { Text("Teléfono") },
                isError = errorTelefono != null,
                supportingText = { errorTelefono?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = menuModalidadExpandido,
                onExpandedChange = { menuModalidadExpandido = it }
            ) {
                OutlinedTextField(
                    value = modalidad.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Modalidad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuModalidadExpandido) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = menuModalidadExpandido,
                    onDismissRequest = { menuModalidadExpandido = false }
                ) {
                    Modalidad.entries.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.name) },
                            onClick = {
                                modalidad = opcion
                                menuModalidadExpandido = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tarifaDisplay,
                onValueChange = {
                    val soloDigitos = it.filter { c -> c.isDigit() }.take(6)
                    tarifaRaw = soloDigitos
                    tarifaDisplay = soloDigitos // mientras hay foco, se ve el número plano, sin $ ni puntos
                    if (intentoDeGuardar) errorTarifa = EspecialistaValidator.validarTarifa(tarifaRaw)
                },
                label = { Text("Tarifa por hora") },
                isError = errorTarifa != null,
                supportingText = { errorTarifa?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        tarifaDisplay = if (focusState.isFocused) {
                            tarifaRaw // al entrar a editar: solo el número
                        } else {
                            if (tarifaRaw.isNotBlank()) TarifaFormatter.formatear(tarifaRaw) else ""
                        }
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (guardarState is UiState.Error) {
                Text(
                    text = (guardarState as UiState.Error).mensaje,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    intentoDeGuardar = true
                    if (validarTodo()) {
                        val tarifa = tarifaRaw.toIntOrNull() ?: 0
                        if (esEdicion) {
                            viewModel.actualizarEspecialista(
                                ActualizarEspecialistaRequest(
                                    id = idEspecialista!!,
                                    nombre = nombre,
                                    apellido = apellido,
                                    especialidad = especialidad,
                                    email = email,
                                    telefono = telefono,
                                    modalidad = modalidad,
                                    tarifaHora = tarifa
                                )
                            )
                        } else {
                            viewModel.crearEspecialista(
                                InsertarEspecialistaRequest(
                                    nombre = nombre,
                                    apellido = apellido,
                                    especialidad = especialidad,
                                    email = email,
                                    telefono = telefono,
                                    modalidad = modalidad,
                                    tarifaHora = tarifa
                                )
                            )
                        }
                    }
                },
                enabled = guardarState !is UiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (guardarState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text(if (esEdicion) "Guardar cambios" else "Crear especialista")
                }
            }
        }
    }
}