package com.patc.inventarioqr.dto;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        boolean activa
) {
}
