package com.intel.NLPproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화 (NATIVE_APP_KEY 부분은 카카오 개발자센터에서 발급받은 값)
        KakaoSdk.init(this, "7491d378e550ec14a3ee3c96fab41252")
    }
}