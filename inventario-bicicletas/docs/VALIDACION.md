# **Validación y pruebas de adaptación del sistema a bicicletas**

**Fecha de comprobación: 2 de noviembre de 2026.**

La validación del sistema de inventario de bicicletas se realizó con el propósito de comprobar que las funcionalidades desarrolladas trabajen correctamente y que la adaptación a las necesidades de una tienda de bicicletas cumpla con los requisitos establecidos.

Durante este proceso se realizaron pruebas automatizadas en Java y verificaciones directamente sobre la aplicación en funcionamiento. Estas pruebas permitieron evaluar el registro de productos, el control de existencias, la generación y lectura de códigos QR y la conservación de la información almacenada.

## **1. Pruebas automatizadas en Java**

Se ejecutaron las pruebas del proyecto mediante Gradle, utilizando Java 17. Como resultado, se obtuvieron 11 pruebas satisfactorias, sin fallos ni errores. Además, la compilación del código fuente y la generación del archivo JAR finalizaron correctamente.

Las pruebas se encuentran organizadas dentro de la carpeta `src/test/java/com/patc/inventarioqr` y permitieron comprobar los siguientes aspectos:

- **Generación de códigos QR:** se verificó que el sistema pueda crear imágenes PNG, leerlas mediante ZXing y comprobar que contengan el enlace correcto hacia la ficha del producto.
- **Lectura de códigos QR:** se comprobó que las imágenes generadas puedan enviarse al servicio de lectura y que el sistema identifique el producto sin modificar sus existencias.
- **Validación de enlaces:** se verificó que la dirección mostrada en la pantalla de inicio coincida con la dirección almacenada en el código QR.
- **Compatibilidad con códigos anteriores:** se comprobó que el sistema pueda reconocer identificadores PATC utilizados en etiquetas anteriores.
- **Validación de imágenes:** se realizaron pruebas con archivos vacíos, formatos no compatibles e imágenes que no contienen códigos QR legibles, comprobando que el sistema responda correctamente.
- **Identificación de productos:** se verificó que los códigos desconocidos y los productos inexistentes generen mensajes de error controlados.
- **Registro de bicicletas:** se comprobó el almacenamiento de características como marca, modelo, talla y rin, además de la restricción de códigos SKU duplicados.
- **Control de movimientos:** se verificó el registro de entradas y salidas, el rechazo de operaciones que superen las existencias disponibles y la conservación del historial.
- **Ajustes de inventario:** se comprobó que el sistema permita establecer las existencias en cero y que rechace cantidades negativas.
- **Configuración de direcciones:** se verificó el funcionamiento de las direcciones configuradas o recibidas por el servidor y el rechazo de configuraciones incorrectas.

## **2. Verificación de la aplicación en funcionamiento**

También se realizaron pruebas utilizando el archivo JAR generado por el proyecto y una base de datos H2 almacenada en archivos de prueba.

Para estas comprobaciones se utilizó Chrome for Testing, versión 130.0.6723.116, mediante herramientas de automatización del navegador. Se evaluó el comportamiento de la aplicación en dos tamaños de pantalla: escritorio, con una resolución de 1280 × 900 píxeles, y celular, con una resolución de 375 × 812 píxeles.

Durante la ejecución se comprobaron diferentes funcionalidades:

**Navegación y registro de productos:** se verificó que desde la pantalla principal fuera posible ingresar a las categorías y registrar nuevas referencias de bicicletas.

**Búsqueda y validación de productos:** se comprobó que el sistema permitiera localizar referencias registradas y rechazara los códigos SKU repetidos para evitar duplicaciones.

**Control de entradas y salidas:** se realizó una prueba con un saldo inicial de 4 unidades. Posteriormente, se registró una entrada de 5 unidades y una salida de 3, obteniendo un saldo final de 6 unidades.

**Restricción de movimientos:** se intentó registrar una salida superior a las existencias disponibles. El sistema rechazó la operación y mantuvo intacta la cantidad almacenada.

**Ajustes de inventario:** se verificó que fuera posible modificar las existencias hasta llegar a cero y posteriormente establecerlas nuevamente en 6 unidades.

**Lectura de códigos QR:** se cargó una imagen PNG generada por el sistema y se comprobó que permitiera identificar y abrir el producto correspondiente sin modificar su stock.

**Validación de archivos:** se enviaron imágenes no válidas y se comprobó que el sistema mostrara errores controlados. También se verificó la lectura de identificadores pertenecientes a etiquetas anteriores.

