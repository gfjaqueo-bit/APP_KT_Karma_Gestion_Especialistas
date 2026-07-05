package cl.giancarlo.especialista.models

data class Especialista(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val especialidad: String,
    val email: String,
    val telefono: String,
    val modalidad: Modalidad,
    val tarifaHora: Int
)