package cl.giancarlo.especialistaapi.models.Responses;

import cl.giancarlo.especialistaapi.models.entities.Modalidad;

public record EspecialistaResponse(
        int id,
        String nombre,
        String apellido,
        String especialidad,
        String email,
        String telefono,
        Modalidad modalidad,
        Integer tarifaHora
) {}

