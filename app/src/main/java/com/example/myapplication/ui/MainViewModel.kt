package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AuditReportEntity
import com.example.myapplication.data.local.PoleEntity
import com.example.myapplication.data.repository.StreetlightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val repository: StreetlightRepository) : ViewModel() {

    val poles: StateFlow<List<PoleEntity>> = repository.getPoles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<AuditReportEntity>> = repository.getReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.syncPoles()
        }
    }

    fun refreshPoles() {
        viewModelScope.launch {
            repository.syncPoles()
        }
    }

    fun reportStatus(poleId: String, status: String) {
        viewModelScope.launch {
            val report = AuditReportEntity(
                id = UUID.randomUUID().toString(),
                poleId = poleId,
                status = status,
                timestamp = System.currentTimeMillis(),
                complaintId = "GL-${UUID.randomUUID().toString().take(6).uppercase()}"
            )
            repository.submitReport(report)
        }
    }
}

class MainViewModelFactory(private val repository: StreetlightRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
