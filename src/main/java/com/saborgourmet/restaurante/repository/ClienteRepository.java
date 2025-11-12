package com.saborgourmet.restaurante.repository;

import com.saborgourmet.restaurante.model.Cliente;
import com.saborgourmet.restaurante.model.EstadoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Método personalizado: buscar cliente por DNI
    Optional<Cliente> findByDni(String dni);

    // Método personalizado: buscar clientes por estado
    List<Cliente> findByEstado(EstadoCliente estado);

    // Método personalizado: buscar clientes por nombres o apellidos (búsqueda parcial)
    List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);
}
