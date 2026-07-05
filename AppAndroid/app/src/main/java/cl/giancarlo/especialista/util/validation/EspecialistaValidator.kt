package cl.giancarlo.especialista.util.validation

// Cada función devuelve el mensaje de error a mostrar, o null si el campo es válido.
// Se hace así (en vez de un Boolean) para que la UI pueda mostrar el texto directo
// bajo el campo sin tener que mantener un mapa de mensajes aparte.
object EspecialistaValidator {

    private const val MAX_LARGO_TEXTO = 35
    private const val MAX_LARGO_EMAIL = 100
    private const val MAX_DIGITOS_TELEFONO = 10
    private const val MAX_DIGITOS_TARIFA = 6

    fun validarNombre(valor: String): String? =
        validarTextoObligatorio(valor, "El nombre")

    fun validarApellido(valor: String): String? =
        validarTextoObligatorio(valor, "El apellido")

    fun validarEspecialidad(valor: String): String? =
        validarTextoObligatorio(valor, "La especialidad")

    private fun validarTextoObligatorio(valor: String, etiqueta: String): String? {
        return when {
            // isBlank() cubre tanto "" como "   " (solo espacios) en un solo chequeo.
            valor.isBlank() -> "$etiqueta es obligatorio"
            valor.length > MAX_LARGO_TEXTO -> "$etiqueta no puede superar los $MAX_LARGO_TEXTO caracteres"
            else -> null
        }
    }

    fun validarEmail(valor: String): String? {
        return when {
            valor.isBlank() -> "El email es obligatorio"
            valor.length > MAX_LARGO_EMAIL -> "El email no puede superar los $MAX_LARGO_EMAIL caracteres"
            // Patterns.EMAIL_ADDRESS es la regex que trae el propio Android para validar
            // "contiene @ + dominio válido" sin tener que escribir una regex a mano.
            !android.util.Patterns.EMAIL_ADDRESS.matcher(valor).matches() ->
                "El email no tiene un formato válido"
            else -> null
        }
    }

    fun validarTelefono(valor: String): String? {
        return when {
            valor.isBlank() -> "El teléfono es obligatorio"
            !valor.all { it.isDigit() } -> "El teléfono solo puede contener números"
            valor.length > MAX_DIGITOS_TELEFONO -> "El teléfono no puede tener más de $MAX_DIGITOS_TELEFONO dígitos"
            else -> null
        }
    }

    // Recibe el valor SIN formato (solo dígitos) -> la pantalla es responsable de
    // desformatear antes de validar, usando TarifaFormatter.quitarFormato(...).
    fun validarTarifa(valorSinFormato: String): String? {
        return when {
            valorSinFormato.isBlank() -> "La tarifa es obligatoria"
            !valorSinFormato.all { it.isDigit() } -> "La tarifa solo puede contener números"
            valorSinFormato.length > MAX_DIGITOS_TARIFA -> "La tarifa no puede tener más de $MAX_DIGITOS_TARIFA dígitos"
            valorSinFormato.toInt() <= 0 -> "La tarifa debe ser mayor a 0"
            else -> null
        }
    }
}