package com.proyecto.backus.repository;

import com.proyecto.backus.model.Inventario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
                          AND (
                              :busqueda = ''
                              OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                              OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                              OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                              OR LOWER(pr.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))
                          )
                          AND (:soloDisponibles = false OR i.cantidad > 0)
                        ORDER BY p.nombre, pr.descripcion
                        """)
        List<Inventario> buscarCatalogo(
                        @Param("zona") String zona,
                        @Param("busqueda") String busqueda,
                        @Param("soloDisponibles") boolean soloDisponibles);

        @Query("""
                        SELECT i
                        FROM Inventario i
                        JOIN FETCH i.presentacion pr
                        JOIN FETCH pr.producto p
                        WHERE i.id = :id
                          AND LOWER(i.zona) = LOWER(:zona)
                          AND pr.activo = true
                          AND p.activo = true
                        """)
        java.util.Optional<Inventario> buscarDetalle(
                        @Param("id") Long id,
                        @Param("zona") String zona);

        @Query("""
                        SELECT i
                        FROM Inventario i
                        JOIN FETCH i.presentacion pr
                        JOIN FETCH pr.producto p
                        WHERE LOWER(i.zona) = LOWER(:zona)
                          AND p.id = :productoId
                          AND pr.id <> :presentacionId
                          AND i.cantidad > 0
                          AND pr.activo = true
                          AND p.activo = true
                        ORDER BY pr.descripcion
                        """)
        List<Inventario> buscarAlternativas(
                        @Param("zona") String zona,
                        @Param("productoId") Long productoId,
                        @Param("presentacionId") Long presentacionId);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT i
                        FROM Inventario i
                        WHERE i.id = :id
                          AND LOWER(i.zona) = LOWER(:zona)
                        """)
        Optional<Inventario> buscarParaConfirmar(
                        @Param("id") Long id,
                        @Param("zona") String zona);
}