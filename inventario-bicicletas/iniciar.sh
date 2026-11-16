#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
echo 'Se requiere JDK 17. La primera ejecucion necesita internet.'
echo 'Abre http://localhost:8080 cuando termine de iniciar. Ctrl+C para detener.'
exec sh ./gradlew bootRun
