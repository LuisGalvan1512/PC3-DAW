package com.saborgourmet.restaurante.controller;

import com.saborgourmet.restaurante.model.EstadoMesa;
import com.saborgourmet.restaurante.model.Mesa;
import com.saborgourmet.restaurante.service.MesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaViewController {

    private final MesaService mesaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        model.addAttribute("disponibles", mesaService.contarPorEstado(EstadoMesa.DISPONIBLE));
        model.addAttribute("ocupadas", mesaService.contarPorEstado(EstadoMesa.OCUPADA));
        return "mesas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("mesa", new Mesa());
        model.addAttribute("estados", EstadoMesa.values());
        return "mesas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Mesa mesa,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "mesas/formulario";
        }

        try {
            if (mesa.getIdMesa() == null) {
                mesaService.crear(mesa);
                redirectAttributes.addFlashAttribute("mensaje", "Mesa creada exitosamente");
            } else {
                mesaService.actualizar(mesa.getIdMesa(), mesa);
                redirectAttributes.addFlashAttribute("mensaje", "Mesa actualizada exitosamente");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/mesas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Mesa mesa = mesaService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        model.addAttribute("mesa", mesa);
        model.addAttribute("estados", EstadoMesa.values());
        return "mesas/formulario";
    }

    @GetMapping("/cambiar-estado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Long id,
                                @PathVariable EstadoMesa estado,
                                RedirectAttributes redirectAttributes) {
        try {
            mesaService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mesas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mesaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa eliminada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mesas";
    }
}
