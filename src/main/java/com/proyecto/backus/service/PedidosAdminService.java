package com.proyecto.backus.service;
import com.proyecto.backus.model.*;
import com.proyecto.backus.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.*;
@Service
public class PedidosAdminService {
 private final PedidoRepository pedidos;private final DetallePedidoRepository detalles;
 public PedidosAdminService(PedidoRepository p,DetallePedidoRepository d){pedidos=p;detalles=d;}
 public static Long numero(String texto){try{return Long.valueOf(texto.trim().toUpperCase(Locale.ROOT).replaceFirst("^DEMO-",""));}catch(NumberFormatException ex){return null;}}
 @Transactional(readOnly=true) public Page<Pedido> listar(String busqueda,LocalDate desde,LocalDate hasta,int pagina){
  if(desde!=null&&hasta!=null&&hasta.isBefore(desde))throw new IllegalArgumentException("La fecha final debe ser igual o posterior a la inicial.");
  Specification<Pedido> filtro=(root,query,cb)->{var condiciones=new ArrayList<jakarta.persistence.criteria.Predicate>();String q=busqueda.trim().toLowerCase(Locale.ROOT);
   if(!q.isEmpty()){String like="%"+q.replace("!","!!").replace("%","!%").replace("_","!_")+"%";var texto=cb.or(cb.like(cb.lower(root.get("bodegaNombre")),like,'!'),cb.like(cb.lower(root.get("responsable")),like,'!'));Long id=numero(q);condiciones.add(id==null?texto:cb.or(texto,cb.equal(root.get("id"),id)));}
   var zona=ZoneId.of("America/Lima");if(desde!=null)condiciones.add(cb.greaterThanOrEqualTo(root.get("fecha"),desde.atStartOfDay(zona).toOffsetDateTime()));if(hasta!=null)condiciones.add(cb.lessThan(root.get("fecha"),hasta.plusDays(1).atStartOfDay(zona).toOffsetDateTime()));return cb.and(condiciones.toArray(jakarta.persistence.criteria.Predicate[]::new));};
  return pedidos.findAll(filtro,PageRequest.of(Math.max(0,pagina),10,Sort.by(Sort.Order.desc("fecha"),Sort.Order.desc("id"))));
 }
 public record Linea(String producto,String presentacion,int cantidad,BigDecimal precio,BigDecimal subtotal){}
 public record Detalle(Long id,String numero,String fecha,String estado,String bodega,String responsable,String correo,String telefono,String direccion,String zona,List<Linea> lineas,long bultos,BigDecimal total){}
 public static Detalle transformar(Pedido p,List<DetallePedido> detalles){var lineas=detalles.stream().map(d->new Linea(d.getInventario().getPresentacion().getProducto().getNombre(),d.getInventario().getPresentacion().getDescripcion(),d.getCantidad(),d.getPrecioUnitario(),d.getSubtotal())).toList();return new Detalle(p.getId(),String.format("DEMO-%04d",p.getId()),p.getFecha().atZoneSameInstant(ZoneId.of("America/Lima")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),"CONFIRMADO_DEMO".equals(p.getEstado())?"Registrado":p.getEstado(),dato(p.getBodegaNombre(),"Sin bodega asociada"),dato(p.getResponsable(),"Sin registrar"),dato(p.getCorreoContacto(),"Sin registrar"),dato(p.getTelefonoContacto(),"Sin registrar"),dato(p.getDireccionEntrega(),"Sin registrar"),p.getZona(),lineas,lineas.stream().mapToLong(Linea::cantidad).sum(),p.getTotal());}
 private static String dato(String v,String respaldo){return v==null||v.isBlank()?respaldo:v;}
 @Transactional(readOnly=true) public Detalle detalle(Long id){var p=pedidos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));return transformar(p,detalles.buscarDetallesDelPedido(id));}
}
