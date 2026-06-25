package com.hotel.commons.enums;

public enum EstadoHabitacion {

    DISPONIBLE(1, "Lista para asignarse"),
    OCUPADA(2, "Asignada a una reserva"),
    LIMPIEZA(3, "En limpieza"),
    MANTENIMIENTO(4, "En reparación");

    private final Integer id;
    private final String descripcion;

    EstadoHabitacion(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}