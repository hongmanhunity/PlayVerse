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

const val GOOGLE_WEB_CLIENT_ID = "703356444680-c718sp594779pgojnvd4s698acbmqs4r.apps.googleusercontent.com"

@Composable
fun rememberGoogleSignInLauncher(
    webClientId: String? = GOOGLE_WEB_CLIENT_ID.ifEmpty { null },
    onGoogleSignInSuccess: (email: String, name: String?, avatar: String?, idToken: String?, googleId: String?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val googleSignInOptions = remember(webClientId) {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()

        if (!webClientId.isNullOrEmpty()) {
            builder.requestIdToken(webClientId)
        }

        builder.build()
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
                val realIdToken = account.idToken?.takeIf { it.isNotBlank() }
                val realGoogleId = account.id?.takeIf { it.isNotBlank() } ?: "google_${account.email}"
                onGoogleSignInSuccess(
                    account.email!!,
                    account.displayName,
                    account.photoUrl?.toString(),
                    realIdToken,
                    realGoogleId
                )
            } else {
                Toast.makeText(context, "Không thể lấy thông tin tài khoản Google", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            val errorMsg = when (e.statusCode) {
                12501 -> "Đã hủy đăng nhập Google"
                12500 -> "Vui lòng kiểm tra Google Play Services trên thiết bị (hoặc SHA-1 fingerprint)"
                10 -> "Lỗi Cấu Hình Google Sign-In (Thiếu SHA-1 hoặc Web Client ID trong Google Cloud)"
                else -> "Mã kết quả Google Sign-In: ${e.statusCode}"
            }
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    return {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }
}
