package com.patc.inventarioqr.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.patc.inventarioqr.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.id = :id")
    Optional<Producto> buscarParaModificar(@Param("id") Long id);

    boolean existsBySkuIgnoreCase(String sku);
    Optional<Producto> findBySkuIgnoreCase(String sku);
    Optional<Producto> findByCodigoQr(String codigoQr);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByCategoriaIdAndNombreContainingIgnoreCase(Long categoriaId, String nombre);
    List<Producto> findByStockActualLessThanEqual(Integer stockMinimo);
}
