package com.hotel.reservas.controller;

import com.hotel.commons.controllers.CommonController;
import com.hotel.reservas.dto.ReservaRequest;
import com.hotel.reservas.dto.ReservaResponse;
import com.hotel.reservas.service.ReservaService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@Validated
public class ReservaController extends CommonController<ReservaRequest, ReservaResponse, ReservaService> {

    public ReservaController(ReservaService service) {
        super(service);
    }

    @PatchMapping("/{idReserva}/estado/{idEstado}")
    public ResponseEntity<ReservaResponse> cambiarEstado(
            @PathVariable @Positive(message = "El ID de reserva debe ser positivo") Long idReserva,
            @PathVariable @Positive(message = "El ID de estado debe ser positivo") Integer idEstado) {
        return ResponseEntity.ok(service.cambiarEstado(idReserva, idEstado));
    }

    @GetMapping("/huesped/{idHuesped}/en-curso")
    public ResponseEntity<Boolean> tieneReservasEnCurso(
            @PathVariable @Positive(message = "El ID del huésped debe ser positivo") Long idHuesped) {
        return ResponseEntity.ok(service.tieneReservasEnCurso(idHuesped));
    }
}
