package com.hotel.reservas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ReservaRequest(
        @NotNull(message = "El id del huésped es obligatorio")
        @Positive(message = "El id del huésped debe ser positivo")
        Long idHuesped,

        @NotNull(message = "El id de la habitación es obligatorio")
        @Positive(message = "El id de la habitación debe ser positivo")
        Long idHabitacion,

        @NotNull(message = "La fecha de entrada es obligatoria")
        @FutureOrPresent(message = "La fecha de entrada no puede ser pasada")
        LocalDate fechaEntrada,

        @NotNull(message = "La fecha de salida es obligatoria")
        LocalDate fechaSalida
) {}
