package com.proyecto.backus.config;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.lang.reflect.Proxy;
import static org.junit.jupiter.api.Assertions.*;
class AdministradorInicialTest {
 Usuario guardado; boolean existeAdmin,existeCorreo;
 UsuarioRepository repo(){return (UsuarioRepository)Proxy.newProxyInstance(UsuarioRepository.class.getClassLoader(),new Class<?>[]{UsuarioRepository.class},(p,m,a)->switch(m.getName()){case "existsByRol"->existeAdmin;case "existsByCorreo"->existeCorreo;case "saveAndFlush"->{guardado=(Usuario)a[0];yield guardado;}default->throw new UnsupportedOperationException();});}
 MockEnvironment env(){return new MockEnvironment().withProperty("BACKUS_CREAR_ADMIN","true").withProperty("BACKUS_ADMIN_INICIAL_PASSWORD","Prueba-inicial-123");}
 @Test void creaCuentaAdminConHash(){var encoder=new BCryptPasswordEncoder();new AdministradorInicial(repo(),encoder,env()).run(null);assertEquals("ADMIN",guardado.getRol());assertEquals("admin@backus.demo",guardado.getCorreo());assertTrue(encoder.matches("Prueba-inicial-123",guardado.getPasswordHash()));}
 @Test void noReemplazaAdministradorExistente(){existeAdmin=true;new AdministradorInicial(repo(),new BCryptPasswordEncoder(),env()).run(null);assertNull(guardado);}
 @Test void noPromueveCuentaRegistrada(){existeCorreo=true;assertThrows(IllegalStateException.class,()->new AdministradorInicial(repo(),new BCryptPasswordEncoder(),env()).run(null));assertNull(guardado);}
 @Test void noCreaNadaEnArranqueNormal(){new AdministradorInicial(repo(),new BCryptPasswordEncoder(),new MockEnvironment()).run(null);assertNull(guardado);}
}
