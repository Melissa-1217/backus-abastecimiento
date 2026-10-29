package com.proyecto.backus.repository;
import com.proyecto.backus.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductoRepository extends JpaRepository<Producto,Long>{long countByActivoTrue();}
