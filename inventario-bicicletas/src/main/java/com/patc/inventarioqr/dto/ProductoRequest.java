package com.patc.inventarioqr.dto;

import java.math.BigDecimal;

public record ProductoRequest(
        String sku,
        String nombre,
        String descripcion,
        String unidadMedida,
        BigDecimal precioUnitario,
        Integer stockActual,
        Integer stockMinimo,
        String clasificacionAbc,
        Long categoriaId,
        String marca,
        String modelo,
        String talla,
        String rin
) {
}
