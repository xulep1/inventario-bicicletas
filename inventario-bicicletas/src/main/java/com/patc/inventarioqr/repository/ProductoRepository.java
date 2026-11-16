package com.patc.inventarioqr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patc.inventarioqr.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsBySkuIgnoreCase(String sku);
    Optional<Producto> findBySkuIgnoreCase(String sku);
    Optional<Producto> findByCodigoQr(String codigoQr);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByCategoriaIdAndNombreContainingIgnoreCase(Long categoriaId, String nombre);
    List<Producto> findByStockActualLessThanEqual(Integer stockMinimo);
}
