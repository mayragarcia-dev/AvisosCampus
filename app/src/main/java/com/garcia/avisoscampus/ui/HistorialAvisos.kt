package com.garcia.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.garcia.avisoscampus.model.Aviso
import com.garcia.avisoscampus.model.Origen

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    TarjetaAvisos("Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía")
        } else {
            avisos.take(20).forEach { aviso -> FilaAviso(aviso) }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    val colorOrigen = when (aviso.origen) {
        Origen.BOTON -> MaterialTheme.colorScheme.primary
        Origen.RECORDATORIO -> Color(0xFF795100)
        Origen.MODO_AVION -> Color(0xFF5A408A)
        Origen.CARGADOR -> Color(0xFF17643B)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = colorOrigen.copy(alpha = 0.15f),
                contentColor = colorOrigen
            ) {
                Text(
                    text = aviso.origen.texto,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(aviso.titulo, style = MaterialTheme.typography.labelLarge)
                Text(aviso.texto, style = MaterialTheme.typography.bodySmall)
            }
            Text(aviso.hora, style = MaterialTheme.typography.labelSmall)
        }
    }
}
