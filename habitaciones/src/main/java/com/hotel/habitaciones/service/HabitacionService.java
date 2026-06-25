package com.hotel.habitaciones.service;

import com.hotel.commons.services.CrudService;
import com.hotel.habitaciones.dto.HabitacionRequest;
import com.hotel.habitaciones.dto.HabitacionResponse;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

   
    HabitacionResponse obtenerPorIdHabitacion(Long id);

    /*
     * Solo ADMIN puede invocar este endpoint.
     * No se permite cambiar manualmente a DISPONIBLE si está OCUPADA.
     */
    HabitacionResponse cambiarEstado(Long id, Integer idEstado);

    /*
      Cambia el estado sin restricciones de rol (confianza interna).
     */
    HabitacionResponse cambiarEstadoInterno(Long id, Integer idEstado);
}
