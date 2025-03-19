package com.intel.NLPproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

// 서버에 전달할 질문 데이터 모델
data class QuestionData(
    val selectedDestination: String,
    val selectedSubregion: String,
    val selectedCompanion: String,
    val selectedTransportation: String,
    val selectedBudget: String,
    val customDestinationText: String,
    val customDestinationInput: String,
    val selectedDeparture: String,
    val selectedReturn: String
)

// Google Cloud 서버에 요청할 API 인터페이스
interface CloudApiService {
    @POST("submitQuestion") // 엔드포인트 (서버 설정에 맞게 수정)
    suspend fun submitQuestion(@Body data: QuestionData): Response<Void>
}

object RetrofitClient {
    private const val BASE_URL = "http://34.47.121.123:5000/"

    val cloudApiService: CloudApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudApiService::class.java)
    }
}
