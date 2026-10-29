package com.proyecto.backus.controller;

import com.proyecto.backus.model.Inventario;
import com.proyecto.backus.repository.InventarioRepository;
import com.proyecto.backus.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import com.proyecto.backus.service.PedidoService;
import com.proyecto.backus.repository.PedidoRepository;
import com.proyecto.backus.repository.DetallePedidoRepository;
import com.proyecto.backus.model.Pedido;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarritoController {

    private final CarritoService carritoService;
    private final InventarioRepository inventarioRepository;

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public CarritoController(
            CarritoService carritoService,
            InventarioRepository inventarioRepository,
            PedidoService pedidoService,
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository) {

        this.pedidoService = pedidoService;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.carritoService = carritoService;
        this.inventarioRepository = inventarioRepository;
    }

    @PostMapping(value = "/carrito/agregar", produces = "text/html")
    public String agregar(
            @RequestParam("inventarioId") Long inventarioId,
            @RequestParam("cantidad") int cantidad,
            RedirectAttributes mensajes) {

        try {
            agregarValidado(inventarioId, cantidad);

            mensajes.addFlashAttribute(
                "mensaje",
                "Producto agregado al pedido."
            );

        } catch (IllegalArgumentException ex) {
            mensajes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/catalogo";
    }

    private void agregarValidado(Long inventarioId, int cantidad) {
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

    }

    @PostMapping(value = "/carrito/agregar", produces = "application/json")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Map<String, Object>> agregarSinRecarga(
            @RequestParam("inventarioId") Long inventarioId,
            @RequestParam("cantidad") int cantidad) {
        try {
            agregarValidado(inventarioId, cantidad);
            long total = carritoService.obtenerItems().values().stream().mapToLong(Integer::longValue).sum();
            return org.springframework.http.ResponseEntity.ok(Map.of(
                "mensaje", "Producto agregado al pedido", "cantidadCarrito", total));
        } catch (IllegalArgumentException ex) {
            return org.springframework.http.ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
        }
    }

    private boolean prepararVista(Model model) {
        List<Map<String, Object>> lineas = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        long bultos = 0;
        boolean valido = true;
        for (var entrada : carritoService.obtenerItems().entrySet()) {
            Inventario item = inventarioRepository.buscarDetalle(entrada.getKey(), "Arequipa").orElse(null);
            Map<String, Object> linea = new LinkedHashMap<>();
            linea.put("id", entrada.getKey());
            linea.put("item", item);
            linea.put("cantidad", entrada.getValue());
            boolean disponible = item != null && item.getCantidad() >= entrada.getValue();
            linea.put("valido", disponible);
            BigDecimal subtotal = item == null ? BigDecimal.ZERO : item.getPresentacion().getPrecio()
                .multiply(BigDecimal.valueOf(entrada.getValue()));
            linea.put("subtotal", subtotal);
            lineas.add(linea);
            total = total.add(subtotal);
            bultos += entrada.getValue();
            valido &= disponible;
        }
        model.addAttribute("lineas", lineas);
        model.addAttribute("total", total);
        model.addAttribute("bultos", bultos);
        model.addAttribute("zona", "Arequipa");
        model.addAttribute("carritoValido", valido && !lineas.isEmpty());
        return valido && !lineas.isEmpty();
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model, java.security.Principal principal) {
        model.addAttribute("datosPendientes", pedidoService.datosPendientes(principal == null ? null : principal.getName()));
        model.addAttribute("requiereSesion", principal == null);
        prepararVista(model);
        model.addAttribute("revision", false);
        return "carrito";
    }

    @PostMapping("/carrito/actualizar")
    public String actualizar(@RequestParam("inventarioId") Long id,
            @RequestParam("cantidad") int cantidad, RedirectAttributes mensajes) {
        try {
            Inventario item = inventarioRepository.buscarDetalle(id, "Arequipa")
                .orElseThrow(() -> new IllegalArgumentException("La presentación ya no está disponible."));
            if (cantidad <= 0 || cantidad > item.getCantidad()) {
                throw new IllegalArgumentException("Selecciona una cantidad entre 1 y " + item.getCantidad() + ".");
            }
            carritoService.actualizar(id, cantidad);
            mensajes.addFlashAttribute("mensaje", "Cantidad actualizada.");
        } catch (IllegalArgumentException ex) {
            mensajes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/quitar")
    public String quitar(@RequestParam("inventarioId") Long id, RedirectAttributes mensajes) {
        carritoService.quitar(id);
        mensajes.addFlashAttribute("mensaje", "Producto retirado del carrito.");
        return "redirect:/carrito";
    }

    @GetMapping("/carrito/revisar")
    public String revisar(Model model, HttpSession session, RedirectAttributes mensajes, java.security.Principal principal) {
        var pendientes = pedidoService.datosPendientes(principal == null ? null : principal.getName());
        if (!pendientes.isEmpty()) {
            mensajes.addFlashAttribute("error", "Completa tus datos antes de revisar el pedido. Tu carrito se conserva.");
            return "redirect:/carrito";
        }
        if (!prepararVista(model)) {
            mensajes.addFlashAttribute("error", "Revisa el carrito: hay productos sin stock suficiente o está vacío.");
            return "redirect:/carrito";
        }
        String token = UUID.randomUUID().toString();
        session.setAttribute("revisionToken", token);
        session.setAttribute("revisionItems", carritoService.obtenerItems());
        session.setAttribute("revisionTotal", model.getAttribute("total"));
        model.addAttribute("token", token);
        model.addAttribute("revision", true);
        return "revisar-pedido";
    }

    @PostMapping("/carrito/confirmar")
    public String confirmar(@RequestParam("token") String token, HttpSession session, java.security.Principal principal,
            RedirectAttributes mensajes) {
        synchronized (session) {
            if (!token.equals(session.getAttribute("revisionToken")) ||
                    !carritoService.obtenerItems().equals(session.getAttribute("revisionItems"))) {
                mensajes.addFlashAttribute("error", "Vuelve a revisar el pedido antes de confirmar.");
                return "redirect:/carrito";
            }
            session.removeAttribute("revisionToken");
            try {
                Pedido pedido = pedidoService.confirmar(carritoService.obtenerItems(), "Arequipa",
                    (BigDecimal) session.getAttribute("revisionTotal"),principal==null?null:principal.getName());
                carritoService.vaciar();
                session.setAttribute("ultimoPedidoId", pedido.getId());
                return "redirect:/pedido-confirmado";
            } catch (IllegalArgumentException ex) {
                mensajes.addFlashAttribute("error", ex.getMessage());
                return "redirect:/carrito";
            }
        }
    }

    @GetMapping("/pedido-confirmado")
    public String confirmado(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("ultimoPedidoId");
        if (id == null) return "redirect:/carrito";
        Pedido pedido = pedidoRepository.findById(id).orElseThrow();
        var detalles = detallePedidoRepository.buscarDetallesDelPedido(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("numeroPedido", String.format("DEMO-%04d", pedido.getId()));
        model.addAttribute("fechaPedido", pedido.getFecha().atZoneSameInstant(
            java.time.ZoneId.of("America/Lima")).format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        model.addAttribute("bultosPedido", detalles.stream().mapToLong(d -> d.getCantidad()).sum());
        model.addAttribute("zona", "Arequipa");
        return "pedido-confirmado";
    }
}
