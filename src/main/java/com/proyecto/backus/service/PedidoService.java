package com.proyecto.backus.service;

import com.proyecto.backus.model.DetallePedido;
import com.proyecto.backus.model.Inventario;
import com.proyecto.backus.model.Pedido;
import com.proyecto.backus.repository.DetallePedidoRepository;
import com.proyecto.backus.repository.InventarioRepository;
import com.proyecto.backus.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PedidoService {

    private final InventarioRepository inventarioRepository;
    private final com.proyecto.backus.repository.UsuarioRepository usuarios;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoService(
            InventarioRepository inventarioRepository,
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository, com.proyecto.backus.repository.UsuarioRepository usuarios) {
        this.usuarios=usuarios;

        this.inventarioRepository = inventarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<String> datosPendientes(String correo) {
        if (correo == null) return List.of("Iniciar sesión con una cuenta de bodeguero");
        var u = usuarios.findByCorreo(correo).filter(v -> "BODEGUERO".equals(v.getRol())).orElse(null);
        if (u == null) return List.of("Iniciar sesión con una cuenta de bodeguero");
        return camposPendientes(u);
    }

    public static List<String> camposPendientes(com.proyecto.backus.model.Usuario u) {
        List<String> faltantes = new ArrayList<>();
        String telefono = u.getTelefono() == null ? "" : u.getTelefono();
        if (!telefono.matches("[0-9+() -]+") || telefono.replaceAll("[^0-9]", "").length() < 9
                || telefono.replaceAll("[^0-9]", "").length() > 15) faltantes.add("Teléfono de contacto válido (9 a 15 dígitos)");
        if (u.getRuc() == null || !u.getRuc().matches("[0-9]{11}")) faltantes.add("RUC de 11 dígitos");
        if (u.getDireccion() == null || u.getDireccion().isBlank()) faltantes.add("Dirección de la bodega");
        if (u.getDistrito() == null || u.getDistrito().isBlank()) faltantes.add("Distrito");
        if (u.getProvincia() == null || u.getProvincia().isBlank()) faltantes.add("Provincia");
        if (u.getDepartamento() == null || u.getDepartamento().isBlank()) faltantes.add("Departamento");
        return faltantes;
    }

    @Transactional
    public Pedido confirmar(
            Map<Long, Integer> items,
            String zona, BigDecimal totalRevisado, String correoUsuario) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                "Agrega productos antes de confirmar el pedido."
            );
        }

        if (zona == null || zona.isBlank()) {
            throw new IllegalArgumentException(
                "La zona de atención es obligatoria."
            );
        }

        for (Map.Entry<Long, Integer> item : items.entrySet()) {
            if (item.getKey() == null || item.getKey() <= 0
                    || item.getValue() == null || item.getValue() <= 0) {

                throw new IllegalArgumentException(
                    "El pedido contiene un producto o cantidad inválidos."
                );
            }
        }

        List<String> pendientes = datosPendientes(correoUsuario);
        if (!pendientes.isEmpty()) throw new IllegalArgumentException(
            "Antes de confirmar, completa: " + String.join(", ", pendientes) + ". Tu carrito se conserva.");

        Pedido pedido = new Pedido();
        pedido.setZona(zona);
        if(correoUsuario!=null)usuarios.findByCorreo(correoUsuario).filter(u->"BODEGUERO".equals(u.getRol())).ifPresent(u->{
            pedido.setUsuarioId(u.getId());pedido.setBodegaNombre(u.getBodega());pedido.setResponsable(u.getNombre());pedido.setCorreoContacto(u.getCorreo());pedido.setTelefonoContacto(u.getTelefono());
            pedido.setDireccionEntrega(java.util.stream.Stream.of(u.getDireccion(),u.getDistrito(),u.getProvincia(),u.getDepartamento()).filter(v->v!=null&&!v.isBlank()).collect(java.util.stream.Collectors.joining(", ")));
        });

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        // Bloqueamos los registros siempre en el mismo orden.
        Map<Long, Integer> itemsOrdenados = new TreeMap<>(items);

        for (Map.Entry<Long, Integer> item : itemsOrdenados.entrySet()) {

            Inventario inventario = inventarioRepository
                .buscarParaConfirmar(item.getKey(), zona)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Una presentación ya no está disponible en esta zona."
                ));

            if (!inventario.getPresentacion().isActivo()
                    || !inventario.getPresentacion()
                        .getProducto().isActivo()) {

                throw new IllegalArgumentException(
                    "Uno de los productos ya no está activo."
                );
            }

            int cantidad = item.getValue();

            if (cantidad > inventario.getCantidad()) {
                throw new IllegalArgumentException(
                    "Stock insuficiente para "
                    + inventario.getPresentacion()
                        .getProducto().getNombre()
                    + " — "
                    + inventario.getPresentacion().getDescripcion()
                    + ". Disponible: "
                    + inventario.getCantidad()
                );
            }

            BigDecimal precio = inventario
                .getPresentacion().getPrecio();

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setInventario(inventario);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precio);

            detalles.add(detalle);
            total = total.add(detalle.getSubtotal());

            inventario.setCantidad(
                inventario.getCantidad() - cantidad
            );
        }

        if (totalRevisado == null || total.compareTo(totalRevisado) != 0) {
            throw new IllegalArgumentException("Los precios cambiaron. Revisa nuevamente el total antes de confirmar.");
        }
        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        detallePedidoRepository.saveAll(detalles);

        return pedidoGuardado;
    }
}