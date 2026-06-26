package com.hotel.reservas.entity;

import com.hotel.commons.enums.EstadoRegistro;
import com.hotel.commons.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reservas")
    @SequenceGenerator(name = "seq_reservas", sequenceName = "SEQ_RESERVAS", allocationSize = 1)
    private Long id;

    @Column(name = "id_huesped", nullable = false)
    private Long idHuesped;

    @Column(name = "nombre_huesped", nullable = false, length = 120)
    private String nombreHuesped;

    @Column(name = "email_huesped", nullable = false, length = 100)
    private String emailHuesped;

    @Column(name = "id_habitacion", nullable = false)
    private Long idHabitacion;

    @Column(name = "numero_habitacion", nullable = false)
    private Integer numeroHabitacion;

    @Column(name = "tipo_habitacion", nullable = false, length = 50)
    private String tipoHabitacion;

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", nullable = false, length = 20)
    private EstadoReserva estadoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", nullable = false, length = 20)
    private EstadoRegistro estadoRegistro;

    @PrePersist
    public void prePersist() {
        this.estadoReserva = EstadoReserva.CONFIRMADA;
        this.estadoRegistro = EstadoRegistro.ACTIVO;
    }
}
