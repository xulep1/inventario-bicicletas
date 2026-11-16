package com.patc.inventarioqr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patc.inventarioqr.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    boolean existsByProductoId(Long productoId);
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(Long productoId);
}
