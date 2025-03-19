package com.intel.NLPproject.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://34.22.106.23:5000/"

    val cloudApiService: CloudApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Gson은 기본적으로 유니코드 이스케이프를 디코딩함
            .build()
            .create(CloudApiService::class.java)
    }
}
