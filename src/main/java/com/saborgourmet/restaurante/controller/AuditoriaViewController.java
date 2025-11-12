package com.saborgourmet.restaurante.controller;

import com.saborgourmet.restaurante.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaViewController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("auditorias", auditoriaService.listarTodas());
        return "auditoria/listar";
    }
}
