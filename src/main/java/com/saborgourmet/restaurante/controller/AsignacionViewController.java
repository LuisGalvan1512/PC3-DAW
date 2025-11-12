package com.saborgourmet.restaurante.controller;

import com.saborgourmet.restaurante.model.Asignacion;
import com.saborgourmet.restaurante.service.AsignacionService;
import com.saborgourmet.restaurante.service.ClienteService;
import com.saborgourmet.restaurante.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asignaciones")
@RequiredArgsConstructor
public class AsignacionViewController {

    private final AsignacionService asignacionService;
    private final ClienteService clienteService;
    private final MesaService mesaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asignaciones", asignacionService.listarActivas());
        return "asignaciones/listar";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("mesas", mesaService.listarDisponibles());
        return "asignaciones/formulario";
    }

    @PostMapping("/asignar")
    public String asignarMesa(@RequestParam Long idCliente,
                              @RequestParam Long idMesa,
                              @RequestParam(required = false) String observaciones,
                              RedirectAttributes redirectAttributes) {
        try {
            asignacionService.asignarMesa(idCliente, idMesa, observaciones);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa asignada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asignaciones";
    }

    @PostMapping("/reservar")
    public String crearReserva(@RequestParam Long idCliente,
                               @RequestParam Long idMesa,
                               @RequestParam(required = false) String observaciones,
                               RedirectAttributes redirectAttributes) {
        try {
            asignacionService.crearReserva(idCliente, idMesa, observaciones);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva creada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asignaciones";
    }

    @GetMapping("/liberar/{id}")
    public String liberarMesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            asignacionService.liberarMesa(id);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa liberada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asignaciones";
    }

    @GetMapping("/historial")
    public String historial(Model model) {
        model.addAttribute("asignaciones", asignacionService.listarTodas());
        return "asignaciones/historial";
    }
}
