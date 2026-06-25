package com.hotel.commons.enums;

public enum EstadoReserva {

    CONFIRMADA(1, "Reserva creada"),
    EN_CURSO(2, "Check-in realizado"),
    FINALIZADA(3, "Check-out realizado"),
    CANCELADA(4, "Reserva cancelada");

    private final Integer id;
    private final String descripcion;

    EstadoReserva(Integer id, String descripcion) {
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