package com.proyecto.backus.repository;

import com.proyecto.backus.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Pedido> {
 List<Pedido> findTop5ByOrderByFechaDescIdDesc();
}