package com.proyecto.backus.controller;
import jakarta.validation.constraints.*;
public class PresentacionAdminForm{
 @NotBlank @Size(max=120) private String descripcion="";
 @NotNull @Min(1) private Integer contenidoMl;
 @NotNull @Min(1) private Integer unidadesPorPaquete;
 @NotNull @DecimalMin("0.00") @Digits(integer=8,fraction=2) private java.math.BigDecimal precio;
 @Size(max=1000) @Pattern(regexp="(?:https://[^\\s]+|/media/productos/[0-9a-f-]{36}\\.(?:png|jpg))?",message="Usa un enlace HTTPS, una imagen subida o deja el campo vacío.") private String imagenUrl="";
  private boolean activo=true;
 public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v==null?"":v.trim();}
 public Integer getContenidoMl(){return contenidoMl;} public void setContenidoMl(Integer v){contenidoMl=v;}
 public Integer getUnidadesPorPaquete(){return unidadesPorPaquete;} public void setUnidadesPorPaquete(Integer v){unidadesPorPaquete=v;}
 public java.math.BigDecimal getPrecio(){return precio;} public void setPrecio(java.math.BigDecimal v){precio=v;}
 public String getImagenUrl(){return imagenUrl;} public void setImagenUrl(String v){imagenUrl=v==null?"":v.trim();}
 public boolean isActivo(){return activo;} public void setActivo(boolean v){activo=v;}
}
