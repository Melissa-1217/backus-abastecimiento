package com.proyecto.backus.controller;
import com.proyecto.backus.service.PedidosAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.security.Principal;
@Controller @RequestMapping("/admin/pedidos")
public class PedidosAdminController {
 private final PedidosAdminService service;public PedidosAdminController(PedidosAdminService service){this.service=service;}
 @GetMapping public String listar(@RequestParam(defaultValue="",name="busqueda")String busqueda,@RequestParam(required=false,name="desde")@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate desde,@RequestParam(required=false,name="hasta")@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate hasta,@RequestParam(defaultValue="0",name="pagina")int pagina,Model m,Principal p){m.addAttribute("adminCorreo",p.getName());m.addAttribute("adminPagina","pedidos");m.addAttribute("adminTitulo","Pedidos");m.addAttribute("busqueda",busqueda);m.addAttribute("desde",desde);m.addAttribute("hasta",hasta);try{m.addAttribute("pedidos",service.listar(busqueda,desde,hasta,pagina));}catch(IllegalArgumentException ex){m.addAttribute("error",ex.getMessage());m.addAttribute("pedidos",org.springframework.data.domain.Page.empty());}return "admin/pedidos";}
 @GetMapping("/{id}/detalle") @ResponseBody public PedidosAdminService.Detalle detalle(@PathVariable("id")Long id){return service.detalle(id);}
 @GetMapping("/{id}/imprimir") public String imprimir(@PathVariable("id")Long id,Model m){m.addAttribute("pedido",service.detalle(id));return "admin/pedido-imprimir";}
}
