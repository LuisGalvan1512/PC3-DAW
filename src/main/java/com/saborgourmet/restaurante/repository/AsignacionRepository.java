package com.saborgourmet.restaurante.repository;

import com.saborgourmet.restaurante.model.Asignacion;
import com.saborgourmet.restaurante.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    // Buscar asignaciones activas
    List<Asignacion> findByActivaTrue();

    // Buscar asignación activa por mesa
    Optional<Asignacion> findByMesaAndActivaTrue(Mesa mesa);

    // Buscar asignaciones por cliente
    List<Asignacion> findByClienteIdCliente(Long idCliente);

    // Buscar asignaciones activas por cliente
    List<Asignacion> findByClienteIdClienteAndActivaTrue(Long idCliente);
}
