package com.proyecto.backus.repository;

import com.proyecto.backus.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetallePedidoRepository
        extends JpaRepository<DetallePedido, Long> {

    @Query("""
        SELECT d
        FROM DetallePedido d
        JOIN FETCH d.inventario i
        JOIN FETCH i.presentacion pr
        JOIN FETCH pr.producto p
        WHERE d.pedido.id = :pedidoId
        ORDER BY d.id
        """)
    List<DetallePedido> buscarDetallesDelPedido(
        @Param("pedidoId") Long pedidoId
    );
}