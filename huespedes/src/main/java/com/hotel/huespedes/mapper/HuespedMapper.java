package com.hotel.huespedes.mapper;

import com.hotel.commons.mappers.CommonMapper;
import com.hotel.huespedes.dto.HuespedRequest;
import com.hotel.huespedes.dto.HuespedResponse;
import com.hotel.huespedes.entity.Huesped;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper implements CommonMapper<HuespedRequest, HuespedResponse, Huesped> {

    @Override
    public Huesped requestAEntidad(HuespedRequest request) {
        return Huesped.builder()
                .nombre(request.nombre().trim())
                .apellido(request.apellido().trim())
                .email(request.email().trim().toLowerCase())
                .telefono(request.telefono().trim())
                .documento(request.documento().trim())
                .nacionalidad(request.nacionalidad().trim())
                .build();
    }

    @Override
    public HuespedResponse entidadAResponse(Huesped huesped) {
        return new HuespedResponse(
                huesped.getId(),
                huesped.getNombre(),
                huesped.getApellido(),
                huesped.getEmail(),
                huesped.getTelefono(),
                huesped.getDocumento(),
                huesped.getNacionalidad(),
                huesped.getEstadoRegistro().name()
        );
    }
}
