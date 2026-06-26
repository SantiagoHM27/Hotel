package com.hotel.habitaciones.controller;

import com.hotel.commons.controllers.CommonController;
import com.hotel.habitaciones.dto.HabitacionRequest;
import com.hotel.habitaciones.dto.HabitacionResponse;
import com.hotel.habitaciones.service.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/habitaciones")
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {

    public HabitacionController(HabitacionService service) {
        super(service);
    }

    /*
     * Endpoint interno para Feign desde otros microservicios (ej: reservas).
     */
    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerPorIdHabitacion(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerPorIdHabitacion(id));
    }

    /*
     Restricción: no se puede cambiar a DISPONIBLE si está OCUPADA.
    */
    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<HabitacionResponse> cambiarEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @PathVariable @Positive(message = "El ID de estado debe ser positivo") Integer idEstado) {
        return ResponseEntity.ok(service.cambiarEstado(id, idEstado));
    }

    /*
     * Sin restricción del cambio manual (el sistema lo gestiona automáticamente).
     */
    @PutMapping("/{id}/estado-interno/{idEstado}")
    public ResponseEntity<HabitacionResponse> cambiarEstadoInterno(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @PathVariable @Positive(message = "El ID de estado debe ser positivo") Integer idEstado) {
        return ResponseEntity.ok(service.cambiarEstadoInterno(id, idEstado));
    }
}
