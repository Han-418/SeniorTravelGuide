package com.intel.NLPproject

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

object LogoutManager {
    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser

    fun logout(context: Context) {
        // Firebase 로그아웃
        FirebaseAuth.getInstance().signOut()
        // 카카오 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("Kakao", "카카오 로그아웃 실패: $error")
            } else {
                Log.d("Kakao", "카카오 로그아웃 성공")
            }
        }
        // 네이버 로그아웃
        NaverIdLoginSDK.logout()

        // TokenManager와 SharedPreferences 클리어
        TokenManager.kakaoAccessToken = null
        TokenManager.naverAccessToken = null
        // 여기서는 유틸리티 함수를 사용하여 클리어 (빈 문자열이나 제거)
        saveStableUid(context, "kakao_stable_uid", null)
        saveStableUid(context, "naver_stable_uid", null)
    }
}