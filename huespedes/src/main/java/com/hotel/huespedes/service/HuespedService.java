package com.hotel.huespedes.service;

import com.hotel.commons.services.CrudService;
import com.hotel.huespedes.dto.HuespedRequest;
import com.hotel.huespedes.dto.HuespedResponse;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse> {

    /**
     * Busca un huésped por su ID de negocio (usado desde otros microservicios vía Feign).
     * Solo retorna huéspedes ACTIVOS.
     */
    HuespedResponse obtenerPorIdHuesped(Long id);
}
