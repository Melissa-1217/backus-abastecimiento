package com.proyecto.backus.config;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
@Component
public class AdministradorInicial implements ApplicationRunner {
 private final UsuarioRepository usuarios;private final PasswordEncoder encoder;private final Environment env;
 public AdministradorInicial(UsuarioRepository usuarios,PasswordEncoder encoder,Environment env){this.usuarios=usuarios;this.encoder=encoder;this.env=env;}
 @Override @Transactional public void run(ApplicationArguments args){
  if(!env.getProperty("BACKUS_CREAR_ADMIN",Boolean.class,false))return;
  if(usuarios.existsByRol("ADMIN"))return; // Nunca reemplaza cuentas ni contraseñas existentes.
  String correo=env.getProperty("BACKUS_ADMIN_INICIAL_CORREO","admin@backus.demo").trim().toLowerCase(Locale.ROOT);
  String password=env.getProperty("BACKUS_ADMIN_INICIAL_PASSWORD","");
  if(!correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))throw new IllegalStateException("El correo del administrador inicial no es válido.");
  if(password.length()<12||password.getBytes(StandardCharsets.UTF_8).length>72)throw new IllegalStateException("La contraseña inicial debe tener al menos 12 caracteres y no superar 72 bytes.");
  if(usuarios.existsByCorreo(correo))throw new IllegalStateException("Ese correo ya pertenece a una cuenta. La configuración inicial no convierte bodegueros en administradores; usa otro correo.");
  Usuario admin=new Usuario();admin.setNombre("Administrador principal");admin.setBodega("Administración del sistema");admin.setCorreo(correo);admin.setPasswordHash(encoder.encode(password));admin.setRol("ADMIN");usuarios.saveAndFlush(admin);
 }
}
