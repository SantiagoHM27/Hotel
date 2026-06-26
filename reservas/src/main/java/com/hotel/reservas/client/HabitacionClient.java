package com.hotel.reservas.client;

import com.hotel.reservas.dto.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "habitaciones", path = "/api/habitaciones")
public interface HabitacionClient {

    @GetMapping("/id-habitacion/{id}")
    HabitacionResponse obtenerPorIdHabitacion(@PathVariable Long id);

    @PutMapping("/{id}/estado-interno/{idEstado}")
    HabitacionResponse cambiarEstadoInterno(@PathVariable Long id, @PathVariable Integer idEstado);
}
