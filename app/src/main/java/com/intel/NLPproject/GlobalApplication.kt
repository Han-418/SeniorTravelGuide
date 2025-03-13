package com.intel.NLPproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 네이버 로그인 SDK 초기화
        NaverIdLoginSDK.initialize(this, "6MOMPVA7oIECJodLVeeV", "9WCzREbmR1", "SeniorTravelGuide")
        // 카카오 SDK 초기화 (NATIVE_APP_KEY 부분은 카카오 개발자센터에서 발급받은 값)
        KakaoSdk.init(this, "7491d378e550ec14a3ee3c96fab41252")
    }
}