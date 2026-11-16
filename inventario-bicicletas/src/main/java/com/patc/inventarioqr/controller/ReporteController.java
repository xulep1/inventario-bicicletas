package com.patc.inventarioqr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patc.inventarioqr.dto.InventarioResumenResponse;
import com.patc.inventarioqr.service.ReporteService;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/inventario")
    public InventarioResumenResponse resumenInventario() {
        return reporteService.resumenInventario();
    }
}
