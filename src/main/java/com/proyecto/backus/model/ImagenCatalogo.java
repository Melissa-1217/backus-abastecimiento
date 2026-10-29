package com.proyecto.backus.model;
import jakarta.persistence.*;
@Entity @Table(name="imagen_catalogo")
public class ImagenCatalogo {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="producto_id") private Long productoId;
 @Column(name="presentacion_id") private Long presentacionId;
 @Column(nullable=false,length=1000) private String url;
 @Column(nullable=false) private int orden;
 public Long getId(){return id;}public Long getProductoId(){return productoId;}public void setProductoId(Long v){productoId=v;}
 public Long getPresentacionId(){return presentacionId;}public void setPresentacionId(Long v){presentacionId=v;}
 public String getUrl(){return url;}public void setUrl(String v){url=v;}
 public int getOrden(){return orden;}public void setOrden(int v){orden=v;}
}
