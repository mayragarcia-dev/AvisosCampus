# Registro de asistencia de IA

La solicitud «continua con la fase 2» se trabajó con Codex. El acceso automatizado a Android Studio falló al iniciar el control de la aplicación. Las consultas de la tabla son borradores para Gemini y están pendientes de enviar.

| Prompt para Gemini | Qué respondió Gemini | Qué implementé con Codex | Por qué |
| --- | --- | --- | --- |
| ¿Cómo puedo mostrar el texto completo de una notificación al expandirla y conservar su resumen en AndroidX? | Pendiente de consulta. | Añadí `NotificationCompat.BigTextStyle` a `Notificador` y conservé `setContentText`. | Android dispone del texto completo al expandir el aviso y del resumen para la vista compacta. El ViewModel sigue emitiendo el mismo `Aviso`. |
