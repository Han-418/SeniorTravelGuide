package com.intel.NLPproject.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// 서버로 전송할 데이터 모델
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

// 추천 결과 데이터 모델
data class Recommendation(
    val attraction_name: String,
    val average_sentiment_score: Double,
    val short_review: String,
    val image_url: String
)

// 서버 응답 모델
data class RecommendationResponse(
    val recommendations: List<Recommendation>
)
// 비동기 서버 응답 모댈
data class TaskResponse(val task_id: String?)
data class TaskStatusResponse(val state: String, val recommendations: List<Recommendation>?)


// Google Cloud 서버에 요청할 API 인터페이스
interface CloudApiService {
    @POST("submitQuestion")
    suspend fun submitQuestion(@Body questionData: QuestionData): Response<TaskResponse>

    // 페이지네이션을 위한 Query 파라미터 추가
    @GET("taskStatus/{task_id}")
    suspend fun getTaskStatus(
        @Path("task_id") taskId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<TaskStatusResponse>
}

