package com.intel.NLPproject

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
        Text(text = "회원가입", fontSize = 20.sp)

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("이름") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = birthDate.value,
            onValueChange = { birthDate.value = it },
            label = { Text("생년월일 (YYYYMMDD)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // 성별 선택 Radio Button 사용
        GenderSelection(gender)
        PhoneNumberInput(phoneNumber)

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val formattedPhoneNumber = formatPhoneNumber(phoneNumber.value)
            if (formattedPhoneNumber.isNotEmpty()) {
                sendVerificationCode(
                    formattedPhoneNumber, // 변환된 전화번호 사용
                    context, auth, verificationId, isCodeSent
                )
            } else {
                Log.e("PhoneAuth", "잘못된 전화번호 형식")
            }
        }) {
            Text("인증 코드 받기")
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

@Composable
fun PhoneNumberInput(phoneNumber: MutableState<String>) {
    OutlinedTextField(
        value = phoneNumber.value,
        onValueChange = { newValue ->
            // 숫자만 입력하도록 필터링
            val digits = newValue.filter { it.isDigit() }
            // 최대 8자리까지만 입력 가능
            val limitedDigits = if (digits.length > 8) digits.take(8) else digits
            // 자동 포맷팅 없이 숫자만 저장
            phoneNumber.value = limitedDigits
        },
        label = { Text("전화번호 뒷자리 8개 입력(숫자만)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true
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
        Text(text = "성별 선택", fontSize = 20.sp)
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
                Text(text = "남성", modifier = Modifier.padding(start = 4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender.value == "여성",
                    onClick = { gender.value = "여성" }
                )
                Text(text = "여성", modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
