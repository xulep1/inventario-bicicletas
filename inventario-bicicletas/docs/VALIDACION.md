# Validación de la adaptación a bicicletas

Fecha de comprobación: 2 de noviembre de 2026.

## Pruebas automatizadas en Java

Se ejecutó Gradle `test` con Java 17: **11 pruebas, 0 fallos y 0 errores**. La compilación del código y la generación del JAR también finalizaron correctamente.

Las pruebas están en `src/test/java/com/patc/inventarioqr` y cubren:

- Generar un PNG, decodificarlo con ZXing y comprobar el enlace a la ficha con la dirección del servidor recibida.
- Volver a enviar el PNG al endpoint de lectura y recuperar el mismo producto sin alterar sus existencias.
- Comprobar que el enlace mostrado en inicio coincide con el codificado en la imagen.
- Leer el identificador PATC de etiquetas anteriores.
- Rechazar imágenes vacías, archivos que no son imágenes e imágenes sin QR legible.
- Informar un código desconocido o un producto inexistente.
- Registrar marca, modelo, talla y rin y rechazar un SKU repetido.
- Actualizar entradas y salidas, rechazar una salida superior al saldo y conservar el historial.
- Admitir un ajuste a cero y rechazar una cantidad negativa.
- Respetar una dirección configurada o recibida y rechazar una configuración inválida.

## Verificación de la aplicación en ejecución

Se inició el JAR real con una base H2 de pruebas en archivo. Se utilizó Chrome for Testing 130.0.6723.116 mediante automatización de navegador, con vista de escritorio de 1280 × 900 y vista de celular de 375 × 812 píxeles.

| Comprobación | Resultado |
| --- | --- |
| Navegar desde inicio a una categoría y registrar una referencia | Correcto |
| Buscar la referencia y rechazar un SKU duplicado desde el formulario | Correcto |
| Guardar una entrada de 5 y una salida de 3 sobre un saldo inicial de 4 | Saldo final 6 |
| Intentar una salida superior al saldo | Rechazada sin cambiar las existencias |
| Ajustar el inventario a cero desde el formulario y después fijarlo en 6 | Correcto |
| Subir el PNG generado y abrir el producto identificado | Correcto y sin cambiar el stock |
| Enviar una imagen inválida y leer el texto de una etiqueta anterior | Error controlado y búsqueda correcta |
| Mostrar inicio, ficha y lectura QR a 375 píxeles de ancho | Sin desbordamiento horizontal |
| Ejecutar el recorrido sin errores JavaScript | Correcto |
| Detener el proceso y reiniciarlo usando el mismo archivo H2 | Referencia, saldo e historial conservados |

El entorno de pruebas no expone una dirección privada de red utilizable para la detección automática. Se verificó que la interfaz muestra el aviso correspondiente. Las direcciones privadas y la configuración opcional se comprobaron en las pruebas Java; esto no sustituye una conexión física desde el celular en la red de la tienda.

## Comprobaciones pendientes en el establecimiento

- Abrir desde un celular el enlace mostrado en inicio y escanear una etiqueta recién generada.
- Confirmar la conexión Wi-Fi, el acceso de Java a la red privada y la legibilidad de etiquetas impresas.
- Validar el flujo con el personal y registrar versiones de Edge y Firefox si se incluyen en la evaluación.
- Medir rendimiento con el volumen real de referencias.

No se han implementado el inicio de sesión y los permisos por perfil. La puesta en marcha de MySQL permanece fuera de esta comprobación. Las pruebas se realizaron con datos ficticios; las bases y capturas usadas para comprobar la aplicación no se incluyen en el ZIP.
