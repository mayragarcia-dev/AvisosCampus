package com.garcia.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.garcia.avisoscampus.data.AvisosRepositoryMemoria
import com.garcia.avisoscampus.ui.PantallaAvisos
import com.garcia.avisoscampus.ui.theme.AvisosCampusTheme
import com.garcia.avisoscampus.viewmodel.AvisosViewModel
import com.garcia.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    private val factory = AvisosViewModelFactory(AvisosRepositoryMemoria())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                val vm: AvisosViewModel = viewModel(factory = factory)
                PantallaAvisos(vm)
            }
        }
    }
}
