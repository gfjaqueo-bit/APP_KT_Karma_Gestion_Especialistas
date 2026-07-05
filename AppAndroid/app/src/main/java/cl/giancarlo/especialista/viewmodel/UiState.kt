package cl.giancarlo.especialista.viewmodel

// Estado genérico reutilizable para cualquier operación asíncrona (listar, obtener uno,
// crear, actualizar, eliminar). T es el tipo de dato que trae el caso de éxito.
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val mensaje: String) : UiState<Nothing>()
}