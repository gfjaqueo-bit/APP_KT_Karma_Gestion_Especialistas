package cl.giancarlo.especialista.data.repository

import cl.giancarlo.especialista.data.remote.RetrofitClient
import cl.giancarlo.especialista.data.remote.Requests.ActualizarEspecialistaRequest
import cl.giancarlo.especialista.data.remote.Requests.InsertarEspecialistaRequest
import cl.giancarlo.especialista.models.Especialista
import retrofit2.HttpException

// Interfaz única: declara el contrato Y lo implementa en su propio companion object,
// sin clase aparte. El ViewModel va a usar EspecialistaRepository.obtenerTodos(), etc.,
// como si llamara una función del "objeto" EspecialistaRepository directamente.
interface EspecialistaRepository {

    suspend fun obtenerTodos(): List<Especialista>
    suspend fun obtenerPorId(id: Int): Especialista
    suspend fun crear(request: InsertarEspecialistaRequest): Especialista
    suspend fun actualizar(request: ActualizarEspecialistaRequest): Especialista
    suspend fun eliminar(id: Int)
    suspend fun buscar(texto: String): List<Especialista>

    companion object Default : EspecialistaRepository {

        override suspend fun obtenerTodos(): List<Especialista> =
            RetrofitClient.especialistaApiService.obtenerTodos()

        override suspend fun obtenerPorId(id: Int): Especialista =
            RetrofitClient.especialistaApiService.obtenerPorId(id)

        override suspend fun crear(request: InsertarEspecialistaRequest): Especialista =
            RetrofitClient.especialistaApiService.crear(request)

        override suspend fun actualizar(request: ActualizarEspecialistaRequest): Especialista =
            RetrofitClient.especialistaApiService.actualizar(request)

        override suspend fun eliminar(id: Int) {
            // Response<Void> no lanza excepción sola si el HTTP falla (404, 500, etc.);
            // hay que revisar isSuccessful a mano y convertirlo en excepción nosotros mismos.
            val response = RetrofitClient.especialistaApiService.eliminar(id)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
        override suspend fun buscar(texto: String): List<Especialista> =
            RetrofitClient.especialistaApiService.buscar(texto)
            }
        }

