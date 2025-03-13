package com.intel.NLPproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val user = AuthManager.getCurrentUser()

    LaunchedEffect(Unit) {
        // 1) 카카오 토큰 유효성 검사
        val isKakaoLoggedIn = checkKakaoToken()

        // 2) 네이버 토큰 유효성 검사
        val isNaverLoggedIn = checkNaverToken()

        if (isKakaoLoggedIn || isNaverLoggedIn) {
            // 이미 로그인 상태면 메인 화면으로
            navController.navigate("main") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // 로그인이 안 되어 있으면 로그인 화면으로
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }

        when {
            user == null -> {
                // 로그인하지 않은 경우 로그인 화면으로 이동
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            user.isAnonymous -> {
                // 익명 로그인한 경우 자동 로그아웃 후 로그인 화면으로 이동
                AuthManager.logout()
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            else -> {
                // 정상 로그인한 경우 메인 화면으로 이동
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
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