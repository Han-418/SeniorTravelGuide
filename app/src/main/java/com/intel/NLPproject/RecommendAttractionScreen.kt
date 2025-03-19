package com.intel.NLPproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import kotlinx.coroutines.launch

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    var recommendations by remember { mutableStateOf<List<Recommendation>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // 화면이 시작될 때 Retrofit 호출하여 추천 데이터 불러오기
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                // 여기서는 QuestionScreen에서 전송했던 데이터와 동일하거나 저장된 데이터를 사용할 수 있습니다.
                // 예시로 QuestionData를 새로 생성하거나, 이전에 저장한 값을 활용하세요.
                val questionData = com.intel.NLPproject.api.QuestionData(
                    selectedDestination = "제주도",
                    selectedSubregion = "서귀포시",
                    selectedCompanion = "친구들과",
                    selectedTransportation = "버스",
                    selectedBudget = "평균적",
                    customDestinationText = "",
                    customDestinationInput = "",
                    selectedDeparture = "2024-03-20",
                    selectedReturn = "2024-03-23"
                )
                val response = RetrofitClient.cloudApiService.submitQuestion(questionData)
                if (response.isSuccessful) {
                    recommendations = response.body()?.recommendations
                }
            } catch (e: Exception) {
                println("Retrofit Error: ${e.message}")
            }
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            recommendations?.let { recList ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(recList.size) { index ->
                        val rec = recList[index]
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
            } ?: Text("추천 데이터를 불러올 수 없습니다.")
        }
    }
}
