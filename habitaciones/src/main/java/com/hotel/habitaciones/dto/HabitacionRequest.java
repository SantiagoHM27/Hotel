package com.hotel.habitaciones.dto;

import jakarta.validation.constraints.*;

public record HabitacionRequest(

        @NotNull(message = "El número de habitación es obligatorio")
        @Positive(message = "El número de habitación debe ser mayor a 0")
        Integer numero,

        @NotBlank(message = "El tipo de habitación es obligatorio")
        @Size(min = 2, max = 50, message = "El tipo debe tener entre 2 y 50 caracteres")
        String tipo,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        Double precio,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad mínima es 1")
        Integer capacidad
) {}
