package com.patc.inventarioqr.service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ConexionService {
    private final String configurada;

    public ConexionService(@Value("${app.base-url:}") String configurada) {
        this.configurada = configurada.strip().replaceAll("/+$", "");
        if (!this.configurada.isEmpty()) {
            URI uri = URI.create(this.configurada);
            if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
                    || uri.getHost() == null || uri.getRawQuery() != null
                    || uri.getRawFragment() != null || uri.getUserInfo() != null) {
                throw new IllegalArgumentException("APP_BASE_URL debe ser una URL http o https sin parametros");
            }
        }
    }

    public String baseUrl(HttpServletRequest request) {
        if (!configurada.isEmpty()) return configurada;
        String host = request.getServerName();
        if (esLocal(host)) host = direccionLan(host);
        int port = request.getServerPort();
        return UriComponentsBuilder.newInstance().scheme(request.getScheme()).host(host)
                .port((port == 80 && "http".equals(request.getScheme()))
                        || (port == 443 && "https".equals(request.getScheme())) ? -1 : port)
                .path(request.getContextPath()).build().toUriString();
    }

    public boolean esLocal(String host) {
        return "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host)
                || "::1".equals(host) || "[::1]".equals(host)
                || "0:0:0:0:0:0:0:1".equals(host);
    }

    private String direccionLan(String alternativa) {
        try {
            for (NetworkInterface red : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!red.isUp() || red.isLoopback() || red.isVirtual()) continue;
                for (InetAddress ip : Collections.list(red.getInetAddresses())) {
                    if (ip instanceof Inet4Address && ip.isSiteLocalAddress()) return ip.getHostAddress();
                }
            }
        } catch (SocketException ignored) {
            // La interfaz muestra una advertencia si solo queda disponible localhost.
        }
        return alternativa;
    }
}
