package com.proyecto.backus.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.proyecto.backus.repository.UsuarioRepository;
import java.util.Locale;
@Configuration
public class SeguridadConfig {
 @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 @Bean UserDetailsService userDetailsService(UsuarioRepository repository) {
  return correo -> repository.findByCorreo(correo.trim().toLowerCase(Locale.ROOT))
   .map(u -> {
    if(!"ADMIN".equals(u.getRol()) && !"BODEGUERO".equals(u.getRol()))
     throw new UsernameNotFoundException("Rol de cuenta no válido");
    return User.withUsername(u.getCorreo()).password(u.getPasswordHash()).roles(u.getRol()).build();
   }).orElseThrow(() -> new UsernameNotFoundException("Credenciales incorrectas"));
 }
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http.authorizeHttpRequests(auth -> auth.requestMatchers("/admin", "/admin/**").hasRole("ADMIN").requestMatchers("/cuenta", "/cuenta/**").authenticated().anyRequest().permitAll())
   .formLogin(form -> form.loginPage("/login").usernameParameter("correo").passwordParameter("password")
    .successHandler((request,response,authentication)->{
     boolean admin=authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
     response.sendRedirect(request.getContextPath()+(admin?"/admin":"/cuenta"));
    }).failureUrl("/login?error").permitAll())
   .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?salida").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
  return http.build();
 }
}
