package cl.giancarlo.especialista.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.giancarlo.especialista.ui.screens.PantallaBusquedaEspecialistas
import cl.giancarlo.especialista.ui.screens.PantallaDetalleEspecialista
import cl.giancarlo.especialista.ui.screens.PantallaEditarEspecialista
import cl.giancarlo.especialista.ui.screens.PantallaEliminarEspecialista
import cl.giancarlo.especialista.ui.screens.PantallaFormularioEspecialista
import cl.giancarlo.especialista.ui.screens.PantallaListaEspecialistas
import cl.giancarlo.especialista.ui.screens.PantallaMenuPrincipal
import cl.giancarlo.especialista.viewmodel.EspecialistaViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val especialistaViewModel: EspecialistaViewModel = viewModel()

    NavHost(navController = navController, startDestination = Ruta.Menu.ruta) {

        composable(Ruta.Menu.ruta) {
            PantallaMenuPrincipal(
                onListar = { navController.navigate(Ruta.Lista.ruta) },
                onBuscar = { navController.navigate(Ruta.Busqueda.ruta) },
                onAgregar = { navController.navigate(Ruta.Formulario.crear()) },
                onEditar = { navController.navigate(Ruta.EditarLista.ruta) },
                onEliminar = { navController.navigate(Ruta.EliminarLista.ruta) },
                viewModel = especialistaViewModel
            )
        }

        composable(Ruta.Lista.ruta) {
            PantallaListaEspecialistas(
                viewModel = especialistaViewModel,
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Ruta.Formulario.crear()) },
                onVerDetalle = { id -> navController.navigate(Ruta.Detalle.conId(id)) }
            )
        }

        composable(Ruta.Busqueda.ruta) {
            PantallaBusquedaEspecialistas(
                onVerDetalle = { id -> navController.navigate(Ruta.Detalle.conId(id)) },
                onVolver = { navController.popBackStack() },
                viewModel = especialistaViewModel
            )
        }

        composable(Ruta.EditarLista.ruta) {
            PantallaEditarEspecialista(
                onSeleccionar = { id -> navController.navigate(Ruta.Formulario.editar(id)) },
                onVolver = { navController.popBackStack() },
                viewModel = especialistaViewModel
            )
        }

        composable(Ruta.EliminarLista.ruta) {
            PantallaEliminarEspecialista(
                onVolver = { navController.popBackStack() },
                viewModel = especialistaViewModel
            )
        }

        composable(
            route = Ruta.Formulario.rutaConArgumento,
            arguments = listOf(
                navArgument(Ruta.Formulario.ARG_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val idRecibido = backStackEntry.arguments?.getInt(Ruta.Formulario.ARG_ID) ?: -1
            PantallaFormularioEspecialista(
                idEspecialista = if (idRecibido == -1) null else idRecibido,
                onGuardadoExitoso = { navController.popBackStack(Ruta.Menu.ruta, inclusive = false) },
                onVolver = { navController.popBackStack() },
                viewModel = especialistaViewModel
            )
        }

        composable(
            route = Ruta.Detalle.rutaConArgumento,
            arguments = listOf(navArgument(Ruta.Detalle.ARG_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Ruta.Detalle.ARG_ID) ?: return@composable
            PantallaDetalleEspecialista(
                idEspecialista = id,
                onEditar = { idParaEditar -> navController.navigate(Ruta.Formulario.editar(idParaEditar)) },
                onEliminado = { navController.popBackStack(Ruta.Menu.ruta, inclusive = false) },
                onVolver = { navController.popBackStack() },
                viewModel = especialistaViewModel
            )
        }
    }
}