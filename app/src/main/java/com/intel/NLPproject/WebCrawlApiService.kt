package com.intel.NLPproject

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebCrawlApiService {
    @GET("crawl")
    suspend fun getCrawledData(
        @Query("query") query: String = DEFAULT_QUERY
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
