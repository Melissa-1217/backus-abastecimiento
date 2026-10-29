package com.proyecto.backus.service;
import com.proyecto.backus.model.Inventario;
import com.proyecto.backus.model.Pedido;
import com.proyecto.backus.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ResumenAdminService {
 private final InventarioRepository inventarios;private final ProductoRepository productos;private final PedidoRepository pedidos;
 public ResumenAdminService(InventarioRepository i,ProductoRepository p,PedidoRepository o){inventarios=i;productos=p;pedidos=o;}
 public record Resumen(long productosActivos,long presentaciones,long agotadas,long stockBajo,long pedidos,List<Inventario> alertas,List<Pedido> ultimos){}
 @Transactional(readOnly=true)
 public Resumen obtener(){
  var catalogo=inventarios.buscarCatalogo("Arequipa","",false);
  var alertas=catalogo.stream().filter(i->i.getCantidad()<=5).sorted(java.util.Comparator.comparing(Inventario::getCantidad)).toList();
  return new Resumen(productos.countByActivoTrue(),catalogo.size(),catalogo.stream().filter(i->i.getCantidad()==0).count(),catalogo.stream().filter(i->i.getCantidad()>0&&i.getCantidad()<=5).count(),pedidos.count(),alertas,pedidos.findTop5ByOrderByFechaDescIdDesc());
 }
}
