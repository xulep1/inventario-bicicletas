# Inventario de bicicletas

Aplicación web responsiva para gestionar bicicletas, repuestos y accesorios, con registro de productos, movimientos, existencias y códigos QR. Desarrollada con **Java 17**, **Spring Boot**, **HTML**, **CSS**, **JavaScript** y **H2**.

## 1. Iniciar la aplicación en Windows

1. Instala **JDK 17** y comprueba su configuración con `java -version` y `javac -version`.
2. Descarga el repositorio desde **Code → Download ZIP** y extrae todo.
3. En **Visual Studio Code**, abre la carpeta que contiene `build.gradle` y `gradlew.bat`.
4. Abre **Terminal → Nueva terminal** y ejecuta en PowerShell:

   ```powershell
   .\gradlew.bat bootRun
   ```

5. Espera al mensaje `Started InventarioQrApplication` y abre **http://localhost:8080**.

### Notas importantes

- El primer inicio necesita internet para descargar Gradle y las dependencias.
- Mantén la terminal abierta mientras usas el inventario. Para detenerlo, presiona **Ctrl + C**.
- También puedes abrir `INICIAR_WINDOWS.bat` con doble clic, con JDK 17 ya configurado. Este iniciador compila y arranca desde el código fuente.

### Datos

- La base H2 se crea automáticamente en `data`.
- Los datos iniciales son ejemplos ficticios.
- Para conservar tus registros, inicia siempre desde la misma carpeta y conserva `data`.

## 2. Iniciar en Linux o macOS

Con JDK 17 instalado:

```bash
sh iniciar.sh
```

## 3. Probar los QR desde el celular

1. Conecta el celular y el computador a la misma red Wi-Fi y mantén el servidor abierto.
2. En el inicio del inventario, usa el enlace **Abrir desde el celular**.
3. Genera un QR nuevo desde la ficha de un producto y escanéalo con la cámara.
4. Si cambia la IP del computador, vuelve a generar el QR.

## 4. Archivos de la entrega

| Archivo o carpeta | Contenido |
|---|---|
| `src` | Código de la aplicación y pruebas. |
| `gradle`, `gradlew`, `gradlew.bat` | Gradle Wrapper para compilar e iniciar. |
| `build.gradle`, `settings.gradle` | Configuración del proyecto. |
| `INICIAR_WINDOWS.bat`, `iniciar.sh` | Scripts de inicio rápido para Windows y Linux/macOS. |
| `README_USO_RAPIDO.md` | Instrucciones completas y solución de problemas. |
| `docs` | Arquitectura, requisitos y resultados de las verificaciones anteriores. |
| `postman` | Ejemplos de peticiones a la API. |
| `historial-inventario-bicicletas.bundle` | Respaldo opcional de las dos versiones Git anteriores; se conserva como referencia histórica. Para ejecutar esta entrega utiliza los archivos del repositorio actual. |

## 5. Subir esta carpeta a GitHub

1. Después de extraer el ZIP, abre la carpeta `inventario-bicicletas`.
2. En GitHub, usa **Add file → Upload files** y arrastra su contenido conservando las carpetas `src`, `gradle`, `docs` y `postman`.
3. Incluye también los archivos de la raíz (`gradlew`, `gradlew.bat`, `build.gradle`, `settings.gradle`, `INICIAR_WINDOWS.bat`, `iniciar.sh` y este `README.md`), evitando subir `build`, `.gradle` o `data` si ya ejecutaste el proyecto localmente.
4. Guarda los cambios con **Commit changes**.

Esta versión permite revisar el código directamente en GitHub. Las dependencias se descargan al compilar y los archivos `build`, `.gradle` y `data` se generan localmente.
