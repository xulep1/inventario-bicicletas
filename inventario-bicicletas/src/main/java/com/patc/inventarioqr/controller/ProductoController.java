package com.patc.inventarioqr.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.patc.inventarioqr.dto.MensajeResponse;
import com.patc.inventarioqr.dto.MovimientoRequest;
import com.patc.inventarioqr.dto.MovimientoResponse;
import com.patc.inventarioqr.dto.ProductoRequest;
import com.patc.inventarioqr.dto.ProductoResponse;
import com.patc.inventarioqr.service.ProductoService;
import com.patc.inventarioqr.service.QrService;
import com.patc.inventarioqr.service.ConexionService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final QrService qrService;

    private final ConexionService conexionService;

    public ProductoController(ProductoService productoService, QrService qrService, ConexionService conexionService) {
        this.productoService = productoService;
        this.qrService = qrService;
        this.conexionService = conexionService;
    }

    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(name = "nombre", required = false) String nombre) {
        return productoService.listar(nombre);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<ProductoResponse> listarPorCategoria(
            @PathVariable("categoriaId") Long categoriaId,
            @RequestParam(name = "nombre", required = false) String nombre
    ) {
        return productoService.listarPorCategoria(categoriaId, nombre);
    }

    @GetMapping("/{id}")
    public ProductoResponse buscarPorId(@PathVariable("id") Long id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody ProductoRequest request) {
        return ResponseEntity.status(201).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable("id") Long id, @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public MensajeResponse eliminar(@PathVariable("id") Long id) {
        productoService.eliminar(id);
        return new MensajeResponse("Producto eliminado correctamente");
    }
    
    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generarQr(@PathVariable("id") Long id, HttpServletRequest request) {
    ProductoResponse producto = productoService.buscarPorId(id);

    String contenidoQr = conexionService.baseUrl(request) + "/producto.html?id=" + producto.id();

    byte[] imagen = qrService.generarQrPng(contenidoQr, 400, 400);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
            .header(HttpHeaders.CACHE_CONTROL, "no-store")
            .body(imagen);
}

    @GetMapping("/buscar-qr")
    public ProductoResponse buscarQr(@RequestParam("codigo") String codigo) {
        return productoService.buscarPorCodigoQr(codigo);
    }

    @PostMapping(value = "/leer-qr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductoResponse leerQr(@RequestParam("archivo") MultipartFile archivo) {
        String contenidoQr = qrService.leerQrDesdeImagen(archivo);
        return productoService.buscarPorCodigoQr(contenidoQr);
    }

    @PostMapping("/{id}/movimientos")
    public ResponseEntity<MovimientoResponse> registrarMovimiento(
            @PathVariable("id") Long id,
            @RequestBody MovimientoRequest request
    ) {
        return ResponseEntity.status(201).body(productoService.registrarMovimiento(id, request));
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoResponse> movimientosPorProducto(@PathVariable("id") Long id) {
        return productoService.listarMovimientos(id);
    }
}
