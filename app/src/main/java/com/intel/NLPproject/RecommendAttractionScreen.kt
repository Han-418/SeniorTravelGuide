package com.intel.NLPproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.api.Recommendation
import com.intel.NLPproject.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecommendAttractionScreen(navController: NavHostController, taskId: String) {
    var recommendations by remember { mutableStateOf<List<Recommendation>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var pollingCount by remember { mutableStateOf(0) }  // 폴링 횟수
    val maxPollingAttempts = 10  // 최대 폴링 횟수
    val pollingIntervalMs = 5000L  // 폴링 주기 (1000L = 1초)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(taskId) {
        // 폴링을 수행하는 함수
        fun pollTaskStatus() {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.cloudApiService.getTaskStatus(taskId)
                    if (response.isSuccessful && response.code() == 200) {
                        // Celery 작업 완료. 결과 데이터를 받아온다.
                        val body = response.body()
                        val newRecs = body?.recommendations ?: emptyList()
                        recommendations = newRecs
                        isLoading = false
                    } else if (response.code() == 202) {
                        // 아직 작업 중이면 일정 시간 후 재시도
                        if (pollingCount < maxPollingAttempts) {
                            pollingCount++
                            kotlinx.coroutines.delay(pollingIntervalMs)
                            pollTaskStatus() // 재귀 호출
                        } else {
                            isLoading = false
                        }
                    } else {
                        isLoading = false
                    }
                } catch (e: Exception) {
                    isLoading = false
                    println("Retrofit Error: ${e.message}")
                }
            }
        }

        // 화면이 뜨면 바로 폴링 시작
        pollTaskStatus()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (recommendations.isEmpty()) {
                Text("추천 결과가 없습니다.")
            } else {
                // 추천 목록 표시
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(recommendations.size) { index ->
                            val rec = recommendations[index]
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = rec.attraction_name,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = rec.short_review,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

