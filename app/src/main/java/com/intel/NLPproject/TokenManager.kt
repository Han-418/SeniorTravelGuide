package com.intel.NLPproject

object TokenManager {
    var kakaoAccessToken: String? = null
    var naverAccessToken: String? = null

    fun getCurrentUserKey(): String? {
        // 우선 네이버 토큰(안정적인 uid)이 있으면 반환, 없으면 카카오 uid 반환
        TokenManager.naverAccessToken?.let { return it }
        TokenManager.kakaoAccessToken?.let { return it }
        return null
    }
}