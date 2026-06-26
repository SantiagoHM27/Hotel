package com.hotel.habitaciones.service;

import com.hotel.commons.services.CrudService;
import com.hotel.habitaciones.dto.HabitacionRequest;
import com.hotel.habitaciones.dto.HabitacionResponse;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    HabitacionResponse obtenerPorIdHabitacion(Long id);

    HabitacionResponse cambiarEstado(Long id, Integer idEstado);

    HabitacionResponse cambiarEstadoInterno(Long id, Integer idEstado);
}