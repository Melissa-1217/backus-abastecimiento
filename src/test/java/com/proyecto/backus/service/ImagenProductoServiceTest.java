package com.proyecto.backus.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
class ImagenProductoServiceTest {
 @TempDir Path temp;
 @Test void guardaPngConNombrePropioYDecodificable()throws Exception{var out=new ByteArrayOutputStream();ImageIO.write(new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB),"png",out);var service=new ImagenProductoService(temp.toString());var url=service.guardar(new MockMultipartFile("imagenArchivo","../../foto.png","image/png",out.toByteArray()));assertTrue(url.matches("/media/productos/[0-9a-f-]{36}\\.png"));var guardado=temp.resolve(url.substring(url.lastIndexOf('/')+1));assertTrue(Files.exists(guardado));assertEquals(10,ImageIO.read(guardado.toFile()).getWidth());}
 @Test void rechazaArchivoQueFingeSerImagen(){var service=new ImagenProductoService(temp.toString());assertThrows(IllegalArgumentException.class,()->service.guardar(new MockMultipartFile("imagenArchivo","foto.jpg","image/jpeg","<script>hola</script>".getBytes())));}
 @Test void rechazaMasDeCincoMegas(){var service=new ImagenProductoService(temp.toString());assertThrows(IllegalArgumentException.class,()->service.guardar(new MockMultipartFile("imagenArchivo","foto.png","image/png",new byte[5*1024*1024+1])));}
}
