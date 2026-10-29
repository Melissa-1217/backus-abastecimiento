package com.proyecto.backus.config;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.proyecto.backus.repository.UsuarioRepository;
import com.proyecto.backus.model.Usuario;
import java.lang.reflect.Proxy;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
class AdminAccesoTest {
 UsuarioRepository repo(String rol){return (UsuarioRepository)Proxy.newProxyInstance(UsuarioRepository.class.getClassLoader(),new Class<?>[]{UsuarioRepository.class},(p,m,a)->{if(m.getName().equals("findByCorreo")){assertEquals("admin@demo.pe",a[0]);Usuario u=new Usuario();u.setCorreo((String)a[0]);u.setPasswordHash("hash-guardado");u.setRol(rol);return Optional.of(u);}throw new UnsupportedOperationException();});}
 @Test void leeRolAdminYHashDeLaBase(){var u=new SeguridadConfig().userDetailsService(repo("ADMIN")).loadUserByUsername(" ADMIN@DEMO.PE ");assertTrue(u.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")));assertEquals("hash-guardado",u.getPassword());}
 @Test void cuentaNuevaNoEsAdmin(){assertEquals("BODEGUERO",new Usuario().getRol());var u=new SeguridadConfig().userDetailsService(repo("BODEGUERO")).loadUserByUsername("admin@demo.pe");assertTrue(u.getAuthorities().stream().noneMatch(a->a.getAuthority().equals("ROLE_ADMIN")));}
 @Test void rechazaRolesDesconocidos(){assertThrows(UsernameNotFoundException.class,()->new SeguridadConfig().userDetailsService(repo("OTRO")).loadUserByUsername("admin@demo.pe"));}
}
