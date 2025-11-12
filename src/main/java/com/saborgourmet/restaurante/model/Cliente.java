package com.saborgourmet.restaurante.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @Column(nullable = false, unique = true, length = 8)
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoCliente estado = EstadoCliente.ACTIVO;
}
