package com.proyecto.backus.service;
import org.junit.jupiter.api.Test;
import com.proyecto.backus.model.Inventario;
import com.proyecto.backus.repository.*;
import java.lang.reflect.Proxy;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class ResumenAdminTest {
 @SuppressWarnings("unchecked") <T>T repo(Class<T> type,java.util.function.Function<String,Object> answer){return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(p,m,a)->answer.apply(m.getName()));}
 Inventario stock(int n){var i=new Inventario();i.setCantidad(n);return i;}
 @Test void clasificaAgotadosYUmbralSinContarStockRegular(){var inv=repo(InventarioRepository.class,m->List.of(stock(6),stock(5),stock(0),stock(1)));var productos=repo(ProductoRepository.class,m->2L);var pedidos=repo(PedidoRepository.class,m->m.equals("count")?3L:List.of());var resumen=new ResumenAdminService(inv,productos,pedidos).obtener();assertEquals(1,resumen.agotadas());assertEquals(2,resumen.stockBajo());assertEquals(4,resumen.presentaciones());assertEquals(3,resumen.alertas().size());assertEquals(0,resumen.alertas().getFirst().getCantidad());assertEquals(3,resumen.pedidos());}
 @Test void admiteCatalogoVacio(){var inv=repo(InventarioRepository.class,m->List.of());var productos=repo(ProductoRepository.class,m->0L);var pedidos=repo(PedidoRepository.class,m->m.equals("count")?0L:List.of());var resumen=new ResumenAdminService(inv,productos,pedidos).obtener();assertEquals(0,resumen.agotadas());assertTrue(resumen.alertas().isEmpty());}
}
