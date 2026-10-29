package com.proyecto.backus.service;
import com.proyecto.backus.controller.PresentacionAdminForm;
import com.proyecto.backus.model.*;
import com.proyecto.backus.repository.*;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
class ProductosAdminTest {
 Inventario inventarioGuardado; Presentacion formatoGuardado;
 @SuppressWarnings("unchecked") <T>T repo(Class<T> t,java.util.function.BiFunction<String,Object[],Object> fn){return (T)Proxy.newProxyInstance(t.getClassLoader(),new Class<?>[]{t},(p,m,a)->fn.apply(m.getName(),a));}
 Producto producto()throws Exception{var p=new Producto();var id=Producto.class.getDeclaredField("id");id.setAccessible(true);id.set(p,1L);return p;}
 PresentacionAdminForm form(){var f=new PresentacionAdminForm();f.setDescripcion("Lata 355 ml - paquete de 6");f.setContenidoMl(355);f.setUnidadesPorPaquete(6);f.setPrecio(new BigDecimal("24.00"));f.setImagenUrl("https://example.com/foto.png");return f;}
 @Test void nuevaPresentacionCreaInventarioCero()throws Exception{var p=producto();var ps=repo(ProductoRepository.class,(m,a)->Optional.of(p));var prs=repo(PresentacionRepository.class,(m,a)->{formatoGuardado=(Presentacion)a[0];return formatoGuardado;});var inv=repo(InventarioRepository.class,(m,a)->{inventarioGuardado=(Inventario)a[0];return inventarioGuardado;});new ProductosAdminService(ps,prs,inv).guardarPresentacion(1L,null,form());assertEquals(0,inventarioGuardado.getCantidad());assertEquals("Arequipa",inventarioGuardado.getZona());assertSame(formatoGuardado,inventarioGuardado.getPresentacion());assertEquals(new BigDecimal("24.00"),formatoGuardado.getPrecio());}
 @Test void editarNoTocaExistencias()throws Exception{var p=producto();var pr=new Presentacion();pr.setProducto(p);var ps=repo(ProductoRepository.class,(m,a)->Optional.of(p));var prs=repo(PresentacionRepository.class,(m,a)->m.equals("findById")?Optional.of(pr):a[0]);var inv=repo(InventarioRepository.class,(m,a)->{fail("Editar un formato no debe modificar inventario");return null;});new ProductosAdminService(ps,prs,inv).guardarPresentacion(1L,2L,form());assertEquals(355,pr.getContenidoMl());assertEquals("https://example.com/foto.png",pr.getImagenUrl());}
 @Test void validaPrecioContenidoEImagen(){try(var factory=Validation.buildDefaultValidatorFactory()){var validator=factory.getValidator();var f=form();assertTrue(validator.validate(f).isEmpty());f.setPrecio(new BigDecimal("-1"));f.setContenidoMl(0);f.setImagenUrl("javascript:alert(1)");assertEquals(3,validator.validate(f).size());}}
}
