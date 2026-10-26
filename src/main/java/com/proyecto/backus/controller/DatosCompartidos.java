package com.proyecto.backus.controller;

import com.proyecto.backus.service.CarritoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class DatosCompartidos {
    private final CarritoService carritoService;
    public DatosCompartidos(CarritoService carritoService) {
        this.carritoService = carritoService;
    }
    @ModelAttribute("cantidadCarrito")
    public long cantidadCarrito() {
        return carritoService.obtenerItems().values().stream()
            .mapToLong(Integer::longValue).sum();
    }
}
