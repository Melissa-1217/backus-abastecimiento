package com.proyecto.backus.repository;
import com.proyecto.backus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario,Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Usuario> {
 Optional<Usuario> findByCorreo(String correo);
 boolean existsByCorreo(String correo);
 boolean existsByRol(String rol);
 @org.springframework.data.jpa.repository.Query("select distinct u.distrito from Usuario u where u.rol = 'BODEGUERO' and u.distrito is not null and trim(u.distrito) <> '' order by u.distrito")
 java.util.List<String> distritosDeBodegueros();
}
