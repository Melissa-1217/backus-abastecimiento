package com.proyecto.backus.controller;
import com.proyecto.backus.service.ResumenAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.security.Principal;
@Controller
public class AdminController {
 private final ResumenAdminService resumen;
 public AdminController(ResumenAdminService resumen){this.resumen=resumen;}
 @GetMapping({"/admin","/admin/"}) public String inicio(Model model,Principal principal){
  model.addAttribute("resumen",resumen.obtener());model.addAttribute("adminCorreo",principal.getName());model.addAttribute("zona","Arequipa");return "admin/resumen";
 }
}
