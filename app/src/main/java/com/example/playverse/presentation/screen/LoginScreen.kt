package com.example.playverse.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playverse.presentation.component.auth.AuthButton
import com.example.playverse.presentation.component.auth.AuthHeader
import com.example.playverse.presentation.component.auth.AuthTextField
import com.example.playverse.presentation.state.AuthUiState

/**
 * SCREEN: LoginScreen (Màn hình Đăng Nhập tích hợp API)
 * VỊ TRÍ: presentation/screen/LoginScreen.kt
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: AuthUiState = AuthUiState.Idle,
    onBackClick: () -> Unit = {},
    onLoginSubmit: (email: String, pass: String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val backgroundColor = Color(0xFFF4F7FC)
    val brandBlue = Color(0xFF0066FF)
    val secondaryTextColor = Color(0xFF8A94A6)

    val isLoading = uiState is AuthUiState.Loading
    val errorMessage = (uiState as? AuthUiState.Error)?.errorMessage

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            AuthHeader(
                title = "Đăng Nhập 🎮",
                subtitle = "Sẵn sàng chinh phục thế giới game PlayVerse",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    if (!errorMessage.isNullOrEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFEF4444),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )
                    }

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Game thủ",
                        leadingIcon = Icons.Outlined.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Mật khẩu",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                    contentDescription = "Toggle Password",
                                    tint = secondaryTextColor
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Quên mật khẩu?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = brandBlue,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = brandBlue)
                        }
                    } else {
                        AuthButton(
                            text = "ĐĂNG NHẬP NGAY",
                            onClick = { onLoginSubmit(email, password) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Chưa có tài khoản? ",
                    fontSize = 14.sp,
                    color = secondaryTextColor
                )
                Text(
                    text = "Tạo tài khoản mới",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = brandBlue,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}
