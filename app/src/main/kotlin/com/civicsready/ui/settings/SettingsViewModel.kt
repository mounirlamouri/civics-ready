package com.civicsready.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civicsready.data.repository.CivicsRepository
import com.civicsready.domain.model.FederalOfficials
import com.civicsready.domain.model.Officials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val zipInput: String = "",
    val officials: Officials = Officials(),
    val is6520Mode: Boolean = false,
    val lookupError: String? = null,
    /** Non-null when the zip resolved to state-level info only (district not found). */
    val lookupNotice: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CivicsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.officialsFlow,
                repository.is6520Mode
            ) { officials, is6520 ->
                _uiState.value = _uiState.value.copy(
                    zipInput   = officials.zipCode,
                    officials  = officials,
                    is6520Mode = is6520
                )
            }.collect {}
        }
    }

    fun onZipChanged(value: String) {
        if (value.length <= 5 && value.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(zipInput = value, lookupError = null)
        }
    }

    fun lookupZip() {
        val zip = _uiState.value.zipInput.trim()
        if (zip.length != 5) {
            _uiState.value = _uiState.value.copy(lookupError = "Please enter a 5-digit zip code.", lookupNotice = null)
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, lookupError = null, lookupNotice = null)
        viewModelScope.launch {
            val result = repository.resolveZipCode(zip)
            when {
                result == null -> _uiState.value = _uiState.value.copy(
                    isLoading   = false,
                    lookupError = "Zip code not found. Please verify your zip and try again."
                )
                !result.isExactMatch -> _uiState.value = _uiState.value.copy(
                    isLoading     = false,
                    lookupNotice  = "State officials shown. To find your U.S. Representative, visit house.gov/zip4"
                )
                else -> _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggle6520Mode(enabled: Boolean) {
        viewModelScope.launch {
            repository.set6520Mode(enabled)
        }
    }
}
