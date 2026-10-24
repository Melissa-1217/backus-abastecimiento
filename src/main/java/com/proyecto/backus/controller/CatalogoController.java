package com.proyecto.backus.controller;

import com.proyecto.backus.repository.InventarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogoController {

    private final InventarioRepository inventarioRepository;

    public CatalogoController(
            InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/catalogo";
    }

    @GetMapping("/catalogo")
    public String mostrarCatalogo(
            @RequestParam(name = "busqueda", defaultValue = "")
            String busqueda,
            @RequestParam(name = "soloDisponibles", defaultValue = "false")
            boolean soloDisponibles,
            Model model) {

        String zona = "Arequipa";
        String textoBusqueda = busqueda.trim();

        model.addAttribute("zona", zona);
        model.addAttribute("busqueda", textoBusqueda);
        model.addAttribute("soloDisponibles", soloDisponibles);
        model.addAttribute(
            "inventarios",
            inventarioRepository.buscarCatalogo(
                zona, textoBusqueda, soloDisponibles
            )
        );

        return "catalogo";
    }
}