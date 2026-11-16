# Arquitectura del inventario de bicicletas

El proyecto esta organizado siguiendo una estructura MVC ampliada con capa de servicios y repositorios:

```text
src/main/java/com/patc/inventarioqr
├── controller   -> Expone la API REST con @RestController
├── service      -> Contiene la logica de negocio
├── model        -> Entidades JPA que representan tablas
├── repository   -> Interfaces JpaRepository para persistencia
├── dto          -> Objetos de entrada y salida de la API
├── config       -> Carga de datos iniciales
└── util         -> Funciones auxiliares, como el contenido del QR
```

## Relacion con Programacion III

- Spring Boot: permite ejecutar el sistema como aplicacion Java web.
- API REST: permite crear endpoints HTTP para productos, categorias, movimientos y reportes.
- JPA: permite guardar y consultar entidades en una base de datos relacional sin escribir SQL para cada operacion.
- MVC: separa responsabilidades para que el codigo sea mas ordenado y facil de explicar.
- ZXing: permite generar y leer codigos QR desde Java.

## Adaptación a la tienda de bicicletas

Las vistas estáticas organizan Bicicletas, Repuestos y Accesorios. Los productos incluyen marca, modelo, talla y rin opcionales. Cada referencia o variante que se cuenta por separado conserva un SKU único.

`ConexionService` calcula el origen del enlace de ficha y permite una dirección configurada con `APP_BASE_URL`. `ProductoController` genera el PNG con ese enlace; `QrService` codifica y decodifica las imágenes con ZXing. La pantalla `leer-qr.html` envía la imagen o el texto para identificar el producto.

La configuración predeterminada usa H2 en archivo dentro de `data`. El almacenamiento con MySQL y la autenticación no están validados en esta entrega. Los movimientos se guardan con la actualización del saldo dentro de una transacción; se impide eliminar un producto con historial.
