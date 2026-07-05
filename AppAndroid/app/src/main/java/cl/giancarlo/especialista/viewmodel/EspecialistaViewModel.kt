package cl.giancarlo.especialista.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.giancarlo.especialista.data.remote.Requests.ActualizarEspecialistaRequest
import cl.giancarlo.especialista.data.remote.Requests.InsertarEspecialistaRequest
import cl.giancarlo.especialista.data.repository.EspecialistaRepository
import cl.giancarlo.especialista.models.Especialista
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Sin constructor con parámetros: EspecialistaRepository se llama directo por su companion,
// así que no hace falta ViewModelFactory — Compose instancia este ViewModel con su factory por defecto.
class EspecialistaViewModel : ViewModel() {

    private val _listaState = MutableStateFlow<UiState<List<Especialista>>>(UiState.Loading)
    val listaState: StateFlow<UiState<List<Especialista>>> = _listaState.asStateFlow()

    private val _detalleState = MutableStateFlow<UiState<Especialista>>(UiState.Loading)
    val detalleState: StateFlow<UiState<Especialista>> = _detalleState.asStateFlow()

    // null = "todavía no se intentó guardar" -> la UI lo usa para no mostrar nada hasta que el usuario presione Guardar.
    private val _guardarState = MutableStateFlow<UiState<Especialista>?>(null)
    val guardarState: StateFlow<UiState<Especialista>?> = _guardarState.asStateFlow()

    private val _eliminarState = MutableStateFlow<UiState<Unit>?>(null)
    val eliminarState: StateFlow<UiState<Unit>?> = _eliminarState.asStateFlow()

    // null = todavía no se ha hecho ninguna búsqueda -> la UI muestra el hint en vez de una lista vacía
    private val _busquedaState = MutableStateFlow<UiState<List<Especialista>>?>(null)
    val busquedaState: StateFlow<UiState<List<Especialista>>?> = _busquedaState.asStateFlow()




    fun cargarEspecialistas() {
        viewModelScope.launch {
            _listaState.value = UiState.Loading
            try {
                val especialistas = EspecialistaRepository.obtenerTodos()
                _listaState.value = UiState.Success(especialistas)
            } catch (e: IOException) {
                _listaState.value = UiState.Error("No se pudo conectar con el servidor. Verifica tu conexión.")
            } catch (e: HttpException) {
                _listaState.value = UiState.Error("Error del servidor (${e.code()}).")
            }
        }
    }

    fun obtenerEspecialista(id: Int) {
        viewModelScope.launch {
            _detalleState.value = UiState.Loading
            try {
                val especialista = EspecialistaRepository.obtenerPorId(id)
                _detalleState.value = UiState.Success(especialista)
            } catch (e: HttpException) {
                _detalleState.value = if (e.code() == 404) {
                    UiState.Error("No se encontró el especialista.")
                } else {
                    UiState.Error("Error del servidor (${e.code()}).")
                }
            } catch (e: IOException) {
                _detalleState.value = UiState.Error("No se pudo conectar con el servidor.")
            }
        }
    }

    fun crearEspecialista(request: InsertarEspecialistaRequest) {
        viewModelScope.launch {
            _guardarState.value = UiState.Loading
            try {
                val creado = EspecialistaRepository.crear(request)
                _guardarState.value = UiState.Success(creado)
                cargarEspecialistas() // refresca el listado para que se vea el nuevo registro
            } catch (e: HttpException) {
                _guardarState.value = UiState.Error("No se pudo guardar (${e.code()}).")
            } catch (e: IOException) {
                _guardarState.value = UiState.Error("No se pudo conectar con el servidor.")
            }
        }
    }

    fun actualizarEspecialista(request: ActualizarEspecialistaRequest) {
        viewModelScope.launch {
            _guardarState.value = UiState.Loading
            try {
                val actualizado = EspecialistaRepository.actualizar(request)
                _guardarState.value = UiState.Success(actualizado)
                cargarEspecialistas()
            } catch (e: HttpException) {
                _guardarState.value = if (e.code() == 404) {
                    UiState.Error("No se encontró el especialista a actualizar.")
                } else {
                    UiState.Error("No se pudo actualizar (${e.code()}).")
                }
            } catch (e: IOException) {
                _guardarState.value = UiState.Error("No se pudo conectar con el servidor.")
            }
        }
    }

    fun eliminarEspecialista(id: Int) {
        viewModelScope.launch {
            _eliminarState.value = UiState.Loading
            try {
                EspecialistaRepository.eliminar(id)
                _eliminarState.value = UiState.Success(Unit)
                cargarEspecialistas()
            } catch (e: HttpException) {
                _eliminarState.value = UiState.Error("No se pudo eliminar (${e.code()}).")
            } catch (e: IOException) {
                _eliminarState.value = UiState.Error("No se pudo conectar con el servidor.")
            }
        }
    }

    // La UI llama esto después de mostrar el resultado (ej. un Toast o Snackbar),
    // para que el mensaje de éxito/error no reaparezca si la pantalla se recompone.
    fun resetGuardarState() {
        _guardarState.value = null
    }

    fun resetEliminarState() {
        _eliminarState.value = null
    }

    fun buscarEspecialistas(texto: String) {
        viewModelScope.launch {
            _busquedaState.value = UiState.Loading
            try {
                val resultados = EspecialistaRepository.buscar(texto)
                _busquedaState.value = UiState.Success(resultados)
            } catch (e: IOException) {
                _busquedaState.value = UiState.Error("No se pudo conectar con el servidor.")
            } catch (e: HttpException) {
                _busquedaState.value = UiState.Error("Error del servidor (${e.code()}).")
            }
        }
    }

}