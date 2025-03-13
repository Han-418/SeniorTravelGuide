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

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    // Repository를 이용해 기본 쿼리(DEFAULT_QUERY)를 사용하여 관광지 데이터를 가져옴
    val attractionsState by produceState<List<CrawlResult>>(initialValue = emptyList()) {
        val repository = WebCrawlRepository()
        val result = repository.fetchCrawledData(query = DEFAULT_QUERY)
        if (result.isSuccess) {
            value = result.getOrNull()?.results ?: emptyList()
        } else {
            // 에러 발생 시 빈 리스트로 처리하거나 에러 메시지 등 추가 로직 구현 가능
            value = emptyList()
        }
    }

    // 사용자가 선택한 관광지 목록 (각 항목의 title을 저장)
    val selectedAttractions = remember { mutableStateListOf<String>() }

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

        // LazyColumn을 사용해 스크롤 가능한 리스트로 출력
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
                // 선택된 관광지 목록을 처리하거나 다음 화면으로 전달하는 로직 구현
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
