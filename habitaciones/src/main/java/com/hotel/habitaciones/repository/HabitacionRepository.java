package com.hotel.habitaciones.repository;

import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.habitaciones.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistroAndIdNot(Integer numero, EstadoRegistro estadoRegistro, Long id);

    Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
}