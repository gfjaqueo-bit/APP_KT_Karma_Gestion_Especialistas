package cl.giancarlo.especialista.navigation

// sealed class de rutas: el compilador nos avisa si olvidamos manejar alguna
// en el "when" del NavGraph, a diferencia de usar Strings sueltos por todos lados.
sealed class Ruta(val ruta: String) {

    object Lista : Ruta("lista")


    object Formulario : Ruta("formulario") {
        const val ARG_ID = "idEspecialista"
        // Ruta con argumento opcional: sin id = modo crear, con id = modo editar.
        const val rutaConArgumento = "formulario?$ARG_ID={$ARG_ID}"
        fun crear(): String = "formulario"
        fun editar(id: Int): String = "formulario?$ARG_ID=$id"
    }

    object Detalle : Ruta("detalle") {
        const val ARG_ID = "idEspecialista"
        const val rutaConArgumento = "detalle/{$ARG_ID}"
        fun conId(id: Int): String = "detalle/$id"
    }

    object Menu : Ruta("menu")

    object Busqueda : Ruta("busqueda")

    object EditarLista : Ruta("editar-lista")

    object EliminarLista : Ruta("eliminar-lista")
}