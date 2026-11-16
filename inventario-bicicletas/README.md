# Inventario de bicicletas

Aplicación web responsiva para gestionar bicicletas, repuestos y accesorios, con registro de productos, movimientos, existencias y códigos QR. Desarrollada con Java 17, Spring Boot, HTML, CSS, JavaScript y H2.

## Iniciar la aplicación en Windows

1. Instala **JDK 17** y comprueba su configuración con `java -version` y `javac -version`.
2. Descarga el repositorio desde **Code → Download ZIP** y extrae todo.
3. En **Visual Studio Code**, abre la carpeta que contiene `build.gradle` y `gradlew.bat`.
4. Abre **Terminal → Nueva terminal** y ejecuta en PowerShell:

   ```powershell
   .\gradlew.bat bootRun
   ```

5. Espera al mensaje `Started InventarioQrApplication` y abre **http://localhost:8080**.

El primer inicio necesita internet para descargar Gradle y las dependencias. Mantén la terminal abierta mientras usas el inventario. Para detenerlo, presiona **Ctrl + C**.

También puedes abrir `INICIAR_WINDOWS.bat` con doble clic, con JDK 17 ya configurado. Este iniciador compila y arranca desde el código fuente.

La base H2 se crea automáticamente en `data`. Los datos iniciales son ejemplos ficticios. Para conservar tus registros, inicia siempre desde la misma carpeta y conserva `data`.

## Probar los QR desde el celular

Conecta el celular y el computador a la misma red Wi-Fi y mantén el servidor abierto. En el inicio del inventario, usa el enlace **Abrir desde el celular**. Genera un QR nuevo desde la ficha de un producto y escanéalo con la cámara. Si cambia la IP del computador, vuelve a generar el QR.

## Archivos de la entrega

- `src`: código de la aplicación y pruebas.
- `gradle`, `gradlew` y `gradlew.bat`: Gradle Wrapper para compilar e iniciar.
- `build.gradle` y `settings.gradle`: configuración del proyecto.
- `README_USO_RAPIDO.md`: instrucciones completas y solución de problemas.
- `docs`: arquitectura, requisitos y resultados de las verificaciones anteriores.
- `postman`: ejemplos de peticiones a la API.
- `historial-inventario-bicicletas.bundle`: respaldo opcional de las dos versiones Git anteriores; se conserva como referencia histórica. Para ejecutar esta entrega utiliza los archivos del repositorio actual.

Para trabajar en Linux o macOS, con JDK 17: `sh iniciar.sh`.

## Subir esta carpeta a GitHub

Después de extraer el ZIP, abre `inventario-bicicletas`. En GitHub, usa **Add file → Upload files** y arrastra su contenido conservando las carpetas `src`, `gradle`, `docs` y `postman`. Incluye también todos los archivos de la raíz, especialmente `gradlew.bat`, `build.gradle`, `settings.gradle` y este `README.md`. Guarda los cambios con **Commit changes** o el botón equivalente que muestre tu repositorio.

Esta versión permite revisar el código directamente en GitHub. Las dependencias se descargan al compilar y los archivos `build`, `.gradle` y `data` se generan localmente.
