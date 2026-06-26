package com.hotel.reservas.client;

import com.hotel.reservas.dto.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "huespedes", path = "/api/huespedes")
public interface HuespedClient {

    @GetMapping("/id-huesped/{id}")
    HuespedResponse obtenerPorIdHuesped(@PathVariable Long id);
}
