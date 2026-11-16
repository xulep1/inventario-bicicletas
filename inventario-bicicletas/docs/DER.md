# Diagrama Entidad-Relacion propuesto

Este DER responde a la recomendacion del profesor: vincular el identificador del codigo QR con los registros de la base de datos.

```mermaid
erDiagram
    CATEGORIAS |o--o{ PRODUCTOS : clasifica
    PRODUCTOS ||--o{ MOVIMIENTOS_INVENTARIO : registra
    USUARIOS |o--o{ MOVIMIENTOS_INVENTARIO : realiza

    CATEGORIAS {
        bigint id PK
        varchar nombre UK
        varchar descripcion
        boolean activa
    }

    PRODUCTOS {
        bigint id PK
        varchar sku UK
        varchar nombre
        varchar descripcion
        varchar marca
        varchar modelo
        varchar talla
        varchar rin
        varchar unidad_medida
        decimal precio_unitario
        int stock_actual
        int stock_minimo
        varchar codigo_qr UK
        varchar clasificacion_abc
        bigint categoria_id FK
        datetime fecha_creacion
    }

    USUARIOS {
        bigint id PK
        varchar nombres
        varchar correo UK
        varchar rol
    }

    MOVIMIENTOS_INVENTARIO {
        bigint id PK
        varchar tipo
        int cantidad
        varchar observacion
        datetime fecha
        bigint producto_id FK
        bigint usuario_id FK
    }
```

## Relacion entre QR y base de datos

El campo `productos.codigo_qr` almacena el identificador interno de la referencia. El valor conserva esta estructura:

```text
PATC-INVENTARIO;PRODUCTO_ID=1;SKU=BIC-MTB-29-M
```

De esta forma, al leer el QR, el sistema puede buscar el producto asociado en la tabla `productos` por medio del campo `codigo_qr`, y tambien puede identificar el `PRODUCTO_ID` y el `SKU`.

La imagen QR generada contiene un enlace web a `/producto.html?id=...`, con la dirección actual del servidor. El lector reconoce tanto ese enlace como el identificador interno. El código interno no debe confundirse con la URL de la imagen.

Marca y modelo describen la referencia; talla y rin distinguen variantes cuando se necesitan saldos separados. Esos campos son opcionales. El modelo agrupa unidades por SKU y no identifica el serial físico de cada bicicleta.
