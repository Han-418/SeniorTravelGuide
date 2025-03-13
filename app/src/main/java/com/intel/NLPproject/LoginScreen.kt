package com.intel.NLPproject

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.intel.NLPproject.auth.sendVerificationCode
import com.intel.NLPproject.auth.verifyCode
import com.intel.NLPproject.firebase.UserInfoDatabase
import com.intel.NLPproject.models.UserInfo
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 카카오 로그인 성공/실패 처리를 위한 콜백
        val onKakaoLoginSuccess: (String) -> Unit = { accessToken ->
            // accessToken을 이용해 서버와 통신하거나, 다음 화면으로 이동하는 로직 작성
            Log.d("KakaoLogin", "카카오 로그인 성공! AccessToken: $accessToken")

            // 예: 로그인 성공 후 메인 화면으로 이동
            navController.navigate("first") {
                popUpTo("login") { inclusive = true }
            }
        }
        val onKakaoLoginError: (Throwable) -> Unit = { error ->
            Log.e("KakaoLogin", "카카오 로그인 실패: ${error.message}")
            // 에러 메시지 표시 등 처리
            Toast.makeText(context, "카카오 로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
        }

        OutlinedButton(
            onClick = {
                navController.navigate("phoneLogin")
            },
            modifier = Modifier.height(50.dp)
                .width(300.dp),
            shape = RectangleShape
        ) {
            Text("전화번호로 로그인")
        }
        KakaoLoginButton(
            onSuccess = onKakaoLoginSuccess,
            onError = onKakaoLoginError
        )
        // 네이버 로그인 버튼
        NaverLoginButton(navController = navController) { accessToken ->
            // 로그인 성공 후 추가 작업이 필요하면 이곳에서 처리
            // 예: Log.d("NaverLogin", "AccessToken: $accessToken")
        }
    }
}

@Composable
fun PhoneLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val phoneNumber = remember { mutableStateOf("") }
    val verificationId = remember { mutableStateOf("") }
    val otpCode = remember { mutableStateOf("") }
    val isCodeSent = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("전화번호 인증으로 로그인", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))
        // 전화번호 입력 필드 (숫자만, 최대 8자리)
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { newValue ->
                val digits = newValue.filter { it.isDigit() }
                phoneNumber.value = if (digits.length > 8) digits.take(8) else digits
            },
            label = { Text("전화번호 뒷자리 8개 입력(숫자만)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 로그인 버튼 클릭 인증 코드 발송
        Button(onClick = {
            val formattedPhone = formatPhoneNumber(phoneNumber.value)
            if (formattedPhone.isNotEmpty()) {
                // 전화번호가 데이터베이스에 있든 없든 항상 OTP 인증 코드 발송
                sendVerificationCode(formattedPhone, context, auth, verificationId, isCodeSent)
            } else {
                Log.e("PhoneAuth", "잘못된 전화번호 형식")
            }
        }) {
            Text("로그인")
        }
        // OTP 입력 및 인증 코드 확인 UI (OTP 발송 후 표시)
        if (isCodeSent.value) {
            OutlinedTextField(
                value = otpCode.value,
                onValueChange = { otpCode.value = it },
                label = { Text("인증 코드 입력") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                verifyCode(
                    otpCode.value,
                    verificationId.value,
                    auth,
                    navController
                ) {
                    auth.currentUser?.let { user ->
                        // 신규 사용자일 경우 최소한 전화번호 정보를 저장
                        val newUserInfo = UserInfo(
                            uid = user.uid,
                            name = "",      // 추가 정보가 필요하면 추후 입력받거나 별도 화면 구현
                            birthDate = "",
                            gender = "",
                            phoneNumber = formatPhoneNumber(phoneNumber.value)
                        )
                        UserInfoDatabase().saveUserInfo(newUserInfo) { success ->
                            if (success) {
                                Log.d("UserInfo", "유저 정보 저장 성공")
                                navController.navigate("main")
                            } else {
                                Log.e("UserInfo", "유저 정보 저장 실패")
                            }
                        }
                    }
                }
            }) {
                Text("인증 코드 확인 및 로그인")
            }
        }
    }
}

// 입력받은 전화번호를 "+8210XXXXXXX" 형식으로 포맷팅하는 함수
fun formatPhoneNumber(phoneNumber: String): String {
    val cleanedNumber = phoneNumber.filter { it.isDigit() }
    return if (cleanedNumber.length == 8) {
        "+8210$cleanedNumber"
    } else {
        ""
    }
}

@Composable
fun KakaoLoginButton(onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
    val context = LocalContext.current

    Image(
        painter = painterResource(R.drawable.kakao_login_medium_wide),
        contentDescription = "kakao login",
        modifier = Modifier
            .width(300.dp)
            .height(50.dp)
            .clickable {
                // 카카오톡 앱으로 로그인 시도
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        onError(error)
                    } else if (token != null) {
                        // 로그인 성공, token.accessToken 사용 가능
                        onSuccess(token.accessToken)
                    }
                }
            }
    )
}

@Composable
fun NaverLoginButton(navController: NavHostController, onLoginSuccess: (accessToken: String) -> Unit = {}) {
    val context = LocalContext.current
    // Activity가 아닌 경우 로그인 플로우를 진행할 수 없으므로 반환합니다.
    val activity = context as? Activity ?: return

    Image(
        painter = painterResource(R.drawable.naverlogin),
        contentDescription = "naver login",
        modifier = Modifier
            .height(50.dp)
            .width(300.dp)
            .clickable {
                val oauthLoginCallback = object : OAuthLoginCallback {
                    override fun onSuccess() {
                        // 로그인 성공 시, 액세스 토큰을 가져옵니다.
                        val accessToken = NaverIdLoginSDK.getAccessToken()
                        if (accessToken != null) {
                            // 성공 시, 추가 작업 (예: 서버에 전달, 상태 업데이트 등)을 실행
                            onLoginSuccess(accessToken)
                            // 예: 로그인 성공 후 메인 화면으로 이동
                            navController.navigate("first") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "네이버 로그인 성공했지만 액세스 토큰이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Toast.makeText(context, "네이버 로그인 실패: errorCode:$errorCode, errorDesc:$errorDescription", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(errorCode: Int, message: String) {
                        // onError에서 onFailure를 호출해 동일하게 처리합니다.
                        onFailure(errorCode, message)
                    }
                }
                // 네이버 로그인 플로우 시작 (Activity를 파라미터로 전달)
                NaverIdLoginSDK.authenticate(activity, oauthLoginCallback)
            }
    )
}