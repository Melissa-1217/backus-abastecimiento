package com.proyecto.backus.service;
import com.proyecto.backus.model.ImagenCatalogo;
import com.proyecto.backus.repository.ImagenCatalogoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class GaleriaService {
 private final ImagenCatalogoRepository fotos;
 public GaleriaService(ImagenCatalogoRepository fotos){this.fotos=fotos;}
 public record Foto(Long id,String url){}
 private List<ImagenCatalogo> registros(boolean presentacion,Long id){return presentacion?fotos.findByPresentacionIdOrderByOrdenAscIdAsc(id):fotos.findByProductoIdOrderByOrdenAscIdAsc(id);}
 @Transactional(readOnly=true) public List<Foto> listar(boolean pr,Long id,String principal){var lista=new ArrayList<Foto>();var guardadas=id==null?List.<ImagenCatalogo>of():registros(pr,id);if(principal!=null&&!principal.isBlank()&&guardadas.stream().noneMatch(f->f.getUrl().equals(principal)))lista.add(new Foto(0L,principal));guardadas.forEach(f->lista.add(new Foto(f.getId(),f.getUrl())));return lista;}
 public List<String> enlaces(String texto){if(texto==null||texto.isBlank())return List.of();var lista=texto.lines().map(String::trim).filter(s->!s.isBlank()).distinct().toList();if(lista.stream().anyMatch(s->s.length()>1000||!s.matches("https://[^\\s]+")))throw new IllegalArgumentException("Usa un enlace HTTPS por línea, de hasta 1000 caracteres.");return lista;}
 @Transactional public String guardar(boolean pr,Long id,String principal,List<String> nuevas,List<Long> quitar,String elegida){
  var existentes=registros(pr,id);var ids=new HashSet<Long>();existentes.forEach(f->ids.add(f.getId()));ids.add(0L);
  if(!ids.containsAll(quitar))throw new IllegalArgumentException("Una foto seleccionada no pertenece a esta galería.");
  var conservar=existentes.stream().filter(f->!quitar.contains(f.getId())).toList();var urls=new LinkedHashSet<String>();
  if(principal!=null&&!principal.isBlank()&&!quitar.contains(0L)&&existentes.stream().noneMatch(f->f.getUrl().equals(principal)&&quitar.contains(f.getId())))urls.add(principal);
  conservar.forEach(f->urls.add(f.getUrl()));urls.addAll(nuevas);
  if(elegida!=null&&!elegida.isBlank()&&!urls.contains(elegida))throw new IllegalArgumentException("La imagen principal debe pertenecer a la galería y no estar marcada para quitar.");
  String portada=elegida!=null&&!elegida.isBlank()?elegida:urls.stream().findFirst().orElse("");
  fotos.deleteAll(existentes);int orden=0;
  for(String url:urls){var foto=new ImagenCatalogo();if(pr)foto.setPresentacionId(id);else foto.setProductoId(id);foto.setUrl(url);foto.setOrden(orden++);fotos.save(foto);}
  return portada;
 }
 @Transactional(readOnly=true) public List<String> publicas(Long pr,Long producto,String principalPr,String principalProducto){var lista=listar(true,pr,principalPr);if(lista.isEmpty())lista=listar(false,producto,principalProducto);var urls=new ArrayList<String>();String principal=!lista.isEmpty()&&principalPr!=null&&!principalPr.isBlank()?principalPr:principalProducto;if(principal!=null&&!principal.isBlank())urls.add(principal);for(var f:lista)if(!urls.contains(f.url()))urls.add(f.url());return urls;}
}
