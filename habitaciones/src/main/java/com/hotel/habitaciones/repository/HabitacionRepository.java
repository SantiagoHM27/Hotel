package com.hotel.habitaciones.repository;

import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.habitaciones.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    // Listar solo activas
    List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    // Unicidad de número entre activos
    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estadoRegistro);

    // Unicidad en actualización (excluye el propio registro)
    boolean existsByNumeroAndEstadoRegistroAndIdNot(Integer numero, EstadoRegistro estadoRegistro, Long id);

    // Buscar activa por id
    Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
}
