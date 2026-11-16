package com.patc.inventarioqr.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patc.inventarioqr.dto.InventarioResumenResponse;
import com.patc.inventarioqr.model.Producto;
import com.patc.inventarioqr.repository.ProductoRepository;

@Service
public class ReporteService {

    private final ProductoRepository productoRepository;

    public ReporteService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public InventarioResumenResponse resumenInventario() {
        List<Producto> productos = productoRepository.findAll();
        long stockBajo = productos.stream().filter(Producto::estaEnStockBajo).count();
        int unidades = productos.stream()
                .map(Producto::getStockActual)
                .filter(valor -> valor != null)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal valorInventario = productos.stream()
                .map(producto -> producto.getPrecioUnitario().multiply(BigDecimal.valueOf(producto.getStockActual() == null ? 0 : producto.getStockActual())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventarioResumenResponse(productos.size(), stockBajo, unidades, valorInventario);
    }
}
