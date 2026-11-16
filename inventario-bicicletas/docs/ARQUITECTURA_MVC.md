# Arquitectura del inventario de bicicletas

## 1. Visión general

El proyecto sigue una estructura **MVC ampliada** con capas de servicios y repositorios. Se ejecuta como aplicación web Java con Spring Boot y expone una API REST.

## 2. Estructura de paquetes

```text
src/main/java/com/patc/inventarioqr
├── controller   -> Expone la API REST con @RestController
├── service      -> Contiene la lógica de negocio
├── model        -> Entidades JPA que representan tablas
├── repository   -> Interfaces JpaRepository para persistencia
├── dto          -> Objetos de entrada y salida de la API
├── config       -> Carga de datos iniciales
└── util         -> Funciones auxiliares, como el contenido del QR
```

## 3. Tecnologías y relación con Programación III

| Tecnología | Aporte al proyecto |
|---|---|
| **Spring Boot** | Ejecuta el sistema como aplicación Java web. |
| **API REST** | Crea endpoints HTTP para productos, categorías, movimientos y reportes. |
| **JPA** | Guarda y consulta entidades en una base de datos relacional sin escribir SQL para cada operación. |
| **MVC** | Separa responsabilidades para que el código sea más ordenado y fácil de explicar. |
| **ZXing** | Genera y lee códigos QR desde Java. |

## 4. Adaptación a la tienda de bicicletas

- Las vistas estáticas organizan **Bicicletas, Repuestos y Accesorios**.
- Los productos incluyen **marca, modelo, talla y rin** como campos opcionales.
- Cada referencia o variante que se cuenta por separado conserva un **SKU único**.

## 5. Funcionamiento del QR

1. `ConexionService` calcula el origen del enlace de la ficha y permite configurar una dirección propia con `APP_BASE_URL`.
2. `ProductoController` genera el PNG del QR con ese enlace.
3. `QrService` codifica y decodifica las imágenes con ZXing.
4. La pantalla `leer-qr.html` envía la imagen o el texto para identificar el producto.

## 6. Persistencia e integridad de datos

- **Base de datos predeterminada:** H2 en archivo, dentro de la carpeta `data`.
- **Movimientos:** se guardan junto con la actualización del saldo dentro de una **transacción**.
- **Integridad:** no se puede eliminar un producto que tenga historial.

## 7. Alcance y limitaciones de esta entrega

- El almacenamiento con **MySQL** no está validado.
- La **autenticación** no está validada.
