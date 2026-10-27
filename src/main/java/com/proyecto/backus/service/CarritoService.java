package com.proyecto.backus.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@SessionScope
public class CarritoService {

    private final Map<Long, Integer> items = new LinkedHashMap<>();

    public synchronized void agregar(Long inventarioId, int cantidad) {
        validar(inventarioId, cantidad);

        int cantidadActual = items.getOrDefault(inventarioId, 0);
        int nuevaCantidad = Math.addExact(cantidadActual, cantidad);

        items.put(inventarioId, nuevaCantidad);
    }

    public synchronized void actualizar(Long inventarioId, int cantidad) {
        validar(inventarioId, cantidad);

        if (!items.containsKey(inventarioId)) {
            throw new IllegalArgumentException(
                "El producto no está en el pedido."
            );
        }

        items.put(inventarioId, cantidad);
    }

    public synchronized void quitar(Long inventarioId) {
        items.remove(inventarioId);
    }

    public synchronized Map<Long, Integer> obtenerItems() {
        return new LinkedHashMap<>(items);
    }

    public synchronized void vaciar() {
        items.clear();
    }

    private void validar(Long inventarioId, int cantidad) {
        if (inventarioId == null || inventarioId <= 0) {
            throw new IllegalArgumentException(
                "Selecciona un producto válido."
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                "La cantidad debe ser mayor que cero."
            );
        }
    }
}