package com.intel.NLPproject

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

object TokenManager {
    var kakaoAccessToken: String? = null
    var naverAccessToken: String? = null

    fun getCurrentUserKey(): String? {
        // 우선 네이버 토큰(안정적인 uid)이 있으면 반환, 없으면 카카오 uid 반환
        TokenManager.naverAccessToken?.let { return it }
        TokenManager.kakaoAccessToken?.let { return it }
        return null
    }
    fun getCurrentUserId(context: Context): String? {
        // FirebaseAuth를 사용하는 경우
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) return firebaseUser.uid

        // 소셜 로그인인 경우 TokenManager에 저장된 stable UID를 사용합니다.
        return TokenManager.getCurrentUserKey()
    }

}