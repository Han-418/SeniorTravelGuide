package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLoginState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// AuthManager 역할을 하는 간단한 객체
object AuthManager {
    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        // 먼저 FirebaseAuth를 통한 전화번호 로그인(또는 기타 방식) 확인
        val firebaseUser = AuthManager.getCurrentUser()
        if (firebaseUser != null) {
            Toast.makeText(context, "전화번호로 로그인합니다.", Toast.LENGTH_SHORT).show()
            navController.navigate("first") {
                popUpTo("splash") { inclusive = true }
            }
            return@LaunchedEffect
        }

        // Firebase 사용자가 없으면 카카오와 네이버 토큰을 검사
        val isKakaoLoggedIn = checkKakaoToken()
        val isNaverLoggedIn = checkNaverToken()

        if (isKakaoLoggedIn || isNaverLoggedIn) {
            if (isKakaoLoggedIn) {
                Toast.makeText(context, "카카오로 로그인합니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "네이버로 로그인합니다.", Toast.LENGTH_SHORT).show()
            }
            navController.navigate("first") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "앱 로딩 중...", fontSize = 20.sp)
    }
}

suspend fun checkKakaoToken(): Boolean {
    return suspendCoroutine { continuation ->
        // 콜백 기반의 me(...) 호출
        UserApiClient.instance.me { user: User?, error: Throwable? ->
            if (error != null) {
                // 토큰이 없거나 만료되었을 경우 error 발생
                continuation.resume(false)
            } else {
                // user != null 이면 로그인 유지 중
                continuation.resume(user != null)
            }
        }
    }
}

fun checkNaverToken(): Boolean {
    val state = NaverIdLoginSDK.getState()
    // state가 OK 이고, getAccessToken()이 null이 아니면 로그인 유지 상태
    return (state == NidOAuthLoginState.OK && NaverIdLoginSDK.getAccessToken() != null)
}