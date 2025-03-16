package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserId
import com.intel.NLPproject.firebase.AccommodationDatabase

@Composable
fun RecommendAccommodationScreen(navController: NavHostController) {
    // 예시 숙소 추천 목록 (10개)
    val accommodations = listOf(
        "숙소 추천 1", "숙소 추천 2", "숙소 추천 3", "숙소 추천 4", "숙소 추천 5",
        "숙소 추천 6", "숙소 추천 7", "숙소 추천 8", "숙소 추천 9", "숙소 추천 10"
    )
    val context = LocalContext.current

    // 선택된 숙소 목록을 저장 (다중 선택 가능)
    val selectedAccommodations = remember { mutableStateListOf<String>() }

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
            items(accommodations.size) { index ->
                val accommodation = accommodations[index]
                val isSelected = selectedAccommodations.contains(accommodation)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            if (isSelected) {
                                selectedAccommodations.remove(accommodation)
                            } else {
                                selectedAccommodations.add(accommodation)
                            }
                        },
                    elevation = if (isSelected)
                        CardDefaults.cardElevation(defaultElevation = 8.dp)
                    else
                        CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isSelected) Color.LightGray else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = accommodation)
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.popBackStack() },
            ) {
                Text("뒤로가기")
            }
            // 선택한 숙소 목록만 저장하고 다음 화면으로 이동
            Button(
                onClick = {
                    // 현재 로그인된 유저의 ID를 가져옵니다.
                    val currentUserId = getCurrentUserId(context)
                    if (currentUserId != null) {
                        if (selectedAccommodations.isNotEmpty()) {
                            AccommodationDatabase.saveAccommodations(
                                currentUserId,
                                selectedAccommodations
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "숙소 정보 저장 완료", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigate("restaurantPreference")
                                } else {
                                    Toast.makeText(context, "숙소 정보 저장 실패", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "선택된 숙소가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "로그인된 유저가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                },
            ) {
                Text(text = "저장 후 식당")
            }
        }
        Button(
            onClick = {
                navController.navigate("first")
            }
        ) {
            Text("메인화면으로")
        }

    }
}
