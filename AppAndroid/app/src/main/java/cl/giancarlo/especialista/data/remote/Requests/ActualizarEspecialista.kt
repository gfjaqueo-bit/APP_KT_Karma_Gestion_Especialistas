package cl.giancarlo.especialista.data.remote.Requests


import cl.giancarlo.especialista.models.Modalidad;

// Espejo de ActualizarEspecialista.java: el id SÍ va incluido, porque el backend
// lo espera dentro del body (PUT /api/especialistas sin {id} en la URL).
data class ActualizarEspecialistaRequest(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val especialidad: String,
    val email: String,
    val telefono: String,
    val modalidad: Modalidad,
    val tarifaHora: Int
)