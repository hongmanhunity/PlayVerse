package com.example.playverse.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playverse.domain.model.User
import com.example.playverse.domain.repository.AuthRepository
import com.example.playverse.presentation.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(authRepository.getSavedUser())
    val currentUser = _currentUser.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Vui lòng nhập đầy đủ Email và Mật khẩu!")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(email = email.trim(), password = password)
            result.onSuccess { user ->
                _currentUser.value = user
                _uiState.value = AuthUiState.Success(user, "Đăng nhập thành công!")
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Đăng nhập thất bại!")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Vui lòng nhập đầy đủ thông tin!")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.register(name = name.trim(), email = email.trim(), password = password)
            result.onSuccess { user ->
                _currentUser.value = user
                _uiState.value = AuthUiState.Success(user, "Tạo tài khoản thành công!")
            }.onFailure { exception ->
                _uiState.value = AuthUiState.Error(exception.message ?: "Đăng ký thất bại!")
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _uiState.value = AuthUiState.Idle
    }

    fun resetUiState() {
        _uiState.value = AuthUiState.Idle
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
