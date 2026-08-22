package com.example.playverse.presentation.component.auth

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

/**
 * HELPER: rememberGoogleSignInLauncher
 * Gọi trực tiếp SDK Google Play Services chính thức để hiển thị bảng chọn tài khoản Google của Android System
 */
@Composable
fun rememberGoogleSignInLauncher(
    onGoogleSignInSuccess: (email: String, name: String?, avatar: String?, googleId: String?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()
    }

    val googleSignInClient = remember(context, googleSignInOptions) {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null && !account.email.isNullOrEmpty()) {
                onGoogleSignInSuccess(
                    account.email!!,
                    account.displayName,
                    account.photoUrl?.toString(),
                    account.id
                )
            } else {
                Toast.makeText(context, "Không thể lấy thông tin tài khoản Google", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            val errorMsg = when (e.statusCode) {
                12501 -> "Đã hủy đăng nhập Google"
                12500 -> "Vui lòng kiểm tra Google Play Services trên thiết bị"
                else -> "Mã kết quả Google Sign-In: ${e.statusCode}"
            }
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    return {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }
}
