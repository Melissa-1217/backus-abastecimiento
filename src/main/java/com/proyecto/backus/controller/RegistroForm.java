package com.proyecto.backus.controller;
import jakarta.validation.constraints.*;
public class RegistroForm {
 @NotBlank(message="Escribe tu nombre.") @Size(max=100,message="Máximo 100 caracteres.") private String nombre="";
 @NotBlank(message="Escribe el nombre de tu bodega.") @Size(max=120,message="Máximo 120 caracteres.") private String bodega="";
 @NotBlank(message="Escribe tu correo.") @Email(message="Ingresa un correo válido.") @Size(max=254) private String correo="";
 @NotBlank(message="Escribe una contraseña.") @Size(min=8,max=64,message="Usa entre 8 y 64 caracteres.") private String password="";
 @NotBlank(message="Confirma tu contraseña.") private String confirmacion="";
 public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
 public String getBodega(){return bodega;} public void setBodega(String v){bodega=v;}
 public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public String getConfirmacion(){return confirmacion;} public void setConfirmacion(String v){confirmacion=v;}
}
