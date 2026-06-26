package com.hotel.reservas.repository;

import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.commons.enums.EstadoReserva;
import com.hotel.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByIdHuespedAndEstadoReservaAndEstadoRegistro(Long idHuesped, EstadoReserva estadoReserva, EstadoRegistro estadoRegistro);

    boolean existsByIdHabitacionAndEstadoReservaAndEstadoRegistro(Long idHabitacion, EstadoReserva estadoReserva, EstadoRegistro estadoRegistro);
}
