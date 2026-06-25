package com.hotel.commons.enums;

public enum Rol {

    ADMIN(1, "Administrador"),
    USER(2, "Recepcionista");

    private final Integer id;
    private final String descripcion;

    Rol(Integer id, String descripcion) {
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