package cl.giancarlo.especialista.data.remote.Requests

import cl.giancarlo.especialista.models.Modalidad;

// Espejo de InsertarEspecialista.java: sin id, porque lo genera la base de datos al crear.
data class InsertarEspecialistaRequest(
    val nombre: String,
    val apellido: String,
    val especialidad: String,
    val email: String,
    val telefono: String,
    val modalidad: Modalidad,
    val tarifaHora: Int
)