package com.proyecto.backus.controller;
import com.proyecto.backus.service.InventarioAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.security.Principal;
@Controller @RequestMapping("/admin/inventario")
public class InventarioAdminController {
 private final InventarioAdminService service;
 public InventarioAdminController(InventarioAdminService service){this.service=service;}
 @GetMapping public String inicio(Model m,Principal p){var lista=service.listar();m.addAttribute("inventarios",lista);m.addAttribute("zonas",lista.stream().map(i->i.getZona()).distinct().sorted().toList());m.addAttribute("disponibles",lista.stream().filter(i->i.getCantidad()>5).count());m.addAttribute("bajos",lista.stream().filter(i->i.getCantidad()>0&&i.getCantidad()<=5).count());m.addAttribute("agotados",lista.stream().filter(i->i.getCantidad()==0).count());m.addAttribute("adminCorreo",p.getName());m.addAttribute("adminPagina","inventario");m.addAttribute("adminTitulo","Inventario");m.addAttribute("ajuste",new AjusteStockForm());return "admin/inventario";}
 @PostMapping("/ajustar") public String ajustar(@Valid @ModelAttribute("ajuste")AjusteStockForm f,BindingResult errors,Principal p,RedirectAttributes flash){if(errors.hasErrors()){flash.addFlashAttribute("error","Revisa la cantidad y escribe un motivo de hasta 300 caracteres.");return "redirect:/admin/inventario";}try{service.ajustar(f,p.getName());flash.addFlashAttribute("mensaje","Stock actualizado correctamente.");}catch(IllegalArgumentException ex){flash.addFlashAttribute("error",ex.getMessage());}return "redirect:/admin/inventario";}
}
