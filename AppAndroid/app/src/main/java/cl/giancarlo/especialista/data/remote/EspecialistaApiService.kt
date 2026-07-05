package cl.giancarlo.especialista.data.remote

import cl.giancarlo.especialista.data.remote.Requests.ActualizarEspecialistaRequest
import cl.giancarlo.especialista.data.remote.Requests.InsertarEspecialistaRequest
import cl.giancarlo.especialista.models.Especialista
import retrofit2.Response
import retrofit2.http.*

interface EspecialistaApiService {

    @GET("api/especialistas")
    suspend fun obtenerTodos(): List<Especialista>

    @GET("api/especialistas/{id}")
    suspend fun obtenerPorId(@Path("id") id: Int): Especialista

    @POST("api/especialistas")
    suspend fun crear(@Body request: InsertarEspecialistaRequest): Especialista

    // El id viaja dentro del body (ActualizarEspecialista.id), sin {id} en la ruta,
    // espejo exacto de como quedó el PUT en el backend.
    @PUT("api/especialistas")
    suspend fun actualizar(@Body request: ActualizarEspecialistaRequest): Especialista

    // Response<Void>: solo nos interesa el código HTTP, no el mensaje de texto plano del body.
    @DELETE("api/especialistas/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Void>

    @GET("api/especialistas/buscar")
    suspend fun buscar(@Query("texto") texto: String): List<Especialista>
}