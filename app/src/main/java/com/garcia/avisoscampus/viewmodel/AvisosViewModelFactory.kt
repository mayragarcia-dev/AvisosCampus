package com.garcia.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.garcia.avisoscampus.data.AvisosRepository

class AvisosViewModelFactory(private val repo: AvisosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return requireNotNull(modelClass.cast(AvisosViewModel(repo)))
    }
}
