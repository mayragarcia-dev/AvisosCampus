package com.garcia.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.garcia.avisoscampus.model.PermisoUi
import com.garcia.avisoscampus.notificacion.Notificador
import com.garcia.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
        vm::permisoRespondido
    )

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }

        vm.eventos.collect { aviso ->
            @Suppress("UNUSED_VARIABLE")
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: avisar al ViewModel si no se mostró.
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { espacio ->
        Column(
            modifier = Modifier
                .padding(espacio)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaAvisos("Permiso de notificaciones") {
                // TODO Paso 8: mostrar el mensaje y el acceso a ajustes.
                EstadoPermiso(permiso)
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            vm.permisoRespondido(true)
                        }
                    },
                    enabled = permiso != PermisoUi.Concedido
                ) {
                    Text("Permitir notificaciones")
                }
            }

            TarjetaAvisos("Avisos") {
                Button(onClick = vm::avisarAhora) { Text("Mostrar aviso") }
            }

            TarjetaAvisos("Recordatorio") {
                // TODO Paso 5: conectar los botones y mostrar la cuenta regresiva.
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {}, enabled = false) { Text("Recordar en 10 s") }
                    OutlinedButton(onClick = {}, enabled = false) { Text("Cancelar") }
                }
            }

            TarjetaAvisos("Receptor de anuncios") {
                // TODO Paso 6: conectar el interruptor y gestionar el receptor.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Escuchar el modo avión", modifier = Modifier.weight(1f))
                    Switch(checked = false, onCheckedChange = {}, enabled = false)
                }
            }

            TarjetaAvisos("Historial") {
                // TODO Paso 7: mostrar el historial del ViewModel.
                Text("Aún no hay avisos")
            }
        }
    }
}

@Composable
private fun EstadoPermiso(permiso: PermisoUi) {
    val (etiqueta, color) = when (permiso) {
        PermisoUi.SinPedir -> "Sin pedir" to Color(0xFF495057)
        PermisoUi.Concedido -> "Permitido" to Color(0xFF17643B)
        PermisoUi.Denegado -> "Denegado" to Color(0xFFAC2626)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Estado:")
        Surface(shape = CircleShape, color = color.copy(alpha = 0.15f), contentColor = color) {
            Text(
                text = etiqueta,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun TarjetaAvisos(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}
