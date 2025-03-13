package com.intel.NLPproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun RecommendAccommodationScreen(navController: NavHostController) {
    // 예시 관광지 추천 목록 (10개)
    val accommodations = listOf(
        "숙소 추천 1", "숙소 추천 2", "숙소 추천 3", "숙소 추천 4", "숙소 추천 5",
        "숙소 추천 6", "숙소 추천 7", "숙소 추천 8", "숙소 추천 9", "숙소 추천 10"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "추천 숙소",
            fontSize = 32.sp
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(accommodations.count()) { accommodation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // 정사각형 형태의 셀
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("accommodation")
                    }
                }
            }
        }
        // 버튼 영역: 그리드 아래에 전체 너비로 배치
        Button(
            onClick = {
                navController.navigate("restaurantPreference")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "식당 정하자")
        }
    }
}