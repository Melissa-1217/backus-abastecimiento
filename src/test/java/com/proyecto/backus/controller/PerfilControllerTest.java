package com.proyecto.backus.controller;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import jakarta.validation.Validation;
import java.lang.reflect.Proxy;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
class PerfilControllerTest {
 @Test void actualizaSoloPerfilDelUsuarioAutenticado(){
  Usuario usuario=new Usuario();usuario.setCorreo("ana@demo.pe");usuario.setPasswordHash("hash-existente");
  Usuario[] guardado={null};
  UsuarioRepository repo=(UsuarioRepository)Proxy.newProxyInstance(UsuarioRepository.class.getClassLoader(),new Class<?>[]{UsuarioRepository.class},(proxy,method,args)->{
   if(method.getName().equals("findByCorreo")){assertEquals("ana@demo.pe",args[0]);return Optional.of(usuario);}
   if(method.getName().equals("saveAndFlush")){guardado[0]=(Usuario)args[0];return guardado[0];}
   throw new UnsupportedOperationException(method.getName());
  });
  PerfilForm f=new PerfilForm();f.setNombre(" Ana Perez ");f.setBodega("Bodega Ana");f.setTelefono("954123456");f.setRuc("20123456789");f.setDireccion("Calle 123");f.setDistrito("Cayma");f.setProvincia("Arequipa");f.setDepartamento("Arequipa");f.setReferencia("");
  var errors=new BeanPropertyBindingResult(f,"perfil");
  var controller=new CuentaController(repo,new BCryptPasswordEncoder());
  assertEquals("redirect:/cuenta?actualizada",controller.guardar(f,errors,()->"ana@demo.pe"));
  assertSame(usuario,guardado[0]);assertEquals("Ana Perez",usuario.getNombre());assertEquals("Cayma",usuario.getDistrito());assertEquals("20123456789",usuario.getRuc());assertEquals("ana@demo.pe",usuario.getCorreo());assertEquals("hash-existente",usuario.getPasswordHash());
 }
 @Test void validaRucOpcionalYLimites(){
  try(var factory=Validation.buildDefaultValidatorFactory()){
   var validator=factory.getValidator();PerfilForm f=new PerfilForm();f.setNombre("Ana");f.setBodega("Bodega");f.setRuc("");f.setTelefono("");
   assertTrue(validator.validate(f).isEmpty());f.setRuc("123");assertTrue(validator.validate(f).stream().anyMatch(v->v.getPropertyPath().toString().equals("ruc")));
   f.setRuc("20123456789");f.setDireccion("a".repeat(201));assertTrue(validator.validate(f).stream().anyMatch(v->v.getPropertyPath().toString().equals("direccion")));
  }
 }
}
