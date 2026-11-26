# Registro de asistencia de IA y Revisiones del Proyecto

La solicitud «continua con la fase 2» se trabajó con Codex y Gemini. A continuación se presentan el registro de consultas y el informe detallado de revisión para los tres componentes principales del proyecto **AvisosCampus**.

---

## Tabla de Consultas e Implementación

| Prompt para Gemini | Qué respondió Gemini | Qué implementé con Codex | Por qué |
| --- | --- | --- | --- |
| ¿Cómo puedo mostrar el texto completo de una notificación al expandirla y conservar su resumen en AndroidX? | Se debe usar `setContentText` para el resumen compacto y `.setStyle(NotificationCompat.BigTextStyle().bigText(...))` para permitir desplegar el texto completo sin truncarlo. | Añadí `NotificationCompat.BigTextStyle` a `Notificador` y conservé `setContentText`. | Android dispone del texto completo al expandir el aviso y del resumen para la vista compacta. El ViewModel sigue emitiendo el mismo `Aviso`. |
| ¿Cómo puedo registrar un canal Urgentes de importancia alta y enviar allí los recordatorios sin usar APIs de Android en el ViewModel? | Crear dos `NotificationChannel`: uno con `IMPORTANCE_DEFAULT` y otro con `IMPORTANCE_HIGH`. En el notificador, evaluar `aviso.origen == Origen.RECORDATORIO` para elegir el canal e importancia. | Registré ambos canales al iniciar en `AvisosApp`. `Notificador` selecciona Urgentes cuando el origen es `RECORDATORIO`. | El origen ya identifica el aviso. El canal y la prioridad se deciden donde se publica la notificación, manteniendo el ViewModel libre de `Context`. |
| ¿Cómo puedo escuchar las conexiones y desconexiones del cargador con un interruptor independiente y guardar los avisos en el historial? | Crear un `BroadcastReceiver` que escuche `ACTION_POWER_CONNECTED` y `ACTION_POWER_DISCONNECTED`. La UI registra/desregistra el receptor según el estado del `Switch` con `DisposableEffect`. | Añadí `CargadorReceiver`, el interruptor y el origen `CARGADOR`. La pantalla controla su ciclo de vida y se reutiliza el flujo de avisos existente. | El receptor solo traduce las dos acciones del sistema. La pantalla controla su ciclo de vida y el ViewModel guarda y emite el aviso sin acoplamiento. |

---

## Informes de Revisión Técnica

### 1. Revisión de `Notificador.kt` (`BigTextStyle` y Vista Compacta)

- **Qué se encontró:**
  - `Notificador.kt` utiliza `setContentText(aviso.texto)` para la vista compacta y `.setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))` para la vista expandida.
  - Se conserva el ID del aviso (`notify(aviso.id, notificacion)`).
  - El `PendingIntent` apunta a `MainActivity` y cuenta con la bandera obligatoria `PendingIntent.FLAG_IMMUTABLE`.
  - Se realiza la validación de permisos previa (`tienePermiso(context)`) comprobando `Manifest.permission.POST_NOTIFICATIONS` en Android 13+.
- **Qué cambiaría y por qué:**
  - No se requieren cambios. La implementación actual cumple al 100% con los estándares de AndroidX y los requerimientos del proyecto.
- **Prueba en emulador:**
  - **Prueba:** Enviar o registrar un aviso con texto largo (ejemplo: `"Esta es la primera línea del aviso extenso.\nAquí va la segunda línea con detalles del laboratorio.\nY la tercera línea solo se aprecia al expandir."`).
  - **Resultado esperado:**
    - *Vista compacta:* Se visualiza la primera línea resumida en la barra de estado/notificaciones.
    - *Vista expandida:* Al deslizar hacia abajo la notificación, se despliega el bloque de texto completo gracias a `BigTextStyle`.

---

### 2. Revisión de Canales de Notificaciones (`Avisos` y `Urgentes`)

- **Qué se encontró:**
  - En `AvisosApp.kt` (`Application.onCreate()`) se invoca `Notificador.crearCanal(this)`.
  - Se registran dos canales:
    - `"avisos"`: Nombre `"Avisos"`, importancia `IMPORTANCE_DEFAULT`.
    - `"urgentes"`: Nombre `"Urgentes"`, importancia `IMPORTANCE_HIGH`.
  - En `Notificador.mostrar(...)` se selecciona el canal y prioridad:
    - Si `aviso.origen == Origen.RECORDATORIO` → Canal `"urgentes"`, `PRIORITY_HIGH`.
    - Para los demás orígenes (`BOTON`, `MODO_AVION`, `CARGADOR`) → Canal `"avisos"`, `PRIORITY_DEFAULT`.
  - `AvisosViewModel.kt` se mantiene 100% limpio y libre de referencias a `Context` o `NotificationManager`.
- **Archivos revisados:**
  - `AvisosApp.kt`
  - `notificacion/Notificador.kt`
  - `viewmodel/AvisosViewModel.kt`
  - `model/Modelos.kt`
- **Prueba en emulador:**
  1. Abrir *Ajustes → Aplicaciones → AvisosCampus → Notificaciones* en el emulador y verificar la presencia de las categorías **Avisos** y **Urgentes**.
  2. Presionar el botón *"Mostrar aviso"*: la notificación aparece sin sonido invasivo en el canal **Avisos**.
  3. Presionar *"Recordar en 10 s"*: transcurrido el tiempo, la notificación se publica con aviso flotante (*heads-up*) e importancia alta en el canal **Urgentes**.

---

### 3. Revisión del Receptor del Cargador (`CargadorReceiver`)

- **Qué se encontró:**
  - `CargadorReceiver.kt`: El método `onReceive` únicamente interpreta las acciones `ACTION_POWER_CONNECTED` y `ACTION_POWER_DISCONNECTED` y comunica el estado booleano mediante un callback lambda `(Boolean) -> Unit`.
  - `PantallaAvisos.kt`: Maneja la activación y desactivación dinámica mediante `DisposableEffect(contexto, escuchandoCargador)` registrando el receptor con un `IntentFilter` y desregistrándolo en `onDispose`. El interruptor de cargador es completamente independiente al del modo avión.
  - `AvisosViewModel.kt`: Al recibir el evento (`alCambiarCargador`), registra el aviso con origen `CARGADOR` ("Cargador · Conectado" / "Cargador · Desconectado"), lo guarda en el repositorio/historial y lo emite a `_eventos`. No incluye importaciones de `Context`, `NotificationManager` ni `BroadcastReceiver`.
- **Qué cambiaría y por qué:**
  - No requiere cambios. Mantiene una arquitectura limpia y reactiva.
- **Pruebas en emulador (3 casos):**
  1. **Caso 1: Interruptor activo + Conectar cargador:**
     - Activar el Switch *"Escuchar el cargador"*.
     - Ejecutar en la consola: `adb shell dumpsys battery set ac 1`.
     - *Resultado:* Se publica la notificación `"Cargador · Conectado"` y se añade al historial.
  2. **Caso 2: Interruptor activo + Desconectar cargador:**
     - Con el Switch activo, ejecutar: `adb shell dumpsys battery set ac 0` (y luego `adb shell dumpsys battery reset`).
     - *Resultado:* Se publica la notificación `"Cargador · Desconectado"` y se guarda en el historial.
  3. **Caso 3: Interruptor apagado + Cambio de energía:**
     - Desactivar el Switch *"Escuchar el cargador"*.
     - Cambiar el estado de la batería mediante el panel o comandos `adb`.
     - *Resultado:* El receptor está desregistrado; no se genera ninguna notificación ni entrada en el historial.
