package com.proyecto.backus.controller;

import com.proyecto.backus.model.Inventario;
import com.proyecto.backus.repository.InventarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CatalogoController {

    private final InventarioRepository inventarioRepository;

    public CatalogoController(
            InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("destacados", inventarioRepository
            .buscarCatalogo("Arequipa", "", true).stream().limit(4).toList());
        return "inicio";
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
                zona,
                textoBusqueda,
                soloDisponibles
            )
        );

        return "catalogo";
    }

    @GetMapping("/catalogo/{id}")
    public String detalle(@PathVariable("id") Long id, Model model) {
        Inventario item = inventarioRepository.buscarDetalle(id, "Arequipa")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("item", item);
        model.addAttribute("zona", item.getZona());
        model.addAttribute("precioPorUnidad", item.getPresentacion().getPrecio().divide(
            java.math.BigDecimal.valueOf(item.getPresentacion().getUnidadesPorPaquete()),
            2, java.math.RoundingMode.HALF_UP));
        java.math.BigDecimal costoUnitario = item.getPresentacion().getPrecio().divide(
            java.math.BigDecimal.valueOf(item.getPresentacion().getUnidadesPorPaquete()),
            8, java.math.RoundingMode.HALF_UP);
        // Regla referencial del prototipo, no politica comercial de Backus.
        model.addAttribute("pvpReferencial", costoUnitario.divide(
            new java.math.BigDecimal("0.75"), 2, java.math.RoundingMode.CEILING));
        model.addAttribute("costoUnitarioCalculo", costoUnitario);
        model.addAttribute("otrasPresentaciones", inventarioRepository
            .buscarCatalogo(item.getZona(), "", false).stream()
            .filter(opcion -> opcion.getPresentacion().getProducto().getId()
                .equals(item.getPresentacion().getProducto().getId()))
            .toList());
        return "producto";
    }

    @GetMapping("/catalogo/{id}/alternativas")
    public String mostrarAlternativas(
            @PathVariable("id") Long id,
            Model model) {

        String zona = "Arequipa";

        Inventario seleccionado = inventarioRepository
            .buscarDetalle(id, zona)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Producto no encontrado para esta zona"
            ));

        model.addAttribute("zona", zona);
        model.addAttribute("seleccionado", seleccionado);

        model.addAttribute(
            "alternativas",
            inventarioRepository.buscarAlternativas(
                zona,
                seleccionado.getPresentacion().getProducto().getId(),
                seleccionado.getPresentacion().getId()
            )
        );

        return "alternativas";
    }
}