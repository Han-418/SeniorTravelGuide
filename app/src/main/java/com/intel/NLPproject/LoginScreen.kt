package com.intel.NLPproject

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
fun LoginScreen(
    onKakaoLoginSuccess: (String) -> Unit,
    onKakaoLoginError: (Throwable) -> Unit,
    onLoginSuccess: (accessToken: String) -> Unit = {},
    navController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.loginscr),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.phonelogin2),
                contentDescription = "phone login",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                    navController.navigate("phoneLogin")
                }
            )
            Image(
                painter = painterResource(R.drawable.kakaologin),
                contentDescription = "kakao login",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            onKakaoLoginError(error)
                        } else if (token != null) {
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    onKakaoLoginError(error)
                                } else if (user != null) {
                                    // 안정적인 고유 식별자인 user.id 사용하여 UID 생성
                                    val stableUid = "kakao_${user.id}"
                                    // 실제 사용자 닉네임
                                    val nickname =
                                        user.kakaoAccount?.profile?.nickname ?: "Kakao User"
                                    val userInfo = UserInfo(
                                        uid = stableUid,
                                        name = "카카오_$nickname",
                                        birthDate = "",
                                        gender = "",
                                        phoneNumber = ""
                                    )
                                    // 카카오 로그인 성공 시, 다른 소셜(네이버) 토큰 클리어
                                    TokenManager.naverAccessToken = null
                                    saveStableUid(context, "naver_stable_uid", null)
                                    // 그리고 카카오 stable UID 저장
                                    TokenManager.kakaoAccessToken = stableUid
                                    saveStableUid(context, "kakao_stable_uid", stableUid)
                                    UserInfoDatabase().saveUserInfo(userInfo) { success ->
                                        if (success) {
                                            onKakaoLoginSuccess(token.accessToken)
                                        } else {
                                            onKakaoLoginError(Throwable("사용자 정보 저장 실패"))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
            Image(
                painter = painterResource(R.drawable.naverlogin2),
                contentDescription = "naver login",
                modifier = Modifier
                    .padding(16.dp)
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
                                        saveStableUid(context, "kakao_stable_uid", null)
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
            Spacer(modifier = Modifier.height(32.dp))
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
    val name = remember { mutableStateOf("") }
    val birthDate = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }

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
                fontSize = 16.sp,
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

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
                        // 전화번호는 입력값(예: "3333-5678")을 받아서, 저장 시 +8210이 붙은 형식으로 변환
                        val formattedPhone =
                            formatPhoneNumber(phoneNumber.value) // 예: "3333-5678" -> "+821033335555"

                        // 유저 정보를 담은 data class 인스턴스를 생성 (UserInfo는 미리 정의된 모델 클래스)
                        val userInfo = UserInfo(
                            uid = user.uid,
                            name = name.value,
                            birthDate = birthDate.value,
                            gender = gender.value,
                            phoneNumber = formattedPhone
                        )

                        // 유저 정보를 데이터베이스에 저장
                        UserInfoDatabase().saveUserInfo(userInfo) { success ->
                            if (success) {
                                Log.d("UserInfo", "User info saved successfully")
                            } else {
                                Log.e("UserInfo", "Failed to save user info")
                            }
                        }
                    }
                }
            }) {
                Text("로그인")
            }
        }
    }
}

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