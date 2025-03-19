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
fun RecommendAttractionScreen(navController: NavHostController) {
    var recommendations by remember { mutableStateOf<List<Recommendation>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableStateOf(1) }
    val pageLimit = 10  // 서버에서 한 페이지 당 10개씩 반환한다고 가정

    val coroutineScope = rememberCoroutineScope()
    val taskId = "이미 받은 taskId" // 이전 화면에서 받은 taskId를 활용

    // 페이지네이션 함수: 특정 페이지 데이터를 불러옴
    fun loadRecommendations(page: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.cloudApiService.getTaskStatus(taskId, page, pageLimit)
                if (response.isSuccessful) {
                    // 기존 추천 목록에 추가
                    response.body()?.let { body ->
                        val newRecs = body.recommendations ?: emptyList()
                        recommendations = recommendations + newRecs
                    }
                }
            } catch (e: Exception) {
                println("Retrofit Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // 화면 시작 시 1페이지 데이터 로드
    LaunchedEffect(Unit) {
        loadRecommendations(currentPage)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading && recommendations.isEmpty()) {
            CircularProgressIndicator()
        } else {
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
                // "더 불러오기" 버튼을 클릭하면 다음 페이지를 로드
                Button(
                    onClick = {
                        currentPage++
                        isLoading = true
                        loadRecommendations(currentPage)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("더 불러오기")
                }
            }
        }
    }
}
