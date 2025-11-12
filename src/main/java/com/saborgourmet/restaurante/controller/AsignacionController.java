package com.saborgourmet.restaurante.controller;

import com.saborgourmet.restaurante.model.Asignacion;
import com.saborgourmet.restaurante.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @GetMapping
    public ResponseEntity<List<Asignacion>> listarTodas() {
        return ResponseEntity.ok(asignacionService.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Asignacion>> listarActivas() {
        return ResponseEntity.ok(asignacionService.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> obtenerPorId(@PathVariable Long id) {
        return asignacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/asignar")
    public ResponseEntity<?> asignarMesa(@RequestParam Long idCliente,
                                         @RequestParam Long idMesa,
                                         @RequestParam(required = false) String observaciones) {
        try {
            Asignacion asignacion = asignacionService.asignarMesa(idCliente, idMesa, observaciones);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> crearReserva(@RequestParam Long idCliente,
                                          @RequestParam Long idMesa,
                                          @RequestParam(required = false) String observaciones) {
        try {
            Asignacion asignacion = asignacionService.crearReserva(idCliente, idMesa, observaciones);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/liberar/{id}")
    public ResponseEntity<?> liberarMesa(@PathVariable Long id) {
        try {
            Asignacion asignacion = asignacionService.liberarMesa(id);
            return ResponseEntity.ok(asignacion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Asignacion>> obtenerHistorialCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(asignacionService.obtenerHistorialCliente(idCliente));
    }
}
