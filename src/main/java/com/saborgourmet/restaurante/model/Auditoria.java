package com.saborgourmet.restaurante.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAuditoria;

    @Column(nullable = false, length = 50)
    private String entidad;  // Cliente o Mesa

    @Column(nullable = false, length = 20)
    private String operacion;  // CREAR, ACTUALIZAR, ELIMINAR

    @Column(length = 100)
    private String metodo;  // Nombre del método ejecutado

    @Column(columnDefinition = "TEXT")
    private String detalles;  // Información adicional

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(length = 50)
    private String usuario;  // Usuario que realizó la acción (por ahora "SYSTEM")

    private Long tiempoEjecucionMs;  // Tiempo de ejecución del método
}
