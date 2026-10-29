package com.proyecto.backus.model;
import jakarta.persistence.*;
@Entity @Table(name="usuario_bodega")
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=100) private String nombre;
 @Column(nullable=false,length=120) private String bodega;
 @Column(nullable=false,unique=true,length=254) private String correo;
 @Column(name="password_hash",nullable=false,length=100) private String passwordHash;
 @Column(length=20) private String telefono;
 public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
 @Column(length=11) private String ruc;
 public String getRuc(){return ruc;} public void setRuc(String v){ruc=v;}
 @Column(length=200) private String direccion;
 public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v;}
 @Column(length=100) private String distrito;
 public String getDistrito(){return distrito;} public void setDistrito(String v){distrito=v;}
 @Column(length=100) private String provincia;
 public String getProvincia(){return provincia;} public void setProvincia(String v){provincia=v;}
 @Column(length=100) private String departamento;
 public String getDepartamento(){return departamento;} public void setDepartamento(String v){departamento=v;}
 @Column(length=200) private String referencia;
 public String getReferencia(){return referencia;} public void setReferencia(String v){referencia=v;}
 @Column(nullable=false,length=20) private String rol="BODEGUERO";
 public String getRol(){return rol;} public void setRol(String v){rol=v;}
 public Usuario() {}
 public Long getId(){return id;}
 public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
 public String getBodega(){return bodega;} public void setBodega(String v){bodega=v;}
 public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
 public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
}
