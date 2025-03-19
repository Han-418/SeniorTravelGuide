package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.intel.NLPproject.firebase.UserInfoDatabase
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
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    LaunchedEffect(Unit) {
        // 앱 시작 시 SharedPreferences에서 stable UID 복원
        TokenManager.kakaoAccessToken = loadStableUid(context, "kakao_stable_uid")
        TokenManager.naverAccessToken = loadStableUid(context, "naver_stable_uid")

        val firebaseUser = AuthManager.getCurrentUser()
        if (firebaseUser != null) {
            UserInfoDatabase().getUserInfo(firebaseUser.uid) { userInfo ->
                if (userInfo != null) {
                    Toast.makeText(context, "자동 로그인 완료", Toast.LENGTH_SHORT).show()
                    navController.navigate("first") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
            return@LaunchedEffect
        }

        val isKakaoLoggedIn = checkKakaoToken()
        val isNaverLoggedIn = checkNaverToken()

        if (isKakaoLoggedIn || isNaverLoggedIn) {
            val currentUserKey = TokenManager.getCurrentUserKey()
            if (currentUserKey != null) {
                UserInfoDatabase().getUserInfo(currentUserKey) { userInfo ->
                    if (userInfo != null) {
                        // 메시지는 stable UID의 접두어를 확인하여 결정
                        when {
                            currentUserKey.startsWith("kakao_") -> Toast.makeText(
                                context,
                                "카카오로 로그인합니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            currentUserKey.startsWith("naver_") -> Toast.makeText(
                                context,
                                "네이버로 로그인합니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            else -> Toast.makeText(context, "소셜 로그인", Toast.LENGTH_SHORT).show()
                        }
                        navController.navigate("first") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
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
        Text(
            text = "앱 로딩 중...",
            fontSize = 30.sp,
            fontFamily = myFontFamily,
            modifier = Modifier.offset(y = (-10).dp)
        )
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