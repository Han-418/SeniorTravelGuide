package com.intel.NLPproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    // 관광지 추천 목록 (예시 10개)
    val attractions = listOf(
        "관광지 추천 1", "관광지 추천 2", "관광지 추천 3", "관광지 추천 4", "관광지 추천 5",
        "관광지 추천 6", "관광지 추천 7", "관광지 추천 8", "관광지 추천 9", "관광지 추천 10"
    )
    // 사용자가 선택한 관광지 리스트
    val selectedAttractions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "추천 관광지 목록",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 각 관광지에 대해 체크박스 생성
        attractions.forEach { attraction ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                val checked = selectedAttractions.contains(attraction)
                Checkbox(
                    checked = checked,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            selectedAttractions.add(attraction)
                        } else {
                            selectedAttractions.remove(attraction)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = attraction, fontSize = 28.sp)
            }
        }

        // 선택 완료 버튼
        Button(
            onClick = {
                // 선택된 관광지 목록을 처리하거나 다음 단계로 전달
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
