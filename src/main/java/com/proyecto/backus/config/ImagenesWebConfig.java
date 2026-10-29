package com.proyecto.backus.config;
import com.proyecto.backus.service.ImagenProductoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class ImagenesWebConfig implements WebMvcConfigurer {
 private final ImagenProductoService imagenes;
 public ImagenesWebConfig(ImagenProductoService imagenes){this.imagenes=imagenes;}
 @Override public void addResourceHandlers(ResourceHandlerRegistry registry){String uri=imagenes.directorio().toUri().toString();registry.addResourceHandler("/media/productos/**").addResourceLocations(uri.endsWith("/")?uri:uri+"/");}
}
