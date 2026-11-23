package com.patc.inventarioqr.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.patc.inventarioqr.dto.MovimientoRequest;
import com.patc.inventarioqr.dto.MovimientoResponse;
import com.patc.inventarioqr.dto.ProductoRequest;
import com.patc.inventarioqr.dto.ProductoResponse;
import com.patc.inventarioqr.model.Categoria;
import com.patc.inventarioqr.model.MovimientoInventario;
import com.patc.inventarioqr.model.Producto;
import com.patc.inventarioqr.model.TipoMovimiento;
import com.patc.inventarioqr.model.Usuario;
import com.patc.inventarioqr.repository.CategoriaRepository;
import com.patc.inventarioqr.repository.MovimientoInventarioRepository;
import com.patc.inventarioqr.repository.ProductoRepository;
import com.patc.inventarioqr.repository.UsuarioRepository;
import com.patc.inventarioqr.util.QrPayloadUtil;

@Service
public class ProductoService {

    private static final Pattern ID_EN_URL_PRODUCTO = Pattern.compile("(?:id=|/api/productos/)(\\d+)");
    private static final Pattern ID_EN_PAYLOAD_PATC = Pattern.compile("PRODUCTO_ID=(\\d+)");

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            MovimientoInventarioRepository movimientoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimientoRepository = movimientoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre) {
        List<Producto> productos = nombre == null || nombre.isBlank()
                ? productoRepository.findAll()
                : productoRepository.findByNombreContainingIgnoreCase(nombre);

        return productos.stream()
                .map(this::mapearProducto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return mapearProducto(obtenerProducto(id));
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorCategoria(Long categoriaId, String nombre) {
        Long idCategoria = Objects.requireNonNull(categoriaId, "El id de la categoria es obligatorio");
        List<Producto> productos = nombre == null || nombre.isBlank()
                ? productoRepository.findByCategoriaId(idCategoria)
                : productoRepository.findByCategoriaIdAndNombreContainingIgnoreCase(idCategoria, nombre);

        return productos.stream()
                .map(this::mapearProducto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorCodigoQr(String codigoQr) {
        String contenidoQr = Objects.requireNonNull(codigoQr, "El codigo QR es obligatorio").strip();

        Long productoId = extraerIdProductoDesdeQr(contenidoQr);
        if (productoId != null) {
            return buscarPorId(productoId);
        }

        Producto producto = productoRepository.findByCodigoQr(contenidoQr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe producto asociado al codigo QR"));
        return mapearProducto(producto);
    }

    private Long extraerIdProductoDesdeQr(String contenidoQr) {
        Matcher matcherUrl = ID_EN_URL_PRODUCTO.matcher(contenidoQr);
        if (matcherUrl.find()) {
            return Long.valueOf(matcherUrl.group(1));
        }

        Matcher matcherPayload = ID_EN_PAYLOAD_PATC.matcher(contenidoQr);
        if (matcherPayload.find()) {
            return Long.valueOf(matcherPayload.group(1));
        }

        return null;
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        validarProducto(request);
        String sku = Objects.requireNonNull(normalizar(request.sku()), "El SKU es obligatorio");

        if (productoRepository.existsBySkuIgnoreCase(sku)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con el SKU " + sku);
        }

        Producto producto = new Producto();
        aplicarDatosProducto(producto, request, sku);

        Producto guardado = productoRepository.save(producto);
        guardado.setCodigoQr(QrPayloadUtil.paraProducto(guardado.getId(), guardado.getSku()));
        return mapearProducto(productoRepository.save(guardado));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        validarProducto(request);
        Producto producto = obtenerProductoParaModificar(id);
        String sku = Objects.requireNonNull(normalizar(request.sku()), "El SKU es obligatorio");

        if (!producto.getSku().equalsIgnoreCase(sku) && productoRepository.existsBySkuIgnoreCase(sku)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe otro producto con el SKU " + sku);
        }

        aplicarDatosProducto(producto, request, sku);
        producto.setCodigoQr(QrPayloadUtil.paraProducto(producto.getId(), producto.getSku()));
        return mapearProducto(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = obtenerProductoParaModificar(id);
        if (movimientoRepository.existsByProductoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar un producto que tiene movimientos de inventario");
        }
        productoRepository.delete(producto);
    }

    @Transactional
    public MovimientoResponse registrarMovimiento(Long productoId, MovimientoRequest request) {
        Producto producto = obtenerProductoParaModificar(productoId);
        validarMovimiento(request);

        TipoMovimiento tipo = Objects.requireNonNull(request.tipo(), "El tipo de movimiento es obligatorio");
        int cantidad = Objects.requireNonNull(request.cantidad(), "La cantidad es obligatoria").intValue();

        Usuario usuario = null;
        Long usuarioId = request.usuarioId();
        if (usuarioId != null) {
            usuario = usuarioRepository.findById(Objects.requireNonNull(usuarioId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        }

        Integer stockActualProducto = producto.getStockActual();
        int stockActual = stockActualProducto == null ? 0 : stockActualProducto.intValue();
        int nuevoStock = calcularNuevoStock(stockActual, tipo, cantidad);
        if (nuevoStock < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El movimiento dejaria el inventario en negativo");
        }

        producto.setStockActual(nuevoStock);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(tipo);
        movimiento.setCantidad(cantidad);
        movimiento.setObservacion(request.observacion());

        return mapearMovimiento(movimientoRepository.save(movimiento));
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarMovimientos(Long productoId) {
        Long id = Objects.requireNonNull(productoId, "El id del producto es obligatorio");
        return movimientoRepository.findByProductoIdOrderByFechaDesc(id)
                .stream()
                .map(this::mapearMovimiento)
                .toList();
    }

    private Producto obtenerProducto(Long id) {
        Long productoId = Objects.requireNonNull(id, "El id del producto es obligatorio");
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private Producto obtenerProductoParaModificar(Long id) {
        return productoRepository.buscarParaModificar(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private void aplicarDatosProducto(Producto producto, ProductoRequest request, String sku) {
        Categoria categoria = null;
        Long categoriaId = request.categoriaId();
        if (categoriaId != null) {
            categoria = categoriaRepository.findById(Objects.requireNonNull(categoriaId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
        }

        BigDecimal precioUnitario = request.precioUnitario();
        Integer stockActual = request.stockActual();
        Integer stockMinimo = request.stockMinimo();

        producto.setSku(sku);
        producto.setNombre(normalizar(request.nombre()));
        producto.setDescripcion(request.descripcion());
        producto.setMarca(normalizar(request.marca()));
        producto.setModelo(normalizar(request.modelo()));
        producto.setTalla(normalizar(request.talla()));
        producto.setRin(normalizar(request.rin()));
        producto.setUnidadMedida(normalizar(request.unidadMedida()));
        producto.setPrecioUnitario(precioUnitario == null ? BigDecimal.ZERO : precioUnitario);
        if (stockActual != null) {
            producto.setStockActual(stockActual);
        }
        producto.setStockMinimo(stockMinimo == null ? 0 : stockMinimo.intValue());
        producto.setClasificacionAbc(normalizarClasificacion(request.clasificacionAbc()));
        producto.setCategoria(categoria);
    }

    private int calcularNuevoStock(int stockActual, TipoMovimiento tipo, int cantidad) {
        return switch (tipo) {
            case ENTRADA, DEVOLUCION -> stockActual + cantidad;
            case SALIDA, MERMA -> stockActual - cantidad;
            case AJUSTE -> cantidad;
        };
    }

    private void validarProducto(ProductoRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La informacion del producto es obligatoria");
        }

        String sku = request.sku();
        String nombre = request.nombre();
        String unidadMedida = request.unidadMedida();
        Integer stockActual = request.stockActual();
        Integer stockMinimo = request.stockMinimo();
        BigDecimal precioUnitario = request.precioUnitario();

        if (sku == null || sku.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El SKU es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio");
        }
        if (unidadMedida == null || unidadMedida.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La unidad de medida es obligatoria");
        }
        validarLongitud(sku, 40, "El SKU");
        validarLongitud(nombre, 120, "El nombre");
        validarLongitud(request.descripcion(), 250, "La descripcion");
        validarLongitud(unidadMedida, 30, "La unidad de medida");
        validarLongitud(request.marca(), 60, "La marca");
        validarLongitud(request.modelo(), 80, "El modelo o referencia");
        validarLongitud(request.talla(), 30, "La talla");
        validarLongitud(request.rin(), 30, "El rin");
        if (stockActual != null && stockActual.intValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock actual no puede ser negativo");
        }
        if (stockMinimo != null && stockMinimo.intValue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock minimo no puede ser negativo");
        }
        if (precioUnitario != null && precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede ser negativo");
        }
        String clasificacion = normalizarClasificacion(request.clasificacionAbc());
        if (!List.of("A", "B", "C").contains(clasificacion)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La clasificacion ABC debe ser A, B o C");
        }
    }

    private void validarMovimiento(MovimientoRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La informacion del movimiento es obligatoria");
        }

        TipoMovimiento tipo = request.tipo();
        Integer cantidad = request.cantidad();

        if (tipo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de movimiento es obligatorio");
        }
        if (cantidad == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad es obligatoria");
        }
        if (tipo == TipoMovimiento.AJUSTE) {
            if (cantidad.intValue() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ajuste debe dejar un stock mayor o igual a cero");
            }
        } else if (cantidad.intValue() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor que cero");
        }
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.strip();
    }

    private void validarLongitud(String valor, int maximo, String campo) {
        if (valor != null && valor.length() > maximo) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    campo + " no puede superar " + maximo + " caracteres");
        }
    }

    private String normalizarClasificacion(String valor) {
        if (valor == null || valor.isBlank()) {
            return "C";
        }
        return valor.strip().toUpperCase();
    }

    private ProductoResponse mapearProducto(Producto producto) {
        Categoria categoria = producto.getCategoria();
        return new ProductoResponse(
                producto.getId(),
                producto.getSku(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getUnidadMedida(),
                producto.getPrecioUnitario(),
                producto.getStockActual(),
                producto.getStockMinimo(),
                producto.getCodigoQr(),
                producto.getClasificacionAbc(),
                categoria == null ? null : categoria.getId(),
                categoria == null ? null : categoria.getNombre(),
                producto.estaEnStockBajo(),
                producto.getFechaCreacion(),
                producto.getMarca(),
                producto.getModelo(),
                producto.getTalla(),
                producto.getRin()
        );
    }

    private MovimientoResponse mapearMovimiento(MovimientoInventario movimiento) {
        Usuario usuario = movimiento.getUsuario();
        Producto producto = movimiento.getProducto();
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getObservacion(),
                movimiento.getFecha(),
                producto.getId(),
                producto.getSku(),
                producto.getNombre(),
                usuario == null ? null : usuario.getId(),
                usuario == null ? null : usuario.getNombres()
        );
    }
}
