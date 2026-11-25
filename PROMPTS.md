# Registro de asistencia de IA

La solicitud «continua con la fase 2» se trabajó con Codex. El acceso automatizado a Android Studio falló al iniciar el control de la aplicación. Las consultas de la tabla son borradores para Gemini y están pendientes de enviar.

| Prompt para Gemini | Qué respondió Gemini | Qué implementé con Codex | Por qué |
| --- | --- | --- | --- |
| ¿Cómo puedo mostrar el texto completo de una notificación al expandirla y conservar su resumen en AndroidX? | Pendiente de consulta. | Añadí `NotificationCompat.BigTextStyle` a `Notificador` y conservé `setContentText`. | Android dispone del texto completo al expandir el aviso y del resumen para la vista compacta. El ViewModel sigue emitiendo el mismo `Aviso`. |
| ¿Cómo puedo registrar un canal Urgentes de importancia alta y enviar allí los recordatorios sin usar APIs de Android en el ViewModel? | Pendiente de consulta. | Registré ambos canales al iniciar. `Notificador` selecciona Urgentes cuando el origen es `RECORDATORIO`. | El origen ya identifica el aviso. El canal y la prioridad se deciden donde se publica la notificación. |
| ¿Cómo puedo escuchar las conexiones y desconexiones del cargador con un interruptor independiente y guardar los avisos en el historial? | Pendiente de consulta. | Añadí `CargadorReceiver`, el interruptor y el origen `CARGADOR`. La pantalla registra y desregistra el receptor. El ViewModel registra cada cambio y emite el aviso. | El receptor solo traduce las dos acciones del sistema. La pantalla controla su ciclo de vida y se reutiliza el flujo de avisos existente. |
