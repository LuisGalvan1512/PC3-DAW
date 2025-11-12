package com.saborgourmet.restaurante.controller;

import com.saborgourmet.restaurante.model.Auditoria;
import com.saborgourmet.restaurante.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // Listar todas las auditorías
    @GetMapping
    public ResponseEntity<List<Auditoria>> listarTodas() {
        List<Auditoria> auditorias = auditoriaService.listarTodas();
        return ResponseEntity.ok(auditorias);
    }

    // Listar auditorías por entidad
    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<List<Auditoria>> listarPorEntidad(@PathVariable String entidad) {
        List<Auditoria> auditorias = auditoriaService.listarPorEntidad(entidad);
        return ResponseEntity.ok(auditorias);
    }
}
