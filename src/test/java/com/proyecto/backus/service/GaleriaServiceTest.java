package com.proyecto.backus.service;
import com.proyecto.backus.model.ImagenCatalogo;
import com.proyecto.backus.repository.ImagenCatalogoRepository;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Proxy;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class GaleriaServiceTest {
 List<ImagenCatalogo> datos=new ArrayList<>();long next=1;
 GaleriaService service(){var repo=(ImagenCatalogoRepository)Proxy.newProxyInstance(ImagenCatalogoRepository.class.getClassLoader(),new Class<?>[]{ImagenCatalogoRepository.class},(p,m,a)->{
  if(m.getName().startsWith("findBy")){boolean pr=m.getName().contains("Presentacion");return datos.stream().filter(f->Objects.equals(pr?f.getPresentacionId():f.getProductoId(),a[0])).toList();}
  if(m.getName().equals("deleteAll")){for(Object f:(Iterable<?>)a[0])datos.remove(f);return null;}
  if(m.getName().equals("save")){var f=(ImagenCatalogo)a[0];var id=ImagenCatalogo.class.getDeclaredField("id");id.setAccessible(true);id.set(f,next++);datos.add(f);return f;}
  throw new UnsupportedOperationException(m.getName());
 });return new GaleriaService(repo);}
 @Test void conservaPrincipalYAgregaFotos(){var s=service();assertEquals("https://a.test/1.png",s.guardar(false,1L,"https://a.test/1.png",List.of("https://a.test/2.png"),List.of(),""));assertEquals(2,s.listar(false,1L,"").size());String nueva=s.guardar(false,1L,"https://a.test/1.png",List.of(),List.of(),"https://a.test/2.png");assertEquals("https://a.test/2.png",nueva);assertEquals(2,datos.size());}
 @Test void quitarPrincipalEligeOtra(){var s=service();s.guardar(true,2L,"https://a.test/1.png",List.of("https://a.test/2.png"),List.of(),"");Long id=datos.getFirst().getId();assertEquals("https://a.test/2.png",s.guardar(true,2L,"https://a.test/1.png",List.of(),List.of(id),""));assertEquals(1,datos.size());}
 @Test void noPermiteQuitarFotosDeOtroProducto(){var s=service();assertThrows(IllegalArgumentException.class,()->s.guardar(false,1L,"",List.of(),List.of(999L),""));}
 @Test void admiteMasDeDiezFotosYValidaEnlaces(){var s=service();assertThrows(IllegalArgumentException.class,()->s.enlaces("javascript:alert(1)"));s.guardar(false,1L,"",java.util.stream.IntStream.range(0,25).mapToObj(i->"https://a.test/"+i).toList(),List.of(),"");assertEquals(25,datos.size());}
 @Test void presentacionUsaGaleriaDelProductoSoloSiNoTienePropia(){var s=service();s.guardar(false,1L,"https://a.test/producto.png",List.of(),List.of(),"");assertEquals(List.of("https://a.test/producto.png"),s.publicas(2L,1L,"","https://a.test/producto.png"));s.guardar(true,2L,"https://a.test/lata.png",List.of(),List.of(),"");assertEquals(List.of("https://a.test/lata.png"),s.publicas(2L,1L,"https://a.test/lata.png","https://a.test/producto.png"));}
}
