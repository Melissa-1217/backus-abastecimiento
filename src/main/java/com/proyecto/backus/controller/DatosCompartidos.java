package com.proyecto.backus.controller;

import com.proyecto.backus.service.CarritoService;
import com.proyecto.backus.repository.UsuarioRepository;
import java.security.Principal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class DatosCompartidos {
    private final CarritoService carritoService;
    private final UsuarioRepository usuarios;
    public DatosCompartidos(CarritoService carritoService, UsuarioRepository usuarios) {
        this.usuarios = usuarios;
        this.carritoService = carritoService;
    }
    @ModelAttribute("cantidadCarrito")
    public long cantidadCarrito() {
        return carritoService.obtenerItems().values().stream()
            .mapToLong(Integer::longValue).sum();
    }
    @ModelAttribute("nombreSesion")
    public String nombreSesion(Principal principal) {
        if (principal == null) return null;
        return usuarios.findByCorreo(principal.getName())
            .map(usuario -> usuario.getNombre().trim().split("\\s+", 2)[0])
            .orElse(null);
    }
}
