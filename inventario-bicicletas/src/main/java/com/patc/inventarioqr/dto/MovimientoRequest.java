package com.patc.inventarioqr.dto;

import com.patc.inventarioqr.model.TipoMovimiento;

public record MovimientoRequest(
        TipoMovimiento tipo,
        Integer cantidad,
        String observacion,
        Long usuarioId
) {
}
