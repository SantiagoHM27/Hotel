package com.hotel.reservas.dto;

import java.time.LocalDate;

public record ReservaResponse(
        Long id,
        Long idHuesped,
        String nombreHuesped,
        String emailHuesped,
        Long idHabitacion,
        Integer numeroHabitacion,
        String tipoHabitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        String estadoReserva,
        String estadoRegistro
) {}
