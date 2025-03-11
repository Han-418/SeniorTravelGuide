package com.intel.NLPproject.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

// 인증 코드 전송 함수
fun sendVerificationCode(
    phoneNumber: String,
    context: Context,
    auth: FirebaseAuth,
    verificationId: MutableState<String>,
    isCodeSent: MutableState<Boolean>
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(context as Activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("PhoneAuth", "자동 로그인 성공!")
                        }
                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("PhoneAuth", "인증 실패: ${e.message}")
            }

            override fun onCodeSent(verificationIdReceived: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationId.value = verificationIdReceived
                isCodeSent.value = true
                Log.d("PhoneAuth", "인증 코드 전송됨!")
            }
        })
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}

// 인증 코드 검증 및 로그인 함수
fun verifyCode(
    code: String,
    verificationId: String,
    auth: FirebaseAuth,
    navController: NavController,
    onSuccess: () -> Unit = {}
) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PhoneAuth", "로그인 성공!")
                onSuccess()
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Log.e("PhoneAuth", "로그인 실패", task.exception)
            }
        }
}