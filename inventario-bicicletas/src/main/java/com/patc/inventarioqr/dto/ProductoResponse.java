package com.patc.inventarioqr.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponse(
        Long id,
        String sku,
        String nombre,
        String descripcion,
        String unidadMedida,
        BigDecimal precioUnitario,
        Integer stockActual,
        Integer stockMinimo,
        String codigoQr,
        String clasificacionAbc,
        Long categoriaId,
        String categoriaNombre,
        boolean stockBajo,
        LocalDateTime fechaCreacion,
        String marca,
        String modelo,
        String talla,
        String rin
) {
}
