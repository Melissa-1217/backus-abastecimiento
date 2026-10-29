package com.proyecto.backus.service;
import com.proyecto.backus.repository.*;
import com.proyecto.backus.model.*;
import com.proyecto.backus.controller.AjusteStockForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class InventarioAdminService {
 private final InventarioRepository inventarios;private final AjusteInventarioRepository ajustes;
 public InventarioAdminService(InventarioRepository i,AjusteInventarioRepository a){inventarios=i;ajustes=a;}
 @Transactional(readOnly=true) public List<Inventario> listar(){return inventarios.listarAdministracion();}
 @Transactional public void ajustar(AjusteStockForm f,String administrador){
  if(f.getCantidadNueva()==null||f.getCantidadNueva()<0||f.getMotivo()==null||f.getMotivo().isBlank()||f.getMotivo().length()>300)throw new IllegalArgumentException("Escribe una cantidad válida y un motivo de hasta 300 caracteres.");
  var i=inventarios.bloquearParaAjuste(f.getInventarioId()).orElseThrow(()->new IllegalArgumentException("El registro de inventario ya no está disponible."));
  if(!Objects.equals(i.getCantidad(),f.getCantidadAnterior()))throw new IllegalArgumentException("El stock cambió mientras editabas. Revisa la cantidad actual y vuelve a realizar el ajuste.");
  if(Objects.equals(i.getCantidad(),f.getCantidadNueva()))throw new IllegalArgumentException("La nueva cantidad es igual a las existencias actuales.");
  var a=new AjusteInventario();a.setInventarioId(i.getId());a.setCantidadAnterior(i.getCantidad());a.setCantidadNueva(f.getCantidadNueva());a.setMotivo(f.getMotivo().trim());a.setAdministrador(administrador);i.setCantidad(f.getCantidadNueva());ajustes.save(a);
 }
}
