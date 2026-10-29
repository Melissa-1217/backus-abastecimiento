package com.proyecto.backus.controller;
import jakarta.validation.constraints.*;
public class ProductoAdminForm{
 @NotBlank @Size(max=100) private String nombre="";
 @NotBlank @Size(max=80) private String marca="";
 @NotBlank @Size(max=80) private String categoria="";
 @Size(max=1000) @Pattern(regexp="(?:https://[^\\s]+|/media/productos/[0-9a-f-]{36}\\.(?:png|jpg))?",message="Usa un enlace HTTPS, una imagen subida o deja el campo vacío.") private String imagenUrl="";
 public String getImagenUrl(){return imagenUrl;} public void setImagenUrl(String v){imagenUrl=v==null?"":v.trim();}
 private boolean activo=true;
 public String getNombre(){return nombre;} public void setNombre(String v){nombre=v==null?"":v.trim();}
 public String getMarca(){return marca;} public void setMarca(String v){marca=v==null?"":v.trim();}
 public String getCategoria(){return categoria;} public void setCategoria(String v){categoria=v==null?"":v.trim();}
 public boolean isActivo(){return activo;} public void setActivo(boolean v){activo=v;}
}
