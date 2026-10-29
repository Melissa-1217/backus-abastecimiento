package com.proyecto.backus.repository;
import com.proyecto.backus.model.ImagenCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ImagenCatalogoRepository extends JpaRepository<ImagenCatalogo,Long>{
 List<ImagenCatalogo> findByProductoIdOrderByOrdenAscIdAsc(Long id);
 List<ImagenCatalogo> findByPresentacionIdOrderByOrdenAscIdAsc(Long id);
}
