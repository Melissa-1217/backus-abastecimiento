package com.proyecto.backus.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "inventario",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"presentacion_id", "zona"}
    )
)
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "presentacion_id", nullable = false)
    private Presentacion presentacion;

    @Column(nullable = false, length = 100)
    private String zona;

    @Column(nullable = false)
    private Integer cantidad = 0;

    public Inventario() {
    }

    public Long getId() {
        return id;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}