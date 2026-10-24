package com.proyecto.backus.repository;

import com.proyecto.backus.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventarioRepository
        extends JpaRepository<Inventario, Long> {

    @Query("""
        SELECT i
        FROM Inventario i
        JOIN FETCH i.presentacion pr
        JOIN FETCH pr.producto p
        WHERE LOWER(i.zona) = LOWER(:zona)
          AND pr.activo = true
          AND p.activo = true
        ORDER BY p.nombre, pr.descripcion
        """)
    List<Inventario> buscarCatalogoPorZona(
        @Param("zona") String zona
    );
}