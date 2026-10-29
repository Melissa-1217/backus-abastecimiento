package com.proyecto.backus.controller;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Locale;
@Controller
public class CuentaController {
 private final UsuarioRepository usuarios; private final PasswordEncoder encoder;
 public CuentaController(UsuarioRepository usuarios,PasswordEncoder encoder){this.usuarios=usuarios;this.encoder=encoder;}
 @GetMapping("/login") public String login(){return "login";}
 @GetMapping({"/registro","/register"}) public String registro(Model model){model.addAttribute("registro",new RegistroForm());return "registro";}
 @PostMapping("/registro") public String registrar(@Valid @ModelAttribute("registro") RegistroForm form,BindingResult errors){
  if(!form.getPassword().equals(form.getConfirmacion()))errors.rejectValue("confirmacion","distinta","Las contraseñas no coinciden.");
  if(form.getPassword().getBytes(StandardCharsets.UTF_8).length>72)errors.rejectValue("password","longitud","La contraseña supera el tamaño permitido; usa menos caracteres.");
  String correo=form.getCorreo().trim().toLowerCase(Locale.ROOT);
  if(usuarios.existsByCorreo(correo))errors.rejectValue("correo","duplicado","Ya existe una cuenta con este correo.");
  if(errors.hasErrors()){form.setPassword("");form.setConfirmacion("");return "registro";}
  Usuario u=new Usuario();u.setRol("BODEGUERO");u.setNombre(form.getNombre().trim());u.setBodega(form.getBodega().trim());u.setCorreo(correo);u.setPasswordHash(encoder.encode(form.getPassword()));
  try{usuarios.saveAndFlush(u);}catch(DataIntegrityViolationException ex){errors.rejectValue("correo","duplicado","No se pudo registrar este correo.");form.setPassword("");form.setConfirmacion("");return "registro";}
  return "redirect:/login?registro";
 }
 @GetMapping("/cuenta") public String cuenta(Principal principal,Model model){model.addAttribute("usuario",usuarios.findByCorreo(principal.getName()).orElseThrow());return "cuenta";}

 @GetMapping("/cuenta/editar") public String editar(Principal principal,Model model){
 Usuario u=usuarios.findByCorreo(principal.getName()).orElseThrow(); PerfilForm f=new PerfilForm();
 f.setNombre(u.getNombre());
 f.setBodega(u.getBodega());
 f.setTelefono(u.getTelefono());
 f.setRuc(u.getRuc());
 f.setDireccion(u.getDireccion());
 f.setDistrito(u.getDistrito());
 f.setProvincia(u.getProvincia());
 f.setDepartamento(u.getDepartamento());
 f.setReferencia(u.getReferencia());
model.addAttribute("perfil",f);return "editar-cuenta";}
 @PostMapping("/cuenta/editar") public String guardar(@Valid @ModelAttribute("perfil") PerfilForm f,BindingResult errors,Principal principal){
 if(errors.hasErrors())return "editar-cuenta";
 Usuario u=usuarios.findByCorreo(principal.getName()).orElseThrow();
 u.setNombre(f.getNombre());
 u.setBodega(f.getBodega());
 u.setTelefono(f.getTelefono());
 u.setRuc(f.getRuc());
 u.setDireccion(f.getDireccion());
 u.setDistrito(f.getDistrito());
 u.setProvincia(f.getProvincia());
 u.setDepartamento(f.getDepartamento());
 u.setReferencia(f.getReferencia());
usuarios.saveAndFlush(u);return "redirect:/cuenta?actualizada";}
}
