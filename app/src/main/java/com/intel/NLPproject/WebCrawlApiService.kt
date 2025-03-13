package com.intel.NLPproject

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebCrawlApiService {
    // 예시 엔드포인트: /crawl?query=제주도+관광지
    @GET("crawl")
    suspend fun getCrawledData(
        @Query("query") query: String = "제주도 관광지"
    ): Response<WebCrawlResponse>
}

data class WebCrawlResponse(
    val query: String,
    val results: List<CrawlResult>
)

data class CrawlResult(
    val title: String,
    val description: String
)
