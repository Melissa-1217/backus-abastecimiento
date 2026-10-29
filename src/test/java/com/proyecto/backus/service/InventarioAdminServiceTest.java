package com.proyecto.backus.service;
import com.proyecto.backus.controller.AjusteStockForm;
import com.proyecto.backus.model.*;
import com.proyecto.backus.repository.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Proxy;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
class InventarioAdminServiceTest {
 AjusteInventario guardado;
 Inventario item(int stock){var i=new Inventario();i.setCantidad(stock);return i;}
 @SuppressWarnings("unchecked") <T>T repo(Class<T> type,java.util.function.BiFunction<String,Object[],Object> fn){return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(p,m,a)->fn.apply(m.getName(),a));}
 InventarioAdminService service(Inventario i){return new InventarioAdminService(repo(InventarioRepository.class,(m,a)->Optional.of(i)),repo(AjusteInventarioRepository.class,(m,a)->{guardado=(AjusteInventario)a[0];return guardado;}));}
 AjusteStockForm form(int anterior,int nueva){var f=new AjusteStockForm();f.setInventarioId(1L);f.setCantidadAnterior(anterior);f.setCantidadNueva(nueva);f.setMotivo("Ingreso por conteo");return f;}
 @Test void actualizaYRegistraResponsableYCantidades(){var i=item(3);service(i).ajustar(form(3,20),"admin@demo.pe");assertEquals(20,i.getCantidad());assertEquals(3,guardado.getCantidadAnterior());assertEquals(20,guardado.getCantidadNueva());assertEquals("admin@demo.pe",guardado.getAdministrador());assertEquals("Ingreso por conteo",guardado.getMotivo());}
 @Test void rechazaCambioConcurrenteSinSobrescribir(){var i=item(2);assertThrows(IllegalArgumentException.class,()->service(i).ajustar(form(3,20),"admin@demo.pe"));assertEquals(2,i.getCantidad());assertNull(guardado);}
 @Test void admiteAgotarStock(){var i=item(5);service(i).ajustar(form(5,0),"admin@demo.pe");assertEquals(0,i.getCantidad());}
 @Test void rechazaCantidadesNegativas(){var i=item(3);assertThrows(IllegalArgumentException.class,()->service(i).ajustar(form(3,-1),"admin@demo.pe"));assertEquals(3,i.getCantidad());assertNull(guardado);}
 @Test void requiereMotivoYUnCambio(){var i=item(3);var f=form(3,20);f.setMotivo(" ");assertThrows(IllegalArgumentException.class,()->service(i).ajustar(f,"admin@demo.pe"));assertThrows(IllegalArgumentException.class,()->service(i).ajustar(form(3,3),"admin@demo.pe"));assertNull(guardado);}
}
