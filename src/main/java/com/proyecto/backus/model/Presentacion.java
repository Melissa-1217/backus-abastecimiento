package com.proyecto.backus.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "presentacion")
public class Presentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false, length = 120)
    private String descripcion;

    @Column(name = "contenido_ml", nullable = false)
    private Integer contenidoMl;

    @Column(name = "unidades_por_paquete", nullable = false)
    private Integer unidadesPorPaquete;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private boolean activo = true;

    public Presentacion() {
    }

    public Long getId() {
        return id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getContenidoMl() {
        return contenidoMl;
    }

    public void setContenidoMl(Integer contenidoMl) {
        this.contenidoMl = contenidoMl;
    }

    public Integer getUnidadesPorPaquete() {
        return unidadesPorPaquete;
    }

    public void setUnidadesPorPaquete(Integer unidadesPorPaquete) {
        this.unidadesPorPaquete = unidadesPorPaquete;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}