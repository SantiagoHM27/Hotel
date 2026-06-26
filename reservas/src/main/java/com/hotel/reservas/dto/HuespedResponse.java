package com.hotel.reservas.dto;

public record HuespedResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String documento,
        String nacionalidad,
        String estadoRegistro
) {}
