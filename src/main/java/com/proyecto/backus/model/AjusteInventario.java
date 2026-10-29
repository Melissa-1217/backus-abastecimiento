package com.proyecto.backus.model;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
@Entity @Table(name="ajuste_inventario")
public class AjusteInventario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="inventario_id",nullable=false) private Long inventarioId;
 @Column(name="cantidad_anterior",nullable=false) private Integer cantidadAnterior;
 @Column(name="cantidad_nueva",nullable=false) private Integer cantidadNueva;
 @Column(nullable=false,length=300) private String motivo;
 @Column(nullable=false,length=254) private String administrador;
 @Column(nullable=false) private OffsetDateTime fecha=OffsetDateTime.now();
 public void setInventarioId(Long v){inventarioId=v;}public Long getInventarioId(){return inventarioId;}
 public void setCantidadAnterior(Integer v){cantidadAnterior=v;}public Integer getCantidadAnterior(){return cantidadAnterior;}
 public void setCantidadNueva(Integer v){cantidadNueva=v;}public Integer getCantidadNueva(){return cantidadNueva;}
 public void setMotivo(String v){motivo=v;}public String getMotivo(){return motivo;}
 public void setAdministrador(String v){administrador=v;}public String getAdministrador(){return administrador;}
 public OffsetDateTime getFecha(){return fecha;}
}
