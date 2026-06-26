package com.hotel.habitaciones.service;

import com.hotel.commons.enums.EstadoHabitacion;
import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.commons.exceptions.EntidadRelacionadaException;
import com.hotel.commons.exceptions.RecursoDuplicadoException;
import com.hotel.commons.exceptions.RecursoNoEncontradoException;
import com.hotel.commons.exceptions.TransicionEstadoInvalidaException;
import com.hotel.habitaciones.dto.HabitacionRequest;
import com.hotel.habitaciones.dto.HabitacionResponse;
import com.hotel.habitaciones.entity.Habitacion;
import com.hotel.habitaciones.mapper.HabitacionMapper;
import com.hotel.habitaciones.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository repository;
    private final HabitacionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        return repository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(mapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        return mapper.entidadAResponse(buscarActivaPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorIdHabitacion(Long id) {
        return obtenerPorId(id);
    }

    @Override
    @Transactional
    public HabitacionResponse registrar(HabitacionRequest request) {
        validarNumeroUnicoRegistro(request.numero());

        Habitacion habitacion = mapper.requestAEntidad(request);

        return mapper.entidadAResponse(repository.save(habitacion));
    }

    @Override
    @Transactional
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = buscarActivaPorId(id);

        validarNumeroUnicoActualizacion(request.numero(), id);

        habitacion.setNumero(request.numero());
        habitacion.setTipo(request.tipo().trim());
        habitacion.setPrecio(request.precio());
        habitacion.setCapacidad(request.capacidad());

        return mapper.entidadAResponse(repository.save(habitacion));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Habitacion habitacion = buscarActivaPorId(id);

        if (habitacion.getEstadoHabitacion() == EstadoHabitacion.OCUPADA) {
            throw new EntidadRelacionadaException("No se puede eliminar una habitación OCUPADA.");
        }

        habitacion.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        repository.save(habitacion);
    }

    @Override
    @Transactional
    public HabitacionResponse cambiarEstado(Long id, Integer idEstado) {
        Habitacion habitacion = buscarActivaPorId(id);
        EstadoHabitacion nuevoEstado = obtenerEstadoHabitacionPorId(idEstado);

        if (habitacion.getEstadoHabitacion() == EstadoHabitacion.OCUPADA
                && nuevoEstado == EstadoHabitacion.DISPONIBLE) {
            throw new TransicionEstadoInvalidaException(
                    "No se puede cambiar manualmente una habitación OCUPADA a DISPONIBLE."
            );
        }

        habitacion.setEstadoHabitacion(nuevoEstado);
        return mapper.entidadAResponse(repository.save(habitacion));
    }

    @Override
    @Transactional
    public HabitacionResponse cambiarEstadoInterno(Long id, Integer idEstado) {
        Habitacion habitacion = buscarActivaPorId(id);
        EstadoHabitacion nuevoEstado = obtenerEstadoHabitacionPorId(idEstado);

        habitacion.setEstadoHabitacion(nuevoEstado);

        return mapper.entidadAResponse(repository.save(habitacion));
    }

    private Habitacion buscarActivaPorId(Long id) {
        return repository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación no encontrada con id: " + id
                ));
    }

    private void validarNumeroUnicoRegistro(Integer numero) {
        if (repository.existsByNumeroAndEstadoRegistro(numero, EstadoRegistro.ACTIVO)) {
            throw new RecursoDuplicadoException(
                    "Ya existe una habitación ACTIVA con el número: " + numero
            );
        }
    }

    private void validarNumeroUnicoActualizacion(Integer numero, Long id) {
        if (repository.existsByNumeroAndEstadoRegistroAndIdNot(numero, EstadoRegistro.ACTIVO, id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe una habitación ACTIVA con el número: " + numero
            );
        }
    }

    private EstadoHabitacion obtenerEstadoHabitacionPorId(Integer idEstado) {
        for (EstadoHabitacion estado : EstadoHabitacion.values()) {
            if (estado.getId().equals(idEstado)) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de habitación inválido: " + idEstado);
    }
}