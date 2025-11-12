package com.saborgourmet.restaurante.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @Column(nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column
    private LocalDateTime fechaLiberacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAsignacion tipo;  // OCUPACION o RESERVA

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
