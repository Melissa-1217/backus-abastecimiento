package com.proyecto.backus.service;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.lang.reflect.Proxy;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class BodegasAdminServiceTest {
 UsuarioRepository repo(Usuario usuario){return (UsuarioRepository)Proxy.newProxyInstance(UsuarioRepository.class.getClassLoader(),new Class<?>[]{UsuarioRepository.class},(p,m,a)->{if(m.getName().equals("findById"))return Optional.ofNullable(usuario);throw new UnsupportedOperationException();});}
 @Test void devuelveDatosDelNegocioSinCredenciales(){var u=new Usuario();u.setNombre("Ana Perez");u.setBodega("Bodega Ana");u.setCorreo("ana@demo.pe");u.setPasswordHash("hash-privado");u.setTelefono("999111222");var dto=new BodegasAdminService(repo(u)).detalle(1L);assertEquals("Bodega Ana",dto.nombre());assertEquals("Ana Perez",dto.responsable());assertEquals("999111222",dto.telefono());var campos=Arrays.stream(dto.getClass().getRecordComponents()).map(c->c.getName()).toList();assertFalse(campos.contains("passwordHash"));assertFalse(campos.contains("rol"));}
 @Test void completaCamposPendientes(){var u=new Usuario();u.setDistrito(" ");var d=BodegasAdminService.convertir(u);assertEquals("Sin registrar",d.distrito());assertEquals("Sin registrar",d.ruc());assertEquals("Sin registrar",d.referencia());}
 @Test void administradorNoApareceComoBodega(){var u=new Usuario();u.setRol("ADMIN");var ex=assertThrows(ResponseStatusException.class,()->new BodegasAdminService(repo(u)).detalle(1L));assertEquals(404,ex.getStatusCode().value());}
 @Test void cuentaInexistenteDevuelve404(){var ex=assertThrows(ResponseStatusException.class,()->new BodegasAdminService(repo(null)).detalle(999L));assertEquals(404,ex.getStatusCode().value());}
}
