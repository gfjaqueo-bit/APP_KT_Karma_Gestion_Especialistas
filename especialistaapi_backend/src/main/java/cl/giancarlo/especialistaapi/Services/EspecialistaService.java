package cl.giancarlo.especialistaapi.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.giancarlo.especialistaapi.models.entities.Especialista;
import cl.giancarlo.especialistaapi.models.Requests.ActualizarEspecialista;
import cl.giancarlo.especialistaapi.models.Requests.InsertarEspecialista;
import cl.giancarlo.especialistaapi.Repository.EspecialistaRepository;
import cl.giancarlo.especialistaapi.Exception.EspecialistaNoEncontrado;

@Service
public class EspecialistaService {

    @Autowired
    private EspecialistaRepository especialistaRepository;

    public List<Especialista> obtenerTodosLosEspecialistas(){
        return especialistaRepository.findAll();
    }

    public Especialista obtenerEspecialistaPorId(int idEspecialista){
        return especialistaRepository.findById(idEspecialista)
                .orElseThrow(() -> new EspecialistaNoEncontrado(idEspecialista));
    }

    public Especialista agregarEspecialista(InsertarEspecialista nuevoEspecialista){
        Especialista especialistaNuevo = new Especialista();
        // OJO: los setters van sobre especialistaNuevo (la entidad), NO sobre nuevoEspecialista (el DTO).
        especialistaNuevo.setNombre(nuevoEspecialista.getNombre());
        especialistaNuevo.setApellido(nuevoEspecialista.getApellido());
        especialistaNuevo.setEspecialidad(nuevoEspecialista.getEspecialidad());
        especialistaNuevo.setEmail(nuevoEspecialista.getEmail());
        especialistaNuevo.setTelefono(nuevoEspecialista.getTelefono());
        especialistaNuevo.setModalidad(nuevoEspecialista.getModalidad());
        especialistaNuevo.setTarifaHora(nuevoEspecialista.getTarifaHora());
        return especialistaRepository.save(especialistaNuevo);
    }

    public String eliminarEspecialista(int idEspecialista){
        if (especialistaRepository.existsById(idEspecialista)) {
            especialistaRepository.deleteById(idEspecialista);
            return "Especialista eliminado correctamente.";
        } else {
            throw new EspecialistaNoEncontrado(idEspecialista);
        }
    }

    public Especialista actualizarEspecialista(ActualizarEspecialista datosActualizados){
        Especialista especialista = especialistaRepository.findById(datosActualizados.getId())
                .orElseThrow(() -> new EspecialistaNoEncontrado(datosActualizados.getId()));

        especialista.setNombre(datosActualizados.getNombre());
        especialista.setApellido(datosActualizados.getApellido());
        especialista.setEspecialidad(datosActualizados.getEspecialidad());
        especialista.setEmail(datosActualizados.getEmail());
        especialista.setTelefono(datosActualizados.getTelefono());
        especialista.setModalidad(datosActualizados.getModalidad());
        especialista.setTarifaHora(datosActualizados.getTarifaHora());
        return especialistaRepository.save(especialista);
    }

    public List<Especialista> buscarEspecialistas(String texto) {
        return especialistaRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(texto, texto);
    }
}