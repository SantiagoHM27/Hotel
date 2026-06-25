package com.hotel.habitaciones.mapper;

import com.hotel.commons.mappers.CommonMapper;
import com.hotel.habitaciones.dto.HabitacionRequest;
import com.hotel.habitaciones.dto.HabitacionResponse;
import com.hotel.habitaciones.entity.Habitacion;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion> {

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        return Habitacion.builder()
                .numero(request.numero())
                .tipo(request.tipo().trim())
                .precio(request.precio())
                .capacidad(request.capacidad())
                .build();
    }

    @Override
    public HabitacionResponse entidadAResponse(Habitacion habitacion) {
        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getNumero(),
                habitacion.getTipo(),
                habitacion.getPrecio(),
                habitacion.getCapacidad(),
                habitacion.getEstadoHabitacion().name(),
                habitacion.getEstadoRegistro().name()
        );
    }
}
