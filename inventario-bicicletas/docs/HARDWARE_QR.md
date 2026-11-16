# Lectura QR en la tienda de bicicletas

La cámara habitual del celular puede leer el enlace del QR y abrir la ficha del producto. El programa debe estar abierto en el computador y ambos equipos deben compartir una red que permita comunicarse. La pantalla de inicio muestra el enlace de acceso.

También se puede descargar o fotografiar una etiqueta y subir la imagen desde `leer-qr.html`. El servidor utiliza ZXing para extraer su contenido. El archivo PNG o JPG debe mostrar la etiqueta completa, incluido el margen blanco, y pesar como máximo 5 MB.

Un lector USB que funcione como teclado puede escribir el contenido en el campo de texto de esa pantalla. La búsqueda se realiza al enviar el formulario.

La versión ajustada genera imágenes de 400 × 400 píxeles, conserva un margen blanco y emplea corrección de errores M. Los QR enlazan `/producto.html?id=...` utilizando la dirección actual del servidor o `APP_BASE_URL` si se configura. El campo `codigo_qr` mantiene el identificador interno PATC; la imagen contiene el enlace web.

Si cambia la IP del computador, hay que regenerar las etiquetas o mantener una dirección de red estable. Los QR antiguos con otra dirección no se actualizan por sí solos.

La cámara física, las condiciones de iluminación y la conectividad de la tienda deben comprobarse en el establecimiento. Leer una etiqueta no cambia las existencias.
