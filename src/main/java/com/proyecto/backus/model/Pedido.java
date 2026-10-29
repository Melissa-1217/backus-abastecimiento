package com.proyecto.backus.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime fecha = OffsetDateTime.now();

    @Column(nullable = false, length = 100)
    private String zona;

    @Column(nullable = false, length = 30)
    private String estado = "CONFIRMADO_DEMO";

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

 @Column(name="usuario_id") private Long usuarioId;
 public Long getUsuarioId(){return usuarioId;} public void setUsuarioId(Long v){usuarioId=v;}
 @Column(name="bodega_nombre",length=120) private String bodegaNombre;
 public String getBodegaNombre(){return bodegaNombre;} public void setBodegaNombre(String v){bodegaNombre=v;}
 @Column(name="responsable",length=100) private String responsable;
 public String getResponsable(){return responsable;} public void setResponsable(String v){responsable=v;}
 @Column(name="correo_contacto",length=254) private String correoContacto;
 public String getCorreoContacto(){return correoContacto;} public void setCorreoContacto(String v){correoContacto=v;}
 @Column(name="telefono_contacto",length=20) private String telefonoContacto;
 public String getTelefonoContacto(){return telefonoContacto;} public void setTelefonoContacto(String v){telefonoContacto=v;}
 @Column(name="direccion_entrega",length=600) private String direccionEntrega;
 public String getDireccionEntrega(){return direccionEntrega;} public void setDireccionEntrega(String v){direccionEntrega=v;}
    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getFecha() {
        return fecha;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}