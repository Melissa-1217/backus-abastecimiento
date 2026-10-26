package com.proyecto.backus.controller;

import com.proyecto.backus.model.Inventario;
import com.proyecto.backus.repository.InventarioRepository;
import com.proyecto.backus.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarritoController {

    private final CarritoService carritoService;
    private final InventarioRepository inventarioRepository;

    public CarritoController(
            CarritoService carritoService,
            InventarioRepository inventarioRepository) {

        this.carritoService = carritoService;
        this.inventarioRepository = inventarioRepository;
    }

    @PostMapping("/carrito/agregar")
    public String agregar(
            @RequestParam("inventarioId") Long inventarioId,
            @RequestParam("cantidad") int cantidad,
            RedirectAttributes mensajes) {

        try {
            if (cantidad <= 0) {
                throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
                );
            }

            Inventario inventario = inventarioRepository
                .buscarDetalle(inventarioId, "Arequipa")
                .orElseThrow(() -> new IllegalArgumentException(
                    "Producto no disponible para esta zona."
                ));

            int cantidadActual = carritoService.obtenerItems()
                .getOrDefault(inventarioId, 0);

            long cantidadTotal = (long) cantidadActual + cantidad;

            if (cantidadTotal > inventario.getCantidad()) {
                throw new IllegalArgumentException(
                    "La cantidad acumulada supera el stock disponible."
                );
            }

            carritoService.agregar(inventarioId, cantidad);

            mensajes.addFlashAttribute(
                "mensaje",
                "Producto agregado al pedido."
            );

        } catch (IllegalArgumentException ex) {
            mensajes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/catalogo";
    }
}