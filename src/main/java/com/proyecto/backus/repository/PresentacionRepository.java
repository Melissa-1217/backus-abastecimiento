package com.proyecto.backus.repository;
import com.proyecto.backus.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface PresentacionRepository extends JpaRepository<Presentacion,Long>{
 @Query("select pr from Presentacion pr join fetch pr.producto order by pr.descripcion") List<Presentacion> listarConProducto();
}
