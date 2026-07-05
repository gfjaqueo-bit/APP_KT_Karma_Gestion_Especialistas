package cl.giancarlo.especialista.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// "object" en Kotlin crea un singleton: una sola instancia en toda la app,
// creada perezosamente ("by lazy") la primera vez que se accede a especialistaApiService.
object RetrofitClient {

    // 10.0.2.2 es la IP con la que el emulador ve a tu PC — nunca localhost (ver nota del espacio, sección 8).
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val especialistaApiService: EspecialistaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EspecialistaApiService::class.java)
    }
}