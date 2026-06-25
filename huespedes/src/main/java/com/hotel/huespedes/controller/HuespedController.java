package com.hotel.huespedes.controller;

import com.hotel.commons.controllers.CommonController;
import com.hotel.huespedes.dto.HuespedRequest;
import com.hotel.huespedes.dto.HuespedResponse;
import com.hotel.huespedes.service.HuespedService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/huespedes")
@Validated
public class HuespedController extends CommonController<HuespedRequest, HuespedResponse, HuespedService> {

    public HuespedController(HuespedService service) {
        super(service);
    }


    @GetMapping("/id-huesped/{id}")
    public ResponseEntity<HuespedResponse> obtenerPorIdHuesped(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerPorIdHuesped(id));
    }
}
