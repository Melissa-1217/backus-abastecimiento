package com.proyecto.backus.service;
import com.proyecto.backus.model.Usuario;
import com.proyecto.backus.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@Service
public class BodegasAdminService {
 private final UsuarioRepository usuarios;public BodegasAdminService(UsuarioRepository usuarios){this.usuarios=usuarios;}
 public record Bodega(Long id,String nombre,String responsable,String correo,String telefono,String ruc,String direccion,String distrito,String provincia,String departamento,String referencia){}
 private static String dato(String valor){return valor==null||valor.isBlank()?"Sin registrar":valor;}
 public static Bodega convertir(Usuario u){return new Bodega(u.getId(),dato(u.getBodega()),dato(u.getNombre()),dato(u.getCorreo()),dato(u.getTelefono()),dato(u.getRuc()),dato(u.getDireccion()),dato(u.getDistrito()),dato(u.getProvincia()),dato(u.getDepartamento()),dato(u.getReferencia()));}
 @Transactional(readOnly=true) public Page<Bodega> listar(String busqueda,String distrito,int pagina){
  Specification<Usuario> filtro=(root,query,cb)->{var condiciones=new ArrayList<jakarta.persistence.criteria.Predicate>();condiciones.add(cb.equal(root.get("rol"),"BODEGUERO"));String texto=busqueda.trim().toLowerCase(Locale.ROOT);
   if(!texto.isBlank()){String like="%"+texto.replace("!","!!").replace("%","!%").replace("_","!_")+"%";condiciones.add(cb.or(cb.like(cb.lower(root.get("bodega")),like,'!'),cb.like(cb.lower(root.get("nombre")),like,'!'),cb.like(cb.lower(root.get("correo")),like,'!'),cb.like(root.get("ruc"),like,'!')));}
   if("__SIN_REGISTRAR__".equals(distrito))condiciones.add(cb.or(cb.isNull(root.get("distrito")),cb.equal(cb.trim(root.get("distrito")),"")));else if(!distrito.isBlank())condiciones.add(cb.equal(cb.lower(root.get("distrito")),distrito.toLowerCase(Locale.ROOT)));return cb.and(condiciones.toArray(jakarta.persistence.criteria.Predicate[]::new));};
  return usuarios.findAll(filtro,PageRequest.of(Math.max(0,pagina),10,Sort.by("bodega").ascending().and(Sort.by("id")))).map(BodegasAdminService::convertir);
 }
 @Transactional(readOnly=true) public List<String> distritos(){return usuarios.distritosDeBodegueros();}
 @Transactional(readOnly=true) public Bodega detalle(Long id){var u=usuarios.findById(id).filter(v->"BODEGUERO".equals(v.getRol())).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));return convertir(u);}
}
