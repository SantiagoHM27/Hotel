package com.hotel.reservas.service;

import com.hotel.commons.services.CrudService;
import com.hotel.reservas.dto.ReservaRequest;
import com.hotel.reservas.dto.ReservaResponse;

public interface ReservaService extends CrudService<ReservaRequest, ReservaResponse> {

    ReservaResponse cambiarEstado(Long idReserva, Integer idEstado);

    boolean tieneReservasEnCurso(Long idHuesped);
}
