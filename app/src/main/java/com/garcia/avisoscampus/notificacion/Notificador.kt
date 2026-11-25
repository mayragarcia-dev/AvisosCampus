package com.garcia.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.garcia.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notificaciones de la app"
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
    }


    // TODO Paso 2: comprobar el permiso de notificaciones.
    // TODO Pasos 3 y 4: publicar el aviso y abrir la app al tocarlo.
    @Suppress("UNUSED_PARAMETER")
    fun mostrar(contexto: Context, aviso: Aviso): Boolean = false
}
