package com.hotel.habitaciones.controller;

import com.hotel.habitaciones.dto.HabitacionRequest;
import com.hotel.habitaciones.dto.HabitacionResponse;
import com.hotel.habitaciones.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService service;

    @PostMapping
    public ResponseEntity<HabitacionResponse> registrar(@Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerPorIdHabitacion(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorIdHabitacion(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request) {
        return ResponseEntity.ok(service.actualizar(request, id));
    }

    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<HabitacionResponse> cambiarEstado(
            @PathVariable Long id,
            @PathVariable Integer idEstado) {
        return ResponseEntity.ok(service.cambiarEstado(id, idEstado));
    }

    @PutMapping("/{id}/estado-interno/{idEstado}")
    public ResponseEntity<HabitacionResponse> cambiarEstadoInterno(
            @PathVariable Long id,
            @PathVariable Integer idEstado) {
        return ResponseEntity.ok(service.cambiarEstadoInterno(id, idEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
