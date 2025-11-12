package com.saborgourmet.restaurante.repository;

import com.saborgourmet.restaurante.model.EstadoMesa;
import com.saborgourmet.restaurante.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // Método personalizado: buscar mesa por número
    Optional<Mesa> findByNumero(Integer numero);

    // Método personalizado: buscar mesas por estado
    List<Mesa> findByEstado(EstadoMesa estado);

    // Método personalizado: buscar mesas por capacidad mínima
    List<Mesa> findByCapacidadGreaterThanEqual(Integer capacidad);

    // Método personalizado: contar mesas por estado
    Long countByEstado(EstadoMesa estado);
}
