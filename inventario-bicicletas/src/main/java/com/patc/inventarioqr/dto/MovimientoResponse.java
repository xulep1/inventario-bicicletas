package com.patc.inventarioqr.dto;

import java.time.LocalDateTime;

import com.patc.inventarioqr.model.TipoMovimiento;

public record MovimientoResponse(
        Long id,
        TipoMovimiento tipo,
        Integer cantidad,
        String observacion,
        LocalDateTime fecha,
        Long productoId,
        String productoSku,
        String productoNombre,
        Long usuarioId,
        String usuarioNombre
) {
}
