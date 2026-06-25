package com.hotel.huespedes.service.impl;

import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.commons.exceptions.EntidadRelacionadaException;
import com.hotel.commons.exceptions.RecursoDuplicadoException;
import com.hotel.commons.exceptions.RecursoNoEncontradoException;
import com.hotel.huespedes.client.ReservaClient;
import com.hotel.huespedes.dto.HuespedRequest;
import com.hotel.huespedes.dto.HuespedResponse;
import com.hotel.huespedes.entity.Huesped;
import com.hotel.huespedes.mapper.HuespedMapper;
import com.hotel.huespedes.repository.HuespedRepository;
import com.hotel.huespedes.service.HuespedService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HuespedServiceImpl implements HuespedService {

    private final HuespedRepository repository;
    private final HuespedMapper mapper;
    private final ReservaClient reservaClient;

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponse> listar() {
        return repository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(mapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerPorId(Long id) {
        return mapper.entidadAResponse(buscarActivoPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerPorIdHuesped(Long id) {
        // Endpoint específico para Feign desde otros microservicios
        return obtenerPorId(id);
    }

    @Override
    @Transactional
    public HuespedResponse registrar(HuespedRequest request) {
        validarUnicidadEnRegistro(request);

        Huesped huesped = mapper.requestAEntidad(request);
        return mapper.entidadAResponse(repository.save(huesped));
    }

    @Override
    @Transactional
    public HuespedResponse actualizar(HuespedRequest request, Long id) {
        Huesped existente = buscarActivoPorId(id);
        validarUnicidadEnActualizacion(request, id);

        existente.setNombre(request.nombre().trim());
        existente.setApellido(request.apellido().trim());
        existente.setEmail(request.email().trim().toLowerCase());
        existente.setTelefono(request.telefono().trim());
        existente.setDocumento(request.documento().trim());
        existente.setNacionalidad(request.nacionalidad().trim());

        return mapper.entidadAResponse(repository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Huesped huesped = buscarActivoPorId(id);

        // Regla: no se puede eliminar si tiene reservas EN_CURSO
        try {
            boolean tieneReservasEnCurso = reservaClient.tieneReservasEnCurso(huesped.getId());
            if (tieneReservasEnCurso) {
                throw new EntidadRelacionadaException(
                        "No se puede eliminar el huésped porque tiene reservas EN_CURSO activas."
                );
            }
        } catch (FeignException.NotFound e) {
            // El microservicio de reservas no encontró registros: se puede eliminar
            log.info("Sin reservas EN_CURSO para huésped id={}", id);
        } catch (EntidadRelacionadaException e) {
            throw e;
        } catch (Exception e) {
            log.warn("No se pudo consultar el servicio de reservas: {}. Se procede con eliminación.", e.getMessage());
        }

        // Eliminación lógica
        huesped.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        repository.save(huesped);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Huesped buscarActivoPorId(Long id) {
        return repository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Huésped no encontrado con id: " + id));
    }

    private void validarUnicidadEnRegistro(HuespedRequest req) {
        if (repository.existsByEmailAndEstadoRegistro(
                req.email().toLowerCase(), EstadoRegistro.ACTIVO)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un huésped ACTIVO con el email: " + req.email());
        }
        if (repository.existsByTelefonoAndEstadoRegistro(
                req.telefono(), EstadoRegistro.ACTIVO)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un huésped ACTIVO con el teléfono: " + req.telefono());
        }
        if (repository.existsByDocumentoAndEstadoRegistro(
                req.documento(), EstadoRegistro.ACTIVO)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un huésped ACTIVO con el documento: " + req.documento());
        }
    }

    private void validarUnicidadEnActualizacion(HuespedRequest req, Long id) {
        if (repository.existsByEmailAndEstadoRegistroAndIdNot(
                req.email().toLowerCase(), EstadoRegistro.ACTIVO, id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un huésped ACTIVO con el email: " + req.email());
        }
        if (repository.existsByTelefonoAndEstadoRegistroAndIdNot(
                req.telefono(), EstadoRegistro.ACTIVO, id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un huésped ACTIVO con el teléfono: " + req.telefono());
        }
        if (repository.existsByDocumentoAndEstadoRegistroAndIdNot(
                req.documento(), EstadoRegistro.ACTIVO, id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe un huésped ACTIVO con el documento: " + req.documento());
        }
    }
}
