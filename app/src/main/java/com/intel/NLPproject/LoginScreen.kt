package com.intel.NLPproject

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.intel.NLPproject.auth.sendVerificationCode
import com.intel.NLPproject.auth.verifyCode
import com.intel.NLPproject.firebase.UserInfoDatabase
import com.intel.NLPproject.models.UserInfo

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val phoneNumber = remember { mutableStateOf("") }
    val verificationId = remember { mutableStateOf("") }
    val otpCode = remember { mutableStateOf("") }
    val isCodeSent = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val birthDate = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 큰 타이틀
        Text(
            text = "회원가입",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 이름 입력 칸 (플레이스홀더 사용)
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            placeholder = {
                Text(
                    text = "이름",
                    fontSize = 28.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 28.sp,
                textAlign = TextAlign.Start
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 생년월일 입력 칸 (플레이스홀더 사용)
        OutlinedTextField(
            value = birthDate.value,
            onValueChange = { birthDate.value = it },
            placeholder = {
                // 길이가 길어질 경우 줄바꿈이나 Ellipsis를 적용할 수 있습니다.
                Text(
                    text = "생년월일 (YYYYMMDD)",
                    fontSize = 28.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 28.sp,
                textAlign = TextAlign.Start
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 성별 선택
        GenderSelection(gender)

        // 전화번호 입력 칸 (플레이스홀더 사용)
        PhoneNumberInput(phoneNumber)

        Spacer(modifier = Modifier.height(16.dp))

        // 인증 코드 받기 버튼
        Button(onClick = {
            val formattedPhoneNumber = formatPhoneNumber(phoneNumber.value)
            if (formattedPhoneNumber.isNotEmpty()) {
                sendVerificationCode(
                    formattedPhoneNumber,
                    context, auth, verificationId, isCodeSent
                )
            } else {
                Log.e("PhoneAuth", "잘못된 전화번호 형식")
            }
        }) {
            Text("인증 코드 받기", fontSize = 28.sp)
        }

        // 인증 코드 입력 칸 & 로그인 버튼 (코드 전송된 경우에만 표시)
        if (isCodeSent.value) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = otpCode.value,
                onValueChange = { otpCode.value = it },
                placeholder = {
                    Text(
                        text = "인증 코드 입력",
                        fontSize = 28.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Start
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(horizontal = 16.dp)
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
                        // 전화번호는 입력값을 받아서 +8210이 붙은 형식으로 변환
                        val formattedPhone = formatPhoneNumber(phoneNumber.value)

                        // 유저 정보를 담은 data class 인스턴스를 생성
                        val userInfo = UserInfo(
                            uid = user.uid,
                            name = name.value,
                            birthDate = birthDate.value,
                            gender = gender.value,
                            phoneNumber = formattedPhone
                        )

                        // 유저 정보를 DB에 저장
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
                Text("로그인", fontSize = 28.sp)
            }
        }
    }
}

@Composable
fun PhoneNumberInput(phoneNumber: MutableState<String>) {
    OutlinedTextField(
        value = phoneNumber.value,
        onValueChange = { newValue ->
            // 숫자만 입력하도록 필터링
            val digits = newValue.filter { it.isDigit() }
            // 최대 8자리까지만 입력 가능
            val limitedDigits = if (digits.length > 8) digits.take(8) else digits
            phoneNumber.value = limitedDigits
        },
        placeholder = {
            // 두 줄로 나눠도 되고, Ellipsis로 처리할 수도 있습니다.
            Text(
                text = "전화번호 뒷자리 8개 입력(숫자만)",
                fontSize = 28.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        textStyle = LocalTextStyle.current.copy(
            fontSize = 28.sp,
            textAlign = TextAlign.Start
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp)
    )
}

fun formatPhoneNumber(phoneNumber: String): String {
    // 숫자만 추출
    val cleanedNumber = phoneNumber.filter { it.isDigit() }
    return if (cleanedNumber.length == 8) {
        "+8210$cleanedNumber"
    } else {
        ""
    }
}

@Composable
fun GenderSelection(gender: MutableState<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "성별 선택", fontSize = 28.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender.value == "남성",
                    onClick = { gender.value = "남성" }
                )
                Text(text = "남성", modifier = Modifier.padding(start = 4.dp), fontSize = 28.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender.value == "여성",
                    onClick = { gender.value = "여성" }
                )
                Text(text = "여성", modifier = Modifier.padding(start = 4.dp), fontSize = 28.sp)
            }
        }
    }
}

