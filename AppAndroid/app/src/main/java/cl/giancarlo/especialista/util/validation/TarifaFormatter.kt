package cl.giancarlo.especialista.util.validation

import java.text.NumberFormat
import java.util.Locale

// Formatea/desformatea el campo tarifa entre su forma "cruda" (solo dígitos, la que
// se valida y se envía al backend) y su forma "visual" ($15.000, la que se muestra
// cuando el campo pierde el foco).
object TarifaFormatter {

    // Locale chileno: separador de miles con punto, sin decimales (los pesos no llevan centavos).
    private val formatoCLP: NumberFormat = NumberFormat.getInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0
    }

    // "15000" -> "$15.000"
    fun formatear(valorSinFormato: String): String {
        val numero = valorSinFormato.toLongOrNull() ?: return ""
        return "$${formatoCLP.format(numero)}"
    }

    // "$15.000" -> "15000" (para volver a dejarlo editable como número plano)
    fun quitarFormato(valorFormateado: String): String =
        valorFormateado.filter { it.isDigit() }
}