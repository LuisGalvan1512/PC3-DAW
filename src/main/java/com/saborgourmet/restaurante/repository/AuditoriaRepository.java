package com.saborgourmet.restaurante.repository;

import com.saborgourmet.restaurante.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    // Buscar auditorías por entidad
    List<Auditoria> findByEntidad(String entidad);

    // Buscar auditorías por operación
    List<Auditoria> findByOperacion(String operacion);

    // Buscar auditorías en un rango de fechas
    List<Auditoria> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
