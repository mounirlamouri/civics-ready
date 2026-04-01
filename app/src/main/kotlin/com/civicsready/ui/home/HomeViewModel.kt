package com.civicsready.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.Officials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val officials: Officials = Officials(),
    val is6520Mode: Boolean = false,
    val isOrderedMode: Boolean = false,
    val totalAttempted: Int = 0,
    val overallAccuracy: Float = 0f
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CivicsRepository
) : ViewModel() {

    val uiState = combine(
        repository.officialsFlow,
        repository.is6520Mode,
        repository.isOrderedMode,
        repository.allProgressFlow
    ) { officials, is6520, isOrdered, progress ->
        val attempted = progress.values.filter { it.totalAttempts > 0 }
        val accuracy  = if (attempted.isEmpty()) 0f
                        else attempted.map { it.successRate }.average().toFloat()
        HomeUiState(
            officials       = officials,
            is6520Mode      = is6520,
            isOrderedMode   = isOrdered,
            totalAttempted  = attempted.size,
            overallAccuracy = accuracy
        )
    }

    fun toggleOrderedMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setOrderedMode(enabled)
        }
    }
}
