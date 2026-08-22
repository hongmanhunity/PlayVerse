package com.example.playverse.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.presentation.component.auth.AuthButton
import com.example.playverse.presentation.component.auth.AuthHeader
import com.example.playverse.presentation.component.auth.AuthTextField
import com.example.playverse.presentation.component.auth.GoogleAuthButton
import com.example.playverse.presentation.state.AuthUiState

/**
 * SCREEN: RegisterScreen (Đơn giản - Tích hợp Nút Google Auth)
 * VỊ TRÍ: presentation/screen/RegisterScreen.kt
 */
import com.example.playverse.presentation.component.auth.rememberGoogleSignInLauncher

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: AuthUiState = AuthUiState.Idle,
    onBackClick: () -> Unit = {},
    onRegisterSubmit: (name: String, email: String, pass: String) -> Unit = { _, _, _ -> },
    onGoogleLoginSubmit: (email: String, name: String?, avatar: String?) -> Unit = { _, _, _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf("") }

    val launchGoogleSignIn = rememberGoogleSignInLauncher { gEmail, gName, gAvatar, _ ->
        onGoogleLoginSubmit(gEmail, gName, gAvatar)
    }

    val brandBlue = Color(0xFF0066FF)
    val secondaryText = Color(0xFF64748B)

    val isLoading = uiState is AuthUiState.Loading
    val apiError = (uiState as? AuthUiState.Error)?.errorMessage
    val displayError = if (localError.isNotEmpty()) localError else apiError

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            AuthHeader(
                title = "Đăng Ký",
                subtitle = "Tạo tài khoản mới để bắt đầu sử dụng",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (!displayError.isNullOrEmpty()) {
                Text(
                    text = displayError,
                    color = Color(0xFFEF4444),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            AuthTextField(
                value = fullName,
                onValueChange = { fullName = it; localError = "" },
                label = "Họ và tên",
                leadingIcon = Icons.Outlined.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = email,
                onValueChange = { email = it; localError = "" },
                label = "Email",
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = password,
                onValueChange = { password = it; localError = "" },
                label = "Mật khẩu",
                leadingIcon = Icons.Outlined.Lock,
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = "Toggle Password",
                            tint = secondaryText
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; localError = "" },
                label = "Xác nhận mật khẩu",
                leadingIcon = Icons.Outlined.Lock,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = "Đăng Ký",
                isLoading = isLoading,
                onClick = {
                    when {
                        fullName.isBlank() -> {
                            localError = "Vui lòng nhập họ và tên!"
                        }
                        email.isBlank() -> {
                            localError = "Vui lòng nhập địa chỉ email!"
                        }
                        password.length < 6 -> {
                            localError = "Mật khẩu phải có ít nhất 6 ký tự!"
                        }
                        password != confirmPassword -> {
                            localError = "Mật khẩu xác nhận không trùng khớp!"
                        }
                        else -> {
                            onRegisterSubmit(fullName, email, password)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Divider + Google Register
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
                Text(
                    text = "Hoặc",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            GoogleAuthButton(
                text = "Đăng ký với Google",
                onClick = { launchGoogleSignIn() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Đã có tài khoản? ",
                    fontSize = 14.sp,
                    color = secondaryText
                )
                Text(
                    text = "Đăng nhập ngay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = brandBlue,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
