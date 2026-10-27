package com.proyecto.backus.controller;
import jakarta.validation.constraints.*;
public class PerfilForm {
 @NotBlank @Size(max=100) private String nombre;
 @NotBlank @Size(max=120) private String bodega;
 @Pattern(regexp="[0-9+() -]{0,20}",message="Revisa el teléfono de contacto.") private String telefono;
 @Pattern(regexp="(?:[0-9]{11})?",message="El RUC debe tener 11 dígitos o quedar vacío.") private String ruc;
 @Size(max=200) private String direccion;
 @Size(max=100) private String distrito;
 @Size(max=100) private String provincia;
 @Size(max=100) private String departamento;
 @Size(max=200) private String referencia;
 public String getNombre(){return nombre;} public void setNombre(String v){nombre=v==null?"":v.trim();}
 public String getBodega(){return bodega;} public void setBodega(String v){bodega=v==null?"":v.trim();}
 public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v==null?"":v.trim();}
 public String getRuc(){return ruc;} public void setRuc(String v){ruc=v==null?"":v.trim();}
 public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v==null?"":v.trim();}
 public String getDistrito(){return distrito;} public void setDistrito(String v){distrito=v==null?"":v.trim();}
 public String getProvincia(){return provincia;} public void setProvincia(String v){provincia=v==null?"":v.trim();}
 public String getDepartamento(){return departamento;} public void setDepartamento(String v){departamento=v==null?"":v.trim();}
 public String getReferencia(){return referencia;} public void setReferencia(String v){referencia=v==null?"":v.trim();}
}
