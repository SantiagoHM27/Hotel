package com.hotel.reservas.mapper;

import com.hotel.commons.mappers.CommonMapper;
import com.hotel.reservas.dto.ReservaRequest;
import com.hotel.reservas.dto.ReservaResponse;
import com.hotel.reservas.entity.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, ReservaResponse, Reserva> {

    @Override
    public Reserva requestAEntidad(ReservaRequest request) {
        return Reserva.builder()
                .idHuesped(request.idHuesped())
                .idHabitacion(request.idHabitacion())
                .fechaEntrada(request.fechaEntrada())
                .fechaSalida(request.fechaSalida())
                .build();
    }

    @Override
    public ReservaResponse entidadAResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getIdHuesped(),
                reserva.getNombreHuesped(),
                reserva.getEmailHuesped(),
                reserva.getIdHabitacion(),
                reserva.getNumeroHabitacion(),
                reserva.getTipoHabitacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstadoReserva().name(),
                reserva.getEstadoRegistro().name()
        );
    }
}
