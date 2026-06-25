package com.hotel.huespedes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "reservas", path = "/api/reservas")
public interface ReservaClient {


    @GetMapping("/huesped/{idHuesped}/en-curso")
    boolean tieneReservasEnCurso(@PathVariable Long idHuesped);
}
