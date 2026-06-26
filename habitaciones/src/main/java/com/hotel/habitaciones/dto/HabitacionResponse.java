package com.hotel.habitaciones.dto;

public record HabitacionResponse(
        Long id,
        Integer numero,
        String tipo,
        Double precio,
        Integer capacidad,
        String estadoHabitacion,
        String estadoRegistro
) {}