**Adaptación a dispositivos móviles:** se revisaron las pantallas de inicio, ficha del producto y lectura QR en una resolución de 375 píxeles de ancho, sin presentar desbordamientos horizontales.

**Funcionamiento general:** se ejecutó el recorrido completo de la aplicación y no se detectaron errores de JavaScript durante las pruebas.

**Persistencia de la información:** finalmente, se detuvo la aplicación y se inició nuevamente utilizando el mismo archivo de base de datos H2. Se comprobó que los productos, las cantidades disponibles y el historial de movimientos permanecieran almacenados correctamente.

## **3. Resultados generales de las pruebas**

Los resultados obtenidos permiten establecer que las principales funcionalidades del sistema respondieron correctamente durante las verificaciones realizadas.

Se comprobó que el registro de productos funciona adecuadamente, que las entradas y salidas actualizan las existencias de manera correcta y que las operaciones inválidas son rechazadas sin afectar los datos almacenados.

También se verificó que los códigos QR permiten identificar los productos, que las imágenes pueden procesarse mediante ZXing y que la información permanece disponible después de reiniciar la aplicación.

Por otra parte, la interfaz presentó un comportamiento adecuado tanto en pantallas de escritorio como en dispositivos móviles, facilitando la navegación y el acceso a las diferentes funcionalidades.

## **4. Comprobaciones pendientes en el establecimiento**

Aunque las pruebas realizadas fueron satisfactorias, todavía existen algunas verificaciones que deben efectuarse directamente en la tienda de bicicletas para comprobar el funcionamiento en condiciones reales.

Entre las actividades pendientes se encuentran:

- Acceder desde un celular al enlace mostrado en la pantalla principal y comprobar la lectura de una etiqueta QR recién generada.
- Verificar que la conexión Wi-Fi permita la comunicación entre el computador y los dispositivos móviles.
- Comprobar que Java tenga acceso a la red privada del establecimiento.
- Evaluar la legibilidad de los códigos QR después de imprimir las etiquetas.
- Realizar pruebas con el personal encargado del inventario para comprobar la facilidad de uso del sistema.
- Verificar el funcionamiento en otros navegadores, como Microsoft Edge y Mozilla Firefox, si se incluyen dentro de la evaluación.
- Medir el rendimiento de la aplicación utilizando una cantidad de productos similar a la que manejaría la tienda en sus actividades diarias.

Es importante mencionar que el entorno utilizado para las pruebas no proporcionó una dirección privada de red que pudiera detectarse automáticamente. Por esta razón, se verificó que la interfaz mostrara el aviso correspondiente. Las direcciones privadas y las configuraciones opcionales fueron comprobadas mediante pruebas automatizadas en Java, pero todavía es necesario realizar una conexión física desde un celular conectado a la red de la tienda.

## **5. Limitaciones de la versión evaluada**

Durante la validación también se identificaron algunas funcionalidades que todavía no están implementadas o comprobadas.

En primer lugar, el sistema no cuenta con un inicio de sesión validado ni con permisos diferenciados según el perfil de cada usuario. Esto significa que la administración de accesos todavía requiere desarrollo.

En segundo lugar, la conexión con MySQL permanece pendiente, debido a que las pruebas realizadas utilizaron H2 como base de datos de almacenamiento.

Finalmente, todas las verificaciones se realizaron con información ficticia. Los archivos de bases de datos y las capturas utilizadas durante las pruebas no se encuentran incluidos en el archivo ZIP entregado.

## **6. Conclusión**

La validación realizada permitió comprobar que el sistema de inventario de bicicletas cumple satisfactoriamente con las principales funcionalidades desarrolladas. Las 11 pruebas automatizadas finalizaron sin errores, y las verificaciones de la aplicación demostraron que es posible registrar productos, controlar entradas y salidas, consultar existencias y conservar el historial de movimientos.

Asimismo, se confirmó el funcionamiento de la generación y lectura de códigos QR, permitiendo relacionar las etiquetas con los productos almacenados en la base de datos. También se comprobó que la aplicación puede utilizarse desde diferentes tamaños de pantalla y que la información permanece guardada después de reiniciar el programa.

En conclusión, los resultados demuestran que el proyecto cuenta con una base funcional para apoyar la administración del inventario de una tienda de bicicletas. Sin embargo, antes de su implementación definitiva, es necesario realizar pruebas en el establecimiento y completar aspectos pendientes, como la conexión con MySQL y la autenticación de usuarios.
