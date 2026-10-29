package com.proyecto.backus.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;
@Service
public class ImagenProductoService {
 private final Path directorio;
 public ImagenProductoService(@Value("${backus.imagenes.directorio:uploads/productos}")String directorio){this.directorio=Path.of(directorio).toAbsolutePath().normalize();}
 public Path directorio(){return directorio;}
 public String guardar(MultipartFile archivo){
  if(archivo.getSize()>5*1024*1024)throw new IllegalArgumentException("La imagen no debe superar 5 MB.");
  try(var input=ImageIO.createImageInputStream(archivo.getInputStream())){
   if(input==null)throw new IllegalArgumentException("Selecciona una imagen PNG o JPG válida.");
   var readers=ImageIO.getImageReaders(input);if(!readers.hasNext())throw new IllegalArgumentException("Selecciona una imagen PNG o JPG válida.");
   var reader=readers.next();
   try{reader.setInput(input,true,true);String tipo=reader.getFormatName().toLowerCase(java.util.Locale.ROOT);
    if(!tipo.equals("png")&&!tipo.equals("jpeg")&&!tipo.equals("jpg"))throw new IllegalArgumentException("Solo se permiten imágenes PNG o JPG.");
    int ancho=reader.getWidth(0),alto=reader.getHeight(0);
    if(ancho>5000||alto>5000||(long)ancho*alto>10000000)throw new IllegalArgumentException("La imagen es demasiado grande: máximo 5000 px por lado y 10 megapíxeles.");
    var imagen=reader.read(0);String extension=tipo.equals("png")?"png":"jpg";String nombre=UUID.randomUUID()+"."+extension;
    Files.createDirectories(directorio);Path destino=directorio.resolve(nombre);
    try(var output=Files.newOutputStream(destino,StandardOpenOption.CREATE_NEW)){if(!ImageIO.write(imagen,extension,output))throw new IOException("Formato no disponible");}
    return "/media/productos/"+nombre;
   }finally{reader.dispose();}
  }catch(IOException ex){throw new IllegalArgumentException("No se pudo guardar la imagen. Prueba con otro archivo PNG o JPG.");}
 }
}
