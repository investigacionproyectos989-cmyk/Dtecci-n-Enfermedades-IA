TomatoIA_V1 - proyecto completo con ajustes preventivos del historial

Este ZIP contiene el proyecto Android completo (código fuente, recursos, modelo TFLite,
configuración de Gradle y Gradle Wrapper). Se excluyeron las carpetas generadas de compilación,
las cachés locales, el directorio Git y local.properties porque son específicos del equipo.
Android Studio puede regenerar esos archivos al abrir el proyecto.

Cambios aplicados:
1. HistorialViewModel.kt: captura y registra excepciones por consulta de Room para evitar que
   una excepción no gestionada del flujo cierre la pantalla; registra errores al eliminar.
2. HistorialFragment.kt: elimina el filtro antiguo Gray_mold y valida el índice del filtro.
3. HistorialAdapter.kt: muestra una imagen alternativa si la URI de imagen no puede cargarse.
4. MainActivity.kt: protege el retorno desde CameraActivity y registra errores de navegación.

Importante: estos cambios son preventivos; no se ha podido ejecutar una compilación en este entorno
porque Gradle necesita descargar la distribución 8.14.5 y el entorno no tiene acceso a Internet.
Por tanto, el cierre exacto no puede considerarse confirmado ni solucionado hasta probar en Android
Studio y, si persiste, revisar Logcat (FATAL EXCEPTION / Caused by).

Para abrir: descomprimir el ZIP y abrir la carpeta TomatoIA_V1 en Android Studio. Dejar que Gradle
sincronice y ejecutar Build > Rebuild Project. No borrar los datos de la aplicación si se desea
conservar el historial local del dispositivo.
