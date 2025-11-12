package com.saborgourmet.restaurante.service;

import com.saborgourmet.restaurante.model.*;
import com.saborgourmet.restaurante.repository.AsignacionRepository;
import com.saborgourmet.restaurante.repository.ClienteRepository;
import com.saborgourmet.restaurante.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final MesaService mesaService;

    // Listar todas las asignaciones activas
    @Transactional(readOnly = true)
    public List<Asignacion> listarActivas() {
        return asignacionRepository.findByActivaTrue();
    }

    // Listar todas las asignaciones
    @Transactional(readOnly = true)
    public List<Asignacion> listarTodas() {
        return asignacionRepository.findAll();
    }

    // Obtener asignación por ID
    @Transactional(readOnly = true)
    public Optional<Asignacion> obtenerPorId(Long id) {
        return asignacionRepository.findById(id);
    }

    // Asignar cliente a mesa (ocupación)
    @Transactional
    public Asignacion asignarMesa(Long idCliente, Long idMesa, String observaciones) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        // Verificar que la mesa esté disponible
        if (mesa.getEstado() != EstadoMesa.DISPONIBLE) {
            throw new RuntimeException("La mesa no está disponible");
        }

        // Verificar que no haya una asignación activa para esta mesa
        Optional<Asignacion> asignacionExistente = asignacionRepository.findByMesaAndActivaTrue(mesa);
        if (asignacionExistente.isPresent()) {
            throw new RuntimeException("La mesa ya tiene una asignación activa");
        }

        // Crear asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setCliente(cliente);
        asignacion.setMesa(mesa);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setTipo(TipoAsignacion.OCUPACION);
        asignacion.setActiva(true);
        asignacion.setObservaciones(observaciones);

        // Cambiar estado de la mesa a OCUPADA
        mesaService.cambiarEstado(idMesa, EstadoMesa.OCUPADA);

        return asignacionRepository.save(asignacion);
    }

    // Crear reserva
    @Transactional
    public Asignacion crearReserva(Long idCliente, Long idMesa, String observaciones) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        // Verificar que la mesa esté disponible
        if (mesa.getEstado() != EstadoMesa.DISPONIBLE) {
            throw new RuntimeException("La mesa no está disponible para reservar");
        }

        // Crear reserva
        Asignacion asignacion = new Asignacion();
        asignacion.setCliente(cliente);
        asignacion.setMesa(mesa);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setTipo(TipoAsignacion.RESERVA);
        asignacion.setActiva(true);
        asignacion.setObservaciones(observaciones);

        // Cambiar estado de la mesa a RESERVADA
        mesaService.cambiarEstado(idMesa, EstadoMesa.RESERVADA);

        return asignacionRepository.save(asignacion);
    }

    // Liberar mesa (finalizar asignación)
    @Transactional
    public Asignacion liberarMesa(Long idAsignacion) {
        Asignacion asignacion = asignacionRepository.findById(idAsignacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        if (!asignacion.getActiva()) {
            throw new RuntimeException("La asignación ya está finalizada");
        }

        // Finalizar asignación
        asignacion.setActiva(false);
        asignacion.setFechaLiberacion(LocalDateTime.now());

        // Cambiar estado de la mesa a DISPONIBLE
        mesaService.cambiarEstado(asignacion.getMesa().getIdMesa(), EstadoMesa.DISPONIBLE);

        return asignacionRepository.save(asignacion);
    }

    // Obtener historial de asignaciones de un cliente
    @Transactional(readOnly = true)
    public List<Asignacion> obtenerHistorialCliente(Long idCliente) {
        return asignacionRepository.findByClienteIdCliente(idCliente);
    }

    // Obtener asignaciones activas de un cliente
    @Transactional(readOnly = true)
    public List<Asignacion> obtenerAsignacionesActivasCliente(Long idCliente) {
        return asignacionRepository.findByClienteIdClienteAndActivaTrue(idCliente);
    }
}
