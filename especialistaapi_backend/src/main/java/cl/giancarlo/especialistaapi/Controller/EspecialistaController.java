
/*
EspecialistaController.java es el punto de entrada HTTP del módulo Especialista.
Recibe las peticiones REST, delega toda la lógica en EspecialistaService,
y no contiene reglas de negocio propias.
*/

package cl.giancarlo.especialistaapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
/*
 * ========================================
 * IMPORTS DE SPRING FRAMEWORK
 * ========================================
 */
import org.springframework.http.HttpStatus;                    // Códigos de estado HTTP
import org.springframework.http.ResponseEntity;                // Envolver respuestas con status + body
import org.springframework.web.bind.annotation.*;              // Anotaciones REST (@GetMapping, @PostMapping, etc.)

/*
 * ========================================
 * IMPORTS DE VALIDACIÓN
 * ========================================
 */
import jakarta.validation.Valid;                                // Dispara las validaciones (@NotBlank, @Email, etc.) de los Request

/*
 * ========================================
 * IMPORTS DE MODELOS Y SERVICE
 * ========================================
 */
import cl.giancarlo.especialistaapi.models.entities.Especialista;            // Entidad Especialista
import cl.giancarlo.especialistaapi.models.Requests.InsertarEspecialista;    // DTO para crear
import cl.giancarlo.especialistaapi.models.Requests.ActualizarEspecialista;  // DTO para actualizar
import cl.giancarlo.especialistaapi.Services.EspecialistaService;           // Lógica de negocio

import java.util.List;                                          // Para trabajar con listas de especialistas


/*
 * ========================================
 * CONTROLLER REST - MÓDULO ESPECIALISTAS
 * ========================================
 * Expone los 5 endpoints CRUD sobre /api/especialistas.
 * Toda excepción lanzada por el service (EspecialistaNoEncontrado) es
 * capturada por GlobalExceptionHandler y se traduce en un 404 real.
 */
@RestController
@RequestMapping("/api/especialistas")
@CrossOrigin(origins = "*")   // CORS abierto: solo válido para desarrollo local (ver sección 3.2 del espacio)
public class EspecialistaController {

    /*
     * ========================================
     * INYECCIÓN DEL SERVICE
     * ========================================
     */
    @Autowired
    private EspecialistaService especialistaService;


    /*
     * ========================================
     * GET /api/especialistas - LISTAR TODOS
     * ========================================
     */
    @GetMapping
    public ResponseEntity<List<Especialista>> obtenerTodos() {
        List<Especialista> especialistas = especialistaService.obtenerTodosLosEspecialistas();
        return ResponseEntity.ok(especialistas);                 // 200 OK con la lista completa
    }


    /*
     * ========================================
     * GET /api/especialistas/{id} - OBTENER POR ID
     * ========================================
     */
    @GetMapping("/{id}")
    public ResponseEntity<Especialista> obtenerPorId(@PathVariable int id) {
        Especialista especialista = especialistaService.obtenerEspecialistaPorId(id);
        return ResponseEntity.ok(especialista);                  // 200 OK, o 404 si el service lanza EspecialistaNoEncontrado
    }


    /*
     * ========================================
     * POST /api/especialistas - CREAR
     * ========================================
     */
    @PostMapping
    public ResponseEntity<Especialista> crear(@Valid @RequestBody InsertarEspecialista nuevoEspecialista) {
        Especialista creado = especialistaService.agregarEspecialista(nuevoEspecialista);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado); // 201 Created
    }


    /*
     * ========================================
     * PUT /api/especialistas - ACTUALIZAR
     * ========================================
     * El id viaja dentro del body (ActualizarEspecialista.id), no en la URL,
     * siguiendo la misma lógica que EspecialistaService.actualizarEspecialista(...).
     */
    @PutMapping
    public ResponseEntity<Especialista> actualizar(@Valid @RequestBody ActualizarEspecialista datosActualizados) {
        Especialista actualizado = especialistaService.actualizarEspecialista(datosActualizados);
        return ResponseEntity.ok(actualizado);                    // 200 OK, o 404 si el id no existe
    }


    /*
     * ========================================
     * DELETE /api/especialistas/{id} - ELIMINAR
     * ========================================
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        String mensaje = especialistaService.eliminarEspecialista(id);
        return ResponseEntity.ok(mensaje);                        // 200 OK con mensaje, o 404 si no existe
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<Especialista>> buscar(@RequestParam String texto) {
        return ResponseEntity.ok(especialistaService.buscarEspecialistas(texto));
    }
}