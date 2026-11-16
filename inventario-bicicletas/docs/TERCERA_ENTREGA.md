# Evidencias de la tercera entrega

## 1. Puntos solicitados por la docente

El avance se concreta en los tres puntos de la diapositiva de la docente.

| Punto solicitado | Implementación y evidencia |
|---|---|
| **Interfaces navegables y responsivas** | Inicio por categorías, catálogo de referencias, ficha con movimientos y pantalla de lectura QR. Enlaces para volver a categorías o productos y CSS con rejillas adaptables. |
| **Formularios y eventos funcionales** | Crear categoría, registrar producto, filtrar referencias, guardar movimientos y leer una imagen o texto QR. Los eventos `submit`, `input` y `change` actualizan la interfaz; `fetch` comunica cada operación con la API. |
| **Avance del repositorio** | Código fuente actualizado, pruebas automatizadas, Gradle Wrapper, documentación e historial Git local con una versión inicial y la adaptación a bicicletas. Se entrega un archivo bundle para conservar ambos commits. |

## 2. Historial y repositorio del equipo

### Historial local

El historial local se incluye en `historial-inventario-bicicletas.bundle`. Desde una terminal con Git puede recuperarse en una carpeta nueva:

```sh
git clone historial-inventario-bicicletas.bundle inventario-bicicletas-fuente
cd inventario-bicicletas-fuente
git log --oneline
```

### Alcance de la entrega

- El bundle conserva las dos versiones Git anteriores como referencia histórica.
- Esta entrega ligera incorpora instrucciones e iniciadores para ejecutar el código actual mediante Gradle (`bootRun`); consulta `README.md` y utiliza los archivos actuales del repositorio para probarla.
- El archivo compilado y sus dependencias se generan localmente.
- No se incluyen bases de datos de prueba en el historial.

### Repositorio remoto

- No se ha vinculado ni publicado un repositorio remoto porque no se proporcionó el enlace del repositorio del equipo.
- Para la entrega académica, el equipo debe incorporar los cambios en su repositorio y adjuntar su URL real.
- Si ya existe un historial del equipo, se deben integrar allí los archivos conservando su historia, sin reemplazarla por la del bundle.

## 3. Recorrido sugerido para la demostración

1. Abrir inicio y seleccionar **Bicicletas**.
2. Registrar una referencia con SKU, marca, modelo, talla y rin.
3. Consultar la ficha y descargar su QR.
4. Leer la imagen desde la pantalla de lectura y abrir el producto encontrado.
5. Registrar una entrada y una salida y comprobar el historial.
6. Intentar una salida superior al saldo y mostrar el rechazo.
7. Consultar la ficha en un celular conectado a la misma red.
8. Cerrar y reiniciar el programa desde la misma carpeta para verificar los datos.

## 4. Notas finales

- Los productos y precios de ejemplo son ficticios.
- La demostración en la red del establecimiento debe realizarse allí.
- Los resultados de las verificaciones efectuadas se detallan en `VALIDACION.md`.
