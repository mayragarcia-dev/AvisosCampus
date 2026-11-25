package com.garcia.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.avisoscampus.data.AvisosRepository
import com.garcia.avisoscampus.model.Aviso
import com.garcia.avisoscampus.model.Origen
import com.garcia.avisoscampus.model.PermisoUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
    val avisos: StateFlow<List<Aviso>> = repo.avisos

    // La pantalla recibe el evento y solicita la notificación.
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private var trabajo: Job? = null
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar(
                titulo = "Tarea lista",
                texto = "Tu entrega fue registrada",
                origen = Origen.BOTON
            )
            _eventos.emit(aviso)
        }
    }

    fun recordarEn(segundos: Int = 10) {
        if (segundos <= 0 || trabajo?.isCompleted == false) return

        trabajo = viewModelScope.launch {
            try {
                for (restantes in segundos downTo 1) {
                    _faltan.value = restantes
                    delay(1_000L)
                }

                val aviso = repo.registrar(
                    titulo = "Recordatorio",
                    texto = "Revisa tu laboratorio pendiente",
                    origen = Origen.RECORDATORIO
                )
                _eventos.emit(aviso)
            } finally {
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    // TODO Paso 6: escuchandoAvion, cambiarEscucha(valor) y alCambiarModoAvion(activo)
    // TODO Paso 8: fun noSePudoMostrar()
}
