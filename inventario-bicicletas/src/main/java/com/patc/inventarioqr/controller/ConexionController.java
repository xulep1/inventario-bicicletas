package com.patc.inventarioqr.controller;

import java.net.URI;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.patc.inventarioqr.service.ConexionService;

@RestController
public class ConexionController {
    private final ConexionService conexion;

    public ConexionController(ConexionService conexion) { this.conexion = conexion; }

    @GetMapping("/api/conexion")
    public Map<String, Object> datos(HttpServletRequest request) {
        String url = conexion.baseUrl(request);
        boolean soloLocal = conexion.esLocal(URI.create(url).getHost());
        return Map.of("url", url, "soloLocal", soloLocal);
    }
}
