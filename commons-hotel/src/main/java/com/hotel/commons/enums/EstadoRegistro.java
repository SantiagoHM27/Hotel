package com.hotel.commons.enums;

public enum EstadoRegistro {

    ACTIVO(1, "Registro activo"),
    ELIMINADO(2, "Registro eliminado");

    private final Integer id;
    private final String descripcion;

    EstadoRegistro(Integer id, String descripcion) {
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