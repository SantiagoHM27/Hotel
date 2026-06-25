package com.hotel.huespedes.entity;

import com.hotel.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "huespedes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_huespedes")
    @SequenceGenerator(name = "seq_huespedes", sequenceName = "SEQ_HUESPEDES", allocationSize = 1)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "documento", nullable = false, length = 20)
    private String documento;

    @Column(name = "nacionalidad", nullable = false, length = 60)
    private String nacionalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", nullable = false, length = 20)
    private EstadoRegistro estadoRegistro;

    @PrePersist
    public void prePersist() {
        this.estadoRegistro = EstadoRegistro.ACTIVO;
    }
}
