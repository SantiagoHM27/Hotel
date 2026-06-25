package com.hotel.habitaciones.entity;

import com.hotel.commons.enums.EstadoHabitacion;
import com.hotel.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "habitaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_habitaciones")
    @SequenceGenerator(name = "seq_habitaciones", sequenceName = "SEQ_HABITACIONES", allocationSize = 1)
    private Long id;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_habitacion", nullable = false, length = 20)
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", nullable = false, length = 20)
    private EstadoRegistro estadoRegistro;

    @PrePersist
    public void prePersist() {
        this.estadoHabitacion = EstadoHabitacion.DISPONIBLE;
        this.estadoRegistro = EstadoRegistro.ACTIVO;
    }
}
