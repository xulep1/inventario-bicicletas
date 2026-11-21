# **Lectura de códigos QR en la tienda de bicicletas**

El sistema de inventario de bicicletas incorpora una funcionalidad de lectura de códigos QR que permite identificar los productos y acceder rápidamente a la información registrada en la base de datos. Esta herramienta facilita las consultas dentro de la tienda y reduce la necesidad de buscar manualmente cada artículo.

Para utilizar esta función desde un celular, es necesario que el programa esté ejecutándose en el computador y que ambos dispositivos estén conectados a una red que permita la comunicación entre ellos. Desde la pantalla principal del sistema se puede consultar el enlace de acceso para ingresar desde el teléfono.

Una de las formas de utilizar esta funcionalidad es mediante la cámara del celular. Al escanear el código QR, el dispositivo reconoce el enlace y permite abrir directamente la ficha del producto en el navegador. Allí se puede consultar la información correspondiente al artículo.

Otra alternativa consiste en utilizar la página `leer-qr.html`, que permite cargar una fotografía o una imagen descargada del código QR. El servidor utiliza la biblioteca ZXing para analizar la imagen y extraer la información contenida en ella. Para obtener una lectura correcta, la imagen debe mostrar el código completo, incluyendo su margen blanco, y no debe superar los 5 MB de tamaño. Se admiten archivos en formato PNG y JPG.

El sistema también permite utilizar un lector QR USB que funcione como teclado. Este dispositivo introduce automáticamente el contenido del código en el campo de texto de la pantalla de lectura. Después de ingresar la información, el usuario debe enviar el formulario para realizar la búsqueda del producto correspondiente.

## **Configuración y funcionamiento de los códigos QR**

Las imágenes generadas por el sistema tienen un tamaño de 400 × 400 píxeles, conservan un margen blanco alrededor del código y utilizan el nivel de corrección de errores M. Estas características permiten mejorar la lectura y facilitar el reconocimiento de la información almacenada.

Cada código QR contiene un enlace web que dirige a la ficha del producto mediante una dirección similar a `/producto.html?id=1`. Para construir este enlace, el sistema utiliza la dirección actual del servidor o una dirección personalizada configurada mediante la variable `APP_BASE_URL`.

Es importante diferenciar el contenido de la imagen QR del identificador interno almacenado en la base de datos. El campo `codigo_qr` conserva el identificador interno con el formato PATC, mientras que la imagen generada contiene el enlace web que permite acceder a la información del producto.

## **Consideraciones para su utilización**

Para garantizar el funcionamiento adecuado de esta herramienta, se deben tener en cuenta algunos aspectos técnicos.

Si cambia la dirección IP del computador donde se ejecuta el sistema, es posible que los códigos QR generados anteriormente dejen de dirigir a la ubicación correcta. En ese caso, será necesario generar nuevamente las etiquetas o configurar una dirección de red estable que permita conservar los mismos enlaces.

También es necesario realizar pruebas directamente en el establecimiento para comprobar que la cámara del celular funcione correctamente, que exista suficiente iluminación y que la conexión de red permita la comunicación entre los dispositivos.

Finalmente, es importante aclarar que la lectura de un código QR solamente permite identificar un producto y consultar su información. Escanear una etiqueta no modifica las cantidades disponibles ni registra automáticamente una entrada o salida del inventario. Para actualizar las existencias, se debe realizar el movimiento correspondiente dentro del sistema.

## **Conclusión**

La implementación de la lectura de códigos QR representa una mejora importante para el sistema de inventario de bicicletas, porque facilita la identificación de los productos y permite acceder a sus fichas de manera rápida y sencilla.

Además, ofrece diferentes alternativas de lectura, como la cámara de un celular, la carga de imágenes y los lectores USB. Esto proporciona mayor flexibilidad para utilizar el sistema en las actividades diarias de la tienda.

En general, esta funcionalidad contribuye a optimizar las consultas, mejorar la organización del inventario y facilitar el trabajo de las personas encargadas de administrar los productos.v
