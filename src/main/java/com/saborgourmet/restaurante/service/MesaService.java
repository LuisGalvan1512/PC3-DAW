package com.saborgourmet.restaurante.service;

import com.saborgourmet.restaurante.model.EstadoMesa;
import com.saborgourmet.restaurante.model.Mesa;
import com.saborgourmet.restaurante.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;

    // Listar todas las mesas
    @Transactional(readOnly = true)
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    // Listar mesas disponibles
    @Transactional(readOnly = true)
    public List<Mesa> listarDisponibles() {
        return mesaRepository.findByEstado(EstadoMesa.DISPONIBLE);
    }

    // Listar mesas por estado
    @Transactional(readOnly = true)
    public List<Mesa> listarPorEstado(EstadoMesa estado) {
        return mesaRepository.findByEstado(estado);
    }

    // Obtener mesa por ID
    @Transactional(readOnly = true)
    public Optional<Mesa> obtenerPorId(Long id) {
        return mesaRepository.findById(id);
    }

    // Buscar mesa por número
    @Transactional(readOnly = true)
    public Optional<Mesa> buscarPorNumero(Integer numero) {
        return mesaRepository.findByNumero(numero);
    }

    // Buscar mesas por capacidad mínima
    @Transactional(readOnly = true)
    public List<Mesa> buscarPorCapacidadMinima(Integer capacidad) {
        return mesaRepository.findByCapacidadGreaterThanEqual(capacidad);
    }

    // Contar mesas por estado
    @Transactional(readOnly = true)
    public Long contarPorEstado(EstadoMesa estado) {
        return mesaRepository.countByEstado(estado);
    }

    // Crear nueva mesa
    @Transactional
    public Mesa crear(Mesa mesa) {
        // Verificar que el número de mesa no exista
        if (mesaRepository.findByNumero(mesa.getNumero()).isPresent()) {
            throw new RuntimeException("Ya existe una mesa con ese número");
        }
        mesa.setEstado(EstadoMesa.DISPONIBLE);
        return mesaRepository.save(mesa);
    }

    // Actualizar mesa
    @Transactional
    public Mesa actualizar(Long id, Mesa mesaActualizada) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        // Verificar número duplicado (si cambió)
        if (!mesa.getNumero().equals(mesaActualizada.getNumero())) {
            if (mesaRepository.findByNumero(mesaActualizada.getNumero()).isPresent()) {
                throw new RuntimeException("Ya existe una mesa con ese número");
            }
        }

        mesa.setNumero(mesaActualizada.getNumero());
        mesa.setCapacidad(mesaActualizada.getCapacidad());

        return mesaRepository.save(mesa);
    }

    // Cambiar estado de la mesa
    @Transactional
    public Mesa cambiarEstado(Long id, EstadoMesa nuevoEstado) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesa.setEstado(nuevoEstado);
        return mesaRepository.save(mesa);
    }

    // Eliminar mesa (solo si está disponible)
    @Transactional
    public void eliminar(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        if (mesa.getEstado() != EstadoMesa.DISPONIBLE) {
            throw new RuntimeException("No se puede eliminar una mesa que no está disponible");
        }

        mesaRepository.deleteById(id);
    }
}
