package com.example.myapplication.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, completa todos los campos") }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update { it.copy(errorMessage = "Correo electrónico inválido") }
            return
        }

        if (currentState.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres") }
            return
        }

        // Simulación de autenticación exitosa
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        // Simular retraso de red o validación asíncrona
        // En una app real usaríamos viewModelScope.launch
        _uiState.update { 
            it.copy(
                isLoading = false,
                isLoggedIn = true
            )
        }
    }

    fun logout() {
        _uiState.update {
            LoginUiState()
        }
    }
}
