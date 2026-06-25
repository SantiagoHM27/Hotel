package com.hotel.huespedes.repository;

import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.huespedes.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {


    List<Huesped> findByEstadoRegistro(EstadoRegistro estadoRegistro);


    boolean existsByEmailAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
    boolean existsByDocumentoAndEstadoRegistro(String documento, EstadoRegistro estadoRegistro);


    boolean existsByEmailAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long id);
    boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long id);
    boolean existsByDocumentoAndEstadoRegistroAndIdNot(String documento, EstadoRegistro estadoRegistro, Long id);

    Optional<Huesped> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
}
