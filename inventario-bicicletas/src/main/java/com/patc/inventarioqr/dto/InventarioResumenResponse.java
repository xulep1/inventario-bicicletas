package com.patc.inventarioqr.dto;

import java.math.BigDecimal;

public record InventarioResumenResponse(
        long totalProductos,
        long productosEnStockBajo,
        int totalUnidadesDisponibles,
        BigDecimal valorEstimadoInventario
) {
}
