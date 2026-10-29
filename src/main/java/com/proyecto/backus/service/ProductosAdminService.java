package com.proyecto.backus.service;
import com.proyecto.backus.model.*;
import com.proyecto.backus.repository.*;
import com.proyecto.backus.controller.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@Service
public class ProductosAdminService {
 private final ProductoRepository productos;private final PresentacionRepository presentaciones;private final InventarioRepository inventarios;
 public ProductosAdminService(ProductoRepository p,PresentacionRepository pr,InventarioRepository i){productos=p;presentaciones=pr;inventarios=i;}
 public record Fila(Producto producto,List<Presentacion> presentaciones){}
 @Transactional(readOnly=true) public List<Fila> listar(){var formatos=presentaciones.listarConProducto();return productos.findAll(org.springframework.data.domain.Sort.by("nombre")).stream().map(p->new Fila(p,formatos.stream().filter(pr->pr.getProducto().getId().equals(p.getId())).toList())).toList();}
 @Transactional(readOnly=true) public Producto producto(Long id){return productos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 @Transactional(readOnly=true) public Presentacion presentacion(Long productoId,Long id){var pr=presentaciones.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));if(!pr.getProducto().getId().equals(productoId))throw new ResponseStatusException(HttpStatus.NOT_FOUND);return pr;}
 @Transactional public Producto guardarProducto(Long id,ProductoAdminForm f){var p=id==null?new Producto():producto(id);p.setNombre(f.getNombre());p.setImagenUrl(f.getImagenUrl());p.setMarca(f.getMarca());p.setCategoria(f.getCategoria());p.setActivo(f.isActivo());return productos.saveAndFlush(p);}
 @Transactional public Presentacion guardarPresentacion(Long productoId,Long id,PresentacionAdminForm f){var p=producto(productoId);boolean nueva=id==null;var pr=nueva?new Presentacion():presentacion(productoId,id);pr.setProducto(p);pr.setDescripcion(f.getDescripcion());pr.setContenidoMl(f.getContenidoMl());pr.setUnidadesPorPaquete(f.getUnidadesPorPaquete());pr.setPrecio(f.getPrecio());pr.setActivo(f.isActivo());pr.setImagenUrl(f.getImagenUrl());presentaciones.saveAndFlush(pr);
 if(nueva){var i=new Inventario();i.setPresentacion(pr);i.setZona("Arequipa");i.setCantidad(0);inventarios.save(i);}
 return pr;
 }
}
