package com.proyecto.backus.controller;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import java.lang.reflect.Proxy;
import static org.junit.jupiter.api.Assertions.*;
class CuentaControllerTest {
 static class Datos {
  Usuario guardado; boolean duplicado;
  UsuarioRepository repository() {return (UsuarioRepository)Proxy.newProxyInstance(UsuarioRepository.class.getClassLoader(), new Class<?>[]{UsuarioRepository.class}, (proxy,method,args)->{if(method.getName().equals("existsByCorreo"))return duplicado;if(method.getName().equals("saveAndFlush")){guardado=(Usuario)args[0];return guardado;}throw new UnsupportedOperationException(method.getName());});}
 }
 private RegistroForm form(){RegistroForm f=new RegistroForm();f.setNombre("Ana Perez");f.setBodega("Mi Bodega");f.setCorreo(" ANA@EJEMPLO.PE ");f.setPassword("Demo-prueba-123");f.setConfirmacion("Demo-prueba-123");return f;}
 @Test void guardaHashYNormalizaCorreo(){var datos=new Datos();var encoder=new BCryptPasswordEncoder();var controller=new CuentaController(datos.repository(),encoder);var form=form();var errors=new BeanPropertyBindingResult(form,"registro");assertEquals("redirect:/login?registro",controller.registrar(form,errors));assertEquals("ana@ejemplo.pe",datos.guardado.getCorreo());assertNotEquals(form.getPassword(),datos.guardado.getPasswordHash());assertTrue(encoder.matches(form.getPassword(),datos.guardado.getPasswordHash()));}
 @Test void rechazaConfirmacionDistinta(){var datos=new Datos();var controller=new CuentaController(datos.repository(),new BCryptPasswordEncoder());var form=form();form.setConfirmacion("otra");var errors=new BeanPropertyBindingResult(form,"registro");assertEquals("registro",controller.registrar(form,errors));assertTrue(errors.hasFieldErrors("confirmacion"));assertNull(datos.guardado);assertEquals("",form.getPassword());}
 @Test void rechazaCorreoDuplicado(){var datos=new Datos();datos.duplicado=true;var controller=new CuentaController(datos.repository(),new BCryptPasswordEncoder());var form=form();var errors=new BeanPropertyBindingResult(form,"registro");assertEquals("registro",controller.registrar(form,errors));assertTrue(errors.hasFieldErrors("correo"));assertNull(datos.guardado);}
}
