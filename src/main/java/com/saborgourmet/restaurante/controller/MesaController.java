package com.saborgourmet.restaurante.controller;

import com.saborgourmet.restaurante.model.EstadoMesa;
import com.saborgourmet.restaurante.model.Mesa;
import com.saborgourmet.restaurante.service.MesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    // Listar todas las mesas
    @GetMapping
    public ResponseEntity<List<Mesa>> listarTodas() {
        List<Mesa> mesas = mesaService.listarTodas();
        return ResponseEntity.ok(mesas);
    }

    // Listar mesas disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Mesa>> listarDisponibles() {
        List<Mesa> mesas = mesaService.listarDisponibles();
        return ResponseEntity.ok(mesas);
    }

    // Listar mesas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Mesa>> listarPorEstado(@PathVariable EstadoMesa estado) {
        List<Mesa> mesas = mesaService.listarPorEstado(estado);
        return ResponseEntity.ok(mesas);
    }

    // Obtener mesa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerPorId(@PathVariable Long id) {
        return mesaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar mesa por número
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Mesa> buscarPorNumero(@PathVariable Integer numero) {
        return mesaService.buscarPorNumero(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar mesas por capacidad mínima
    @GetMapping("/capacidad/{capacidad}")
    public ResponseEntity<List<Mesa>> buscarPorCapacidad(@PathVariable Integer capacidad) {
        List<Mesa> mesas = mesaService.buscarPorCapacidadMinima(capacidad);
        return ResponseEntity.ok(mesas);
    }

    // Contar mesas por estado
    @GetMapping("/contar/{estado}")
    public ResponseEntity<Long> contarPorEstado(@PathVariable EstadoMesa estado) {
        Long cantidad = mesaService.contarPorEstado(estado);
        return ResponseEntity.ok(cantidad);
    }

    // Crear nueva mesa
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Mesa mesa) {
        try {
            Mesa mesaCreada = mesaService.crear(mesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(mesaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar mesa
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody Mesa mesa) {
        try {
            Mesa mesaActualizada = mesaService.actualizar(id, mesa);
            return ResponseEntity.ok(mesaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cambiar estado de la mesa
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestParam EstadoMesa estado) {
        try {
            Mesa mesa = mesaService.cambiarEstado(id, estado);
            return ResponseEntity.ok(mesa);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar mesa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            mesaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
