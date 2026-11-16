@echo off
setlocal
cd /d "%~dp0"
echo INVENTARIO DE LA TIENDA DE BICICLETAS
echo.
echo Esta version inicia desde el codigo fuente con Gradle.
echo Requisito: JDK 17 instalado y configurado.
echo La primera ejecucion necesita internet y puede tardar varios minutos.
echo Manten abierta esta ventana mientras utilizas la aplicacion.
echo Cuando termine de iniciar, abre http://localhost:8080 en tu navegador.
echo Para detener el programa, pulsa Ctrl+C.
echo.
call "%~dp0gradlew.bat" bootRun
if errorlevel 1 (
    echo.
    echo No fue posible iniciar. Revisa el error mostrado arriba.
    echo Comprueba que tienes el JDK 17, conexion a internet
    echo y que el puerto 8080 no esta ocupado por otra aplicacion.
    echo Consulta README.md para obtener instrucciones.
    pause
)
endlocal
