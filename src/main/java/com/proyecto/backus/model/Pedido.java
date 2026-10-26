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