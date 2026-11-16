package com.patc.inventarioqr.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.patc.inventarioqr.model.Categoria;
import com.patc.inventarioqr.model.Producto;
import com.patc.inventarioqr.model.Usuario;
import com.patc.inventarioqr.repository.CategoriaRepository;
import com.patc.inventarioqr.repository.ProductoRepository;
import com.patc.inventarioqr.repository.UsuarioRepository;
import com.patc.inventarioqr.util.QrPayloadUtil;

@Configuration
public class DataSeeder {

    @SuppressWarnings("unused")
    @Bean
    CommandLineRunner cargarDatosIniciales(
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository
    ) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("Operador de demostracion", "demo@bicicletas.local", "ADMIN"));
            }

            if (categoriaRepository.count() == 0) {
                categoriaRepository.save(new Categoria("Bicicletas", "Bicicletas de montana, urbanas e infantiles por referencia y talla"));
                categoriaRepository.save(new Categoria("Repuestos", "Llantas, neumaticos, cadenas, frenos y transmision"));
                categoriaRepository.save(new Categoria("Accesorios", "Cascos, luces, guantes, candados y complementos"));
            }

            if (productoRepository.count() == 0) {
                Categoria bicicletas = categoriaRepository.findByNombreIgnoreCase("Bicicletas").orElse(null);
                Categoria repuestos = categoriaRepository.findByNombreIgnoreCase("Repuestos").orElse(null);
                Categoria accesorios = categoriaRepository.findByNombreIgnoreCase("Accesorios").orElse(null);
                crearProductoDemo(productoRepository, bicicletas, "BIC-MTB-29-M", "Bicicleta de montana rin 29", "Cuadro de aluminio. Ejemplo academico; no corresponde al inventario real.", "unidad", new BigDecimal("950000"), 4, 2, "A", "Marca demo", "MTB 21V", "M", "29");
                crearProductoDemo(productoRepository, bicicletas, "BIC-INF-20", "Bicicleta infantil rin 20", "Bicicleta infantil. Datos de demostracion.", "unidad", new BigDecimal("420000"), 2, 1, "B", "Marca demo", "Infantil", "Infantil", "20");
                crearProductoDemo(productoRepository, repuestos, "REP-LLA-29-210", "Llanta 29 x 2.10", "Verificar ancho y compatibilidad antes de entregar.", "unidad", new BigDecimal("68000"), 12, 4, "B", "Marca demo", "MTB 2.10", null, "29");
                crearProductoDemo(productoRepository, repuestos, "REP-CAD-8V", "Cadena de 8 velocidades", "Cadena de repuesto para transmision de 8 velocidades.", "unidad", new BigDecimal("45000"), 3, 4, "B", "Marca demo", "8 velocidades", null, null);
                crearProductoDemo(productoRepository, accesorios, "ACC-CAS-M", "Casco de ciclismo talla M", "Casco de demostracion; ajustar la talla al registrar cada referencia.", "unidad", new BigDecimal("85000"), 6, 2, "C", "Marca demo", "Urbano", "M", null);
                crearProductoDemo(productoRepository, accesorios, "ACC-LUZ-USB", "Luz recargable USB", "Luz delantera para bicicleta.", "unidad", new BigDecimal("35000"), 8, 3, "C", "Marca demo", "USB delantera", null, null);
            }
        };
    }

    private void crearProductoDemo(
            ProductoRepository productoRepository,
            Categoria categoria,
            String sku,
            String nombre,
            String descripcion,
            String unidadMedida,
            BigDecimal precio,
            int stockActual,
            int stockMinimo,
            String clasificacionAbc,
            String marca,
            String modelo,
            String talla,
            String rin
    ) {
        Producto producto = new Producto();
        producto.setSku(sku);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setMarca(marca);
        producto.setModelo(modelo);
        producto.setTalla(talla);
        producto.setRin(rin);
        producto.setUnidadMedida(unidadMedida);
        producto.setPrecioUnitario(precio);
        producto.setStockActual(stockActual);
        producto.setStockMinimo(stockMinimo);
        producto.setClasificacionAbc(clasificacionAbc);
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto);
        guardado.setCodigoQr(QrPayloadUtil.paraProducto(guardado.getId(), guardado.getSku()));
        productoRepository.save(guardado);
    }
}
