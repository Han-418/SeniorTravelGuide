package com.intel.NLPproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

// 데이터 클래스는 WebCrawlApiService.kt에 정의되어 있음
// data class WebCrawlResponse(val query: String, val results: List<CrawlResult>)
// data class CrawlResult(val title: String, val description: String)

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    // API 호출을 통해 받아온 관광지 데이터를 저장하는 상태
    val attractionsState by produceState<List<CrawlResult>>(initialValue = emptyList()) {
        // 기본 검색어는 "제주도 관광지"로 호출 (필요에 따라 변경 가능)
        val response = RetrofitClient.apiService.getCrawledData(query = "제주도 관광지")
        if (response.isSuccessful) {
            value = response.body()?.results ?: emptyList()
        } else {
            // API 호출 실패 시 빈 리스트 또는 에러 처리 로직 추가
            value = emptyList()
        }
    }

    // 사용자가 선택한 관광지 리스트 (제목만 저장)
    val selectedAttractions = remember { mutableStateListOf<String>() }
    // 스크롤 가능한 리스트를 위해 LazyColumn 사용
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "추천 관광지 목록",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(attractionsState) { attraction ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    val checked = selectedAttractions.contains(attraction.title)
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedAttractions.add(attraction.title)
                            } else {
                                selectedAttractions.remove(attraction.title)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = attraction.title, fontSize = 28.sp)
                }
            }
        }

        Button(
            onClick = {
                // 선택된 관광지 목록 출력 혹은 다음 화면으로 전달
                println("선택된 관광지: $selectedAttractions")
                // 예: navController.navigate("다음화면")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(text = "선택 완료", fontSize = 28.sp)
        }
    }
}
