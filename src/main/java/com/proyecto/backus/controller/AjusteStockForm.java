package com.proyecto.backus.controller;
import jakarta.validation.constraints.*;
public class AjusteStockForm {
 @NotNull @Min(1) private Long inventarioId;
 @NotNull @Min(0) private Integer cantidadAnterior;
 @NotNull @Min(0) private Integer cantidadNueva;
 @NotBlank @Size(max=300) private String motivo;
 public Long getInventarioId(){return inventarioId;}public void setInventarioId(Long v){inventarioId=v;}
 public Integer getCantidadAnterior(){return cantidadAnterior;}public void setCantidadAnterior(Integer v){cantidadAnterior=v;}
 public Integer getCantidadNueva(){return cantidadNueva;}public void setCantidadNueva(Integer v){cantidadNueva=v;}
 public String getMotivo(){return motivo;}public void setMotivo(String v){motivo=v==null?"":v.trim();}
}
