package cl.giancarlo.especialista.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.giancarlo.especialista.models.Especialista

// Compartida por Lista, Editar y Eliminar: la tarjeta es la misma en las 3 pantallas,
// lo único que cambia es qué pasa al tocarla (onClick) y si trae un ícono extra a la derecha.
@Composable
fun TarjetaEspecialista(
    especialista: Especialista,
    onClick: () -> Unit,
    accionTrailing: (@Composable () -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${especialista.nombre} ${especialista.apellido}", style = MaterialTheme.typography.titleMedium)
                Text(text = especialista.especialidad)
                Text(text = especialista.modalidad.name)
                Text(text = "$${especialista.tarifaHora} / hora")
            }
            accionTrailing?.invoke()
        }
    }
}