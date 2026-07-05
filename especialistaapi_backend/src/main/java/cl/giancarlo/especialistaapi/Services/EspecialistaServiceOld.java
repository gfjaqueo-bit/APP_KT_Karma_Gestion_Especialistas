
/*
EspecialistaService.java es un archivo que contiene los métodos principales de acciones, es decir la lógica de negocio,
y los ejecuta según la llamada realizada desde EspecialistaController.
Esta clase actúa como capa intermedia entre el controller y el repository.
*/

package cl.giancarlo.especialistaapi.Services;

/*
 * ========================================
 * IMPORTS PARA SERVICIO DE ESPECIALISTAS
 * ========================================
 */
import java.util.List;                                              // Para trabajar con listas de especialistas


/*
 * ========================================
 * IMPORTS DE SPRING FRAMEWORK
 * ========================================
 */
import org.springframework.beans.factory.annotation.Autowired;      // Inyección de dependencias
import org.springframework.stereotype.Service;                     // Marca esta clase como servicio Spring


/*
 * ========================================
 * IMPORTS DE MODELOS, REPOSITORY Y EXCEPCIÓN
 * ========================================
 */
import cl.giancarlo.especialistaapi.models.entities.Especialista;             // Entidad Especialista (tabla de base de datos)
import cl.giancarlo.especialistaapi.models.Requests.ActualizarEspecialista;   // DTO para actualizar especialista
import cl.giancarlo.especialistaapi.models.Requests.InsertarEspecialista;     // DTO para agregar especialista
import cl.giancarlo.especialistaapi.Repository.EspecialistaRepository;       // Repository de acceso a base de datos
import cl.giancarlo.especialistaapi.Exception.EspecialistaNoEncontrado;      // Excepción custom para especialista no encontrado


/*
 * ========================================
 * SERVICIO DE NEGOCIO - LÓGICA ESPECIALISTAS
 * ========================================
 * Esta clase contiene la lógica de negocio del módulo Especialista
 * Aquí se validan y ejecutan las operaciones antes de acceder al repository
 */
@Service                                                             // Spring gestiona esta clase como un servicio
public class EspecialistaServiceOld {

    /*
     * ========================================
     * INYECCIÓN DEL REPOSITORY
     * ========================================
     */
    @Autowired                                                       // Spring inyecta automáticamente EspecialistaRepository
    private EspecialistaRepository especialistaRepository;           // Acceso a la tabla especialistas


    /*
     * ========================================
     * GET ALL - OBTENER TODOS LOS ESPECIALISTAS
     * ========================================
     * Retorna la lista completa de especialistas registrados
     */
    public List<Especialista> obtenerTodosLosEspecialistas(){
        return especialistaRepository.findAll();                     // Llama al método heredado del repository
    }


    /*
     * ========================================
     * GET BY ID - OBTENER ESPECIALISTA POR ID
     * ========================================
     * Busca un especialista específico según su id
     */
    public Especialista obtenerEspecialistaPorId(int idEspecialista){
        Especialista especialista = especialistaRepository.findById(idEspecialista).orElse(null); // Busca el especialista por id

        if (especialista == null){
            throw new EspecialistaNoEncontrado(idEspecialista);      // Se traduce a 404 en el GlobalExceptionHandler
        }

        return especialista;                                         // Devuelve el especialista encontrado
    }


    /*
     * ========================================
     * POST - AGREGAR NUEVO ESPECIALISTA
     * ========================================
     * Crea un nuevo especialista en la base de datos
     */
    public Especialista agregarEspecialista(InsertarEspecialista nuevoEspecialista){
        Especialista especialistaNuevo = new Especialista();                        // Crea una nueva entidad Especialista
        nuevoEspecialista.setNombre(nuevoEspecialista.getNombre());                 // Asigna el nombre
        nuevoEspecialista.setApellido(nuevoEspecialista.getApellido());             // Asigna el apellido
        nuevoEspecialista.setEspecialidad(nuevoEspecialista.getEspecialidad());     // Asigna la especialidad
        nuevoEspecialista.setEmail(nuevoEspecialista.getEmail());                   // Asigna el email
        nuevoEspecialista.setTelefono(nuevoEspecialista.getTelefono());             // Asigna el teléfono
        nuevoEspecialista.setModalidad(nuevoEspecialista.getModalidad());           // Asigna la modalidad
        nuevoEspecialista.setTarifaHora(nuevoEspecialista.getTarifaHora());         // Asigna la tarifa por hora
        return especialistaRepository.save(especialistaNuevo);                     // Guarda el nuevo especialista en la base de datos
    }


    /*
     * ========================================
     * DELETE - ELIMINAR ESPECIALISTA
     * ========================================
     * Elimina un especialista según su id
     */
    public String eliminarEspecialista(int idEspecialista){
        if (especialistaRepository.existsById(idEspecialista)) {              // Verifica si el especialista existe antes de eliminar
            especialistaRepository.deleteById(idEspecialista);                // Elimina el especialista de la base de datos
            return "Especialista eliminado correctamente.";                   // Mensaje de éxito
        } else {
            throw new EspecialistaNoEncontrado(idEspecialista);               // Se traduce a 404 en el GlobalExceptionHandler
        }
    }


    /*
     * ========================================
     * PUT - ACTUALIZAR ESPECIALISTA
     * ========================================
     * Actualiza los datos de un especialista existente
     */
    public Especialista actualizarEspecialista(ActualizarEspecialista ActualizarEspecialista){
        Especialista especialista = especialistaRepository.findById(ActualizarEspecialista.getId()).orElse(null); // Busca el especialista por id

        if (especialista == null){
            throw new EspecialistaNoEncontrado(ActualizarEspecialista.getId());                    // Se traduce a 404 en el GlobalExceptionHandler
        } else {
            especialista.setNombre(ActualizarEspecialista.getNombre());                 // Actualiza el nombre
            especialista.setApellido(ActualizarEspecialista.getApellido());             // Actualiza el apellido
            especialista.setEspecialidad(ActualizarEspecialista.getEspecialidad());     // Actualiza la especialidad
            especialista.setEmail(ActualizarEspecialista.getEmail());                   // Actualiza el email
            especialista.setTelefono(ActualizarEspecialista.getTelefono());             // Actualiza el teléfono
            especialista.setModalidad(ActualizarEspecialista.getModalidad());           // Actualiza la modalidad
            especialista.setTarifaHora(ActualizarEspecialista.getTarifaHora());         // Actualiza la tarifa por hora
            return especialistaRepository.save(especialista);                      // Guarda cambios en la base de datos
        }
    }
}