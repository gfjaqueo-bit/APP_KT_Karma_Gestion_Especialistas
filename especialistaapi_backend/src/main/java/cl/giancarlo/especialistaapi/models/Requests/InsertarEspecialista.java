package cl.giancarlo.especialistaapi.models.Requests;

import cl.giancarlo.especialistaapi.models.entities.Modalidad;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class InsertarEspecialista {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    private String telefono;

    @NotNull(message = "La modalidad es obligatoria")
    private Modalidad modalidad;

    @Positive(message = "La tarifa por hora debe ser un número positivo")
    private int tarifaHora;

   } 