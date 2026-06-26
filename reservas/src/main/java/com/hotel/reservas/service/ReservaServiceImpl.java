package com.hotel.reservas.service;

import com.hotel.commons.enums.EstadoHabitacion;
import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.commons.enums.EstadoReserva;
import com.hotel.commons.exceptions.RecursoNoEncontradoException;
import com.hotel.commons.exceptions.TransicionEstadoInvalidaException;
import com.hotel.reservas.client.HabitacionClient;
import com.hotel.reservas.client.HuespedClient;
import com.hotel.reservas.dto.HabitacionResponse;
import com.hotel.reservas.dto.HuespedResponse;
import com.hotel.reservas.dto.ReservaRequest;
import com.hotel.reservas.dto.ReservaResponse;
import com.hotel.reservas.entity.Reserva;
import com.hotel.reservas.mapper.ReservaMapper;
import com.hotel.reservas.repository.ReservaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository repository;
    private final ReservaMapper mapper;
    private final HuespedClient huespedClient;
    private final HabitacionClient habitacionClient;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        return repository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(mapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        return mapper.entidadAResponse(buscarActivaPorId(id));
    }

    @Override
    @Transactional
    public ReservaResponse registrar(ReservaRequest request) {
        validarFechas(request);

        HuespedResponse huesped = obtenerHuespedActivo(request.idHuesped());
        HabitacionResponse habitacion = obtenerHabitacionActiva(request.idHabitacion());

        if (!EstadoHabitacion.DISPONIBLE.name().equals(habitacion.estadoHabitacion())) {
            throw new TransicionEstadoInvalidaException("No se puede crear la reserva porque la habitación no está DISPONIBLE.");
        }

        Reserva reserva = mapper.requestAEntidad(request);
        reserva.setNombreHuesped(huesped.nombre() + " " + huesped.apellido());
        reserva.setEmailHuesped(huesped.email());
        reserva.setNumeroHabitacion(habitacion.numero());
        reserva.setTipoHabitacion(habitacion.tipo());

        Reserva guardada = repository.save(reserva);

        // Al crear reserva CONFIRMADA, la habitación cambia automáticamente a OCUPADA.
        habitacionClient.cambiarEstadoInterno(habitacion.id(), EstadoHabitacion.OCUPADA.getId());

        return mapper.entidadAResponse(guardada);
    }

    @Override
    @Transactional
    public ReservaResponse actualizar(ReservaRequest request, Long id) {
        validarFechas(request);
        Reserva reserva = buscarActivaPorId(id);

        if (reserva.getEstadoReserva() == EstadoReserva.FINALIZADA || reserva.getEstadoReserva() == EstadoReserva.CANCELADA) {
            throw new TransicionEstadoInvalidaException("No se puede modificar una reserva FINALIZADA o CANCELADA.");
        }

        if (reserva.getEstadoReserva() == EstadoReserva.EN_CURSO) {
            if (!reserva.getIdHuesped().equals(request.idHuesped())) {
                throw new TransicionEstadoInvalidaException("No se puede cambiar el huésped después del check-in.");
            }
            if (!reserva.getIdHabitacion().equals(request.idHabitacion())) {
                throw new TransicionEstadoInvalidaException("No se puede cambiar habitación después del check-in.");
            }
            if (!reserva.getFechaEntrada().equals(request.fechaEntrada())) {
                throw new TransicionEstadoInvalidaException("No se puede modificar la fecha de entrada después del check-in.");
            }
            reserva.setFechaSalida(request.fechaSalida());
            return mapper.entidadAResponse(repository.save(reserva));
        }

        // CONFIRMADA: se permiten fechaEntrada y fechaSalida, pero no huésped ni habitación para evitar inconsistencias.
        if (!reserva.getIdHuesped().equals(request.idHuesped())) {
            throw new TransicionEstadoInvalidaException("No se puede cambiar el huésped de la reserva.");
        }
        if (!reserva.getIdHabitacion().equals(request.idHabitacion())) {
            throw new TransicionEstadoInvalidaException("No se puede cambiar la habitación de la reserva.");
        }

        reserva.setFechaEntrada(request.fechaEntrada());
        reserva.setFechaSalida(request.fechaSalida());
        return mapper.entidadAResponse(repository.save(reserva));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = buscarActivaPorId(id);

        if (reserva.getEstadoReserva() == EstadoReserva.EN_CURSO) {
            throw new TransicionEstadoInvalidaException("No se puede eliminar/cancelar una reserva EN_CURSO.");
        }
        if (reserva.getEstadoReserva() == EstadoReserva.FINALIZADA || reserva.getEstadoReserva() == EstadoReserva.CANCELADA) {
            throw new TransicionEstadoInvalidaException("No se puede eliminar una reserva FINALIZADA o CANCELADA.");
        }

        // DELETE funciona como cancelación lógica si está CONFIRMADA.
        reserva.setEstadoReserva(EstadoReserva.CANCELADA);
        reserva.setEstadoRegistro(EstadoRegistro.ELIMINADO);
        repository.save(reserva);
        habitacionClient.cambiarEstadoInterno(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getId());
    }

    @Override
    @Transactional
    public ReservaResponse cambiarEstado(Long idReserva, Integer idEstado) {
        Reserva reserva = buscarActivaPorId(idReserva);
        EstadoReserva nuevoEstado = obtenerEstadoReservaPorId(idEstado);

        switch (nuevoEstado) {
            case EN_CURSO -> hacerCheckIn(reserva);
            case FINALIZADA -> hacerCheckOut(reserva);
            case CANCELADA -> cancelar(reserva);
            case CONFIRMADA -> throw new TransicionEstadoInvalidaException("No se puede regresar una reserva a CONFIRMADA.");
            default -> throw new TransicionEstadoInvalidaException("Estado de reserva inválido.");
        }

        return mapper.entidadAResponse(repository.save(reserva));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneReservasEnCurso(Long idHuesped) {
        return repository.existsByIdHuespedAndEstadoReservaAndEstadoRegistro(
                idHuesped, EstadoReserva.EN_CURSO, EstadoRegistro.ACTIVO
        );
    }

    private void hacerCheckIn(Reserva reserva) {
        if (reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            throw new TransicionEstadoInvalidaException("Solo se puede hacer check-in a una reserva CONFIRMADA.");
        }
        reserva.setEstadoReserva(EstadoReserva.EN_CURSO);
        // La habitación permanece OCUPADA.
    }

    private void hacerCheckOut(Reserva reserva) {
        if (reserva.getEstadoReserva() != EstadoReserva.EN_CURSO) {
            throw new TransicionEstadoInvalidaException("Solo se puede hacer check-out a una reserva EN_CURSO.");
        }
        reserva.setEstadoReserva(EstadoReserva.FINALIZADA);
        habitacionClient.cambiarEstadoInterno(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getId());
    }

    private void cancelar(Reserva reserva) {
        if (reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            throw new TransicionEstadoInvalidaException("Solo se puede cancelar una reserva CONFIRMADA.");
        }
        reserva.setEstadoReserva(EstadoReserva.CANCELADA);
        habitacionClient.cambiarEstadoInterno(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getId());
    }

    private Reserva buscarActivaPorId(Long id) {
        return repository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada con id: " + id));
    }

    private void validarFechas(ReservaRequest request) {
        if (!request.fechaEntrada().isBefore(request.fechaSalida())) {
            throw new IllegalArgumentException("La fecha de entrada debe ser menor que la fecha de salida.");
        }
    }

    private HuespedResponse obtenerHuespedActivo(Long idHuesped) {
        try {
            HuespedResponse huesped = huespedClient.obtenerPorIdHuesped(idHuesped);
            if (!EstadoRegistro.ACTIVO.name().equals(huesped.estadoRegistro())) {
                throw new RecursoNoEncontradoException("El huésped no está ACTIVO.");
            }
            return huesped;
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Huésped no encontrado con id: " + idHuesped);
        }
    }

    private HabitacionResponse obtenerHabitacionActiva(Long idHabitacion) {
        try {
            HabitacionResponse habitacion = habitacionClient.obtenerPorIdHabitacion(idHabitacion);
            if (!EstadoRegistro.ACTIVO.name().equals(habitacion.estadoRegistro())) {
                throw new RecursoNoEncontradoException("La habitación no está ACTIVA.");
            }
            return habitacion;
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Habitación no encontrada con id: " + idHabitacion);
        }
    }

    private EstadoReserva obtenerEstadoReservaPorId(Integer idEstado) {
        for (EstadoReserva estado : EstadoReserva.values()) {
            if (estado.getId().equals(idEstado)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de reserva inválido: " + idEstado);
    }
}
