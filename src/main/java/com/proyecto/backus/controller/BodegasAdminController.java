package com.proyecto.backus.controller;
import com.proyecto.backus.service.BodegasAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.security.Principal;
@Controller @RequestMapping("/admin/bodegas")
public class BodegasAdminController {
 private final BodegasAdminService service;public BodegasAdminController(BodegasAdminService service){this.service=service;}
 @GetMapping public String listar(@RequestParam(name="busqueda",defaultValue="")String busqueda,@RequestParam(name="distrito",defaultValue="")String distrito,@RequestParam(name="pagina",defaultValue="0")int pagina,Model m,Principal p){m.addAttribute("adminCorreo",p.getName());m.addAttribute("adminPagina","bodegas");m.addAttribute("adminTitulo","Bodegas");m.addAttribute("busqueda",busqueda);m.addAttribute("distrito",distrito);m.addAttribute("distritos",service.distritos());m.addAttribute("bodegas",service.listar(busqueda,distrito,pagina));return "admin/bodegas";}
 @GetMapping("/{id}/detalle") @ResponseBody public BodegasAdminService.Bodega detalle(@PathVariable("id")Long id){return service.detalle(id);}
}
