package com.saborgourmet.restaurante.service;

import com.saborgourmet.restaurante.model.Auditoria;
import com.saborgourmet.restaurante.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    // Registrar auditoría (usa REQUIRES_NEW para que se guarde aunque la operación principal falle)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String entidad, String operacion, String metodo,
                          String detalles, Long tiempoEjecucion) {
        Auditoria auditoria = new Auditoria();
        auditoria.setEntidad(entidad);
        auditoria.setOperacion(operacion);
        auditoria.setMetodo(metodo);
        auditoria.setDetalles(detalles);
        auditoria.setFechaHora(LocalDateTime.now());
        auditoria.setUsuario("SYSTEM");
        auditoria.setTiempoEjecucionMs(tiempoEjecucion);

        auditoriaRepository.save(auditoria);
    }

    // Obtener todas las auditorías
    @Transactional(readOnly = true)
    public List<Auditoria> listarTodas() {
        return auditoriaRepository.findAll();
    }

    // Obtener auditorías por entidad
    @Transactional(readOnly = true)
    public List<Auditoria> listarPorEntidad(String entidad) {
        return auditoriaRepository.findByEntidad(entidad);
    }

    // Registrar auditoría con usuario específico
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarConUsuario(String entidad, String operacion, String metodo,
                                    String detalles, Long tiempoEjecucion, String usuario) {
        Auditoria auditoria = new Auditoria();
        auditoria.setEntidad(entidad);
        auditoria.setOperacion(operacion);
        auditoria.setMetodo(metodo);
        auditoria.setDetalles(detalles);
        auditoria.setFechaHora(LocalDateTime.now());
        auditoria.setUsuario(usuario);
        auditoria.setTiempoEjecucionMs(tiempoEjecucion);

        auditoriaRepository.save(auditoria);
    }
}
