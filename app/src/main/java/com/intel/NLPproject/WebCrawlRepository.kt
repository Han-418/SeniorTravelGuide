package com.intel.NLPproject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

// 기본 쿼리 값을 상수로 분리
const val DEFAULT_QUERY = "제주도 관광지"

class WebCrawlRepository {

    // 쿼리 파라미터의 기본값을 DEFAULT_QUERY로 지정합니다.
    suspend fun fetchCrawledData(query: String = DEFAULT_QUERY): Result<WebCrawlResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<WebCrawlResponse> = RetrofitClient.apiService.getCrawledData(query)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Result.success(data)
                    } ?: Result.failure(Exception("응답 본문이 없습니다."))
                } else {
                    Result.failure(Exception("API 호출 실패: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
