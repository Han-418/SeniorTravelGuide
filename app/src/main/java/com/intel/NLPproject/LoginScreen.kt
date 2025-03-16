package com.intel.NLPproject

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
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
        Text("여행한잔", fontSize = 50.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .background(color = Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("app icon")
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                navController.navigate("phoneLogin")
            },
            modifier = Modifier
                .height(50.dp)
                .width(300.dp),
            shape = RectangleShape
        ) {
            Text("전화번호로 로그인")
        }
        Spacer(modifier = Modifier.height(8.dp))
        KakaoLoginButton(
            onSuccess = onKakaoLoginSuccess,
            onError = onKakaoLoginError
        )
        Spacer(modifier = Modifier.height(8.dp))
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
    // 전화번호 오류 상태
    val showPhoneError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("전화번호 인증으로 로그인", fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))
        // 전화번호 입력 필드
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { newValue ->
                // 입력값에서 숫자만 추출, 최대 11자리로 제한
                val digits = newValue.filter { it.isDigit() }
                phoneNumber.value = digits.take(11)
                // 입력 도중에는 오류 상태 초기화
                showPhoneError.value = false
            },
            label = { Text("전화번호 (예: 010-3333-3333)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            singleLine = true,
            visualTransformation = PhoneNumberVisualTransformation(),
            isError = showPhoneError.value,
            trailingIcon = {
                if (showPhoneError.value) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color.Red
                    )
                }
            }
        )
        if (showPhoneError.value) {
            Text(
                text = "올바른 전화번호 형식이 아닙니다.",
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 로그인 버튼 클릭 인증 코드 발송
        Button(onClick = {
            if (phoneNumber.value.length != 11 || !phoneNumber.value.startsWith("010")) {
                showPhoneError.value = true
            } else {
                val formattedPhone = formatPhoneNumber(phoneNumber.value)
                if (formattedPhone.isNotEmpty()) {
                    sendVerificationCode(formattedPhone, context, auth, verificationId, isCodeSent)
                } else {
                    Log.e("PhoneAuth", "잘못된 전화번호 형식")
                    showPhoneError.value = true
                }
            }
        }) {
            Text("인증코드 받기")
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
    // 모든 숫자만 남김 (예: "010-3333-3333" → "01033333333")
    val cleanedNumber = phoneNumber.filter { it.isDigit() }
    return if (cleanedNumber.length == 11 && cleanedNumber.startsWith("010")) {
        // "01033333333" → "+82" + "1033333333" → "+821033333333"
        "+82" + cleanedNumber.substring(1)
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
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        onError(error)
                    } else if (token != null) {
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                onError(error)
                            } else if (user != null) {
                                // 안정적인 고유 식별자인 user.id 사용하여 UID 생성
                                val stableUid = "kakao_${user.id}"
                                // 실제 사용자 닉네임
                                val nickname = user.kakaoAccount?.profile?.nickname ?: "Kakao User"
                                val userInfo = UserInfo(
                                    uid = stableUid,
                                    name = "카카오_$nickname",
                                    birthDate = "",
                                    gender = "",
                                    phoneNumber = ""
                                )
                                // 카카오 로그인 성공 시, 다른 소셜(네이버) 토큰 클리어
                                TokenManager.naverAccessToken = null
                                saveStableUid(context, "naver_stable_uid", "")
                                // 그리고 카카오 stable UID 저장
                                TokenManager.kakaoAccessToken = stableUid
                                saveStableUid(context, "kakao_stable_uid", stableUid)
                                UserInfoDatabase().saveUserInfo(userInfo) { success ->
                                    if (success) {
                                        onSuccess(token.accessToken)
                                    } else {
                                        onError(Throwable("사용자 정보 저장 실패"))
                                    }
                                }
                            }
                        }
                    }
                }
            }
    )
}

@Composable
fun NaverLoginButton(
    navController: NavHostController,
    onLoginSuccess: (accessToken: String) -> Unit = {}
) {
    val context = LocalContext.current
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
                        val accessToken = NaverIdLoginSDK.getAccessToken()
                        if (accessToken != null) {
                            // 네이버 사용자 정보 API를 호출하여 ID와 이름 가져오기
                            fetchNaverUserInfo(accessToken) { id, name ->
                                if (id != null) {
                                    val stableUid = "naver_$id"
                                    val userInfo = UserInfo(
                                        uid = stableUid,
                                        name = "네이버_${name ?: "Naver User"}",
                                        birthDate = "",
                                        gender = "",
                                        phoneNumber = ""
                                    )
                                    // 네이버 로그인 시, 카카오 토큰 클리어
                                    TokenManager.kakaoAccessToken = null
                                    saveStableUid(context, "kakao_stable_uid", "")
                                    // 그리고 네이버 stable UID 저장
                                    TokenManager.naverAccessToken = stableUid
                                    saveStableUid(context, "naver_stable_uid", stableUid)
                                    UserInfoDatabase().saveUserInfo(userInfo) { success ->
                                        if (success) {
                                            onLoginSuccess(accessToken)
                                            navController.navigate("first") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "사용자 정보 저장 실패",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "네이버 사용자 정보를 불러오지 못했습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "네이버 로그인 성공했지만 액세스 토큰이 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Toast.makeText(
                            context,
                            "네이버 로그인 실패: errorCode:$errorCode, errorDesc:$errorDescription",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }
                }
                NaverIdLoginSDK.authenticate(activity, oauthLoginCallback)
            }
    )
}

fun fetchNaverUserInfo(accessToken: String, onResult: (id: String?, name: String?) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://openapi.naver.com/v1/nid/me")
        .addHeader("Authorization", "Bearer $accessToken")
        .build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResult(null, null)
        }

        override fun onResponse(call: Call, response: Response) {
            val bodyString = response.body?.string()
            if (bodyString != null) {
                val json = JSONObject(bodyString)
                // 예시 응답: { "resultcode": "00", "message": "success", "response": { "id": "12345", "name": "실제 이름", ... } }
                val responseObj = json.optJSONObject("response")
                val id = responseObj?.optString("id")
                val name = responseObj?.optString("name") ?: responseObj?.optString("nickname")
                ?: "Naver User"
                onResult(id, name)
            } else {
                onResult(null, null)
            }
        }
    })
}
