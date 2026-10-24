package com.proyecto.backus.controller;

import com.proyecto.backus.repository.InventarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String mostrarCatalogo(Model model) {
        String zona = "Arequipa";

        model.addAttribute("zona", zona);
        model.addAttribute(
            "inventarios",
            inventarioRepository.buscarCatalogoPorZona(zona)
        );

        return "catalogo";
    }
}