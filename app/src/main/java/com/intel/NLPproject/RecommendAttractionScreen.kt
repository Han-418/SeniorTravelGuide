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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserId
import com.intel.NLPproject.firebase.AttractionDatabase

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    // 예시 관광지 추천 목록 (10개)
    val attractions = listOf(
        "관광지 추천 1", "관광지 추천 2", "관광지 추천 3", "관광지 추천 4", "관광지 추천 5",
        "관광지 추천 6", "관광지 추천 7", "관광지 추천 8", "관광지 추천 9", "관광지 추천 10"
    )
    val context = LocalContext.current

    // 선택된 관광지를 저장할 상태 (다중 선택 가능)
    val selectedAttractions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "추천 관광지1",
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
            items(attractions.size) { index ->
                val attraction = attractions[index]
                // 선택 상태 여부
                val isSelected = selectedAttractions.contains(attraction)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            if (isSelected) {
                                selectedAttractions.remove(attraction)
                            } else {
                                selectedAttractions.add(attraction)
                            }
                        },
                    // 선택 상태일 때 테두리나 elevation을 달리 할 수 있음.
                    elevation = if (isSelected) CardDefaults.cardElevation(defaultElevation = 8.dp)
                    else CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isSelected) Color.LightGray else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = attraction)
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val currentUserId = getCurrentUserId(context)
                    if (currentUserId != null) {
                        if (selectedAttractions.isNotEmpty()) {
                            AttractionDatabase.saveAttractions(
                                currentUserId,
                                selectedAttractions
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "관광지 정보 저장 완료", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigate("accommodationPreference")
                                } else {
                                    Toast.makeText(context, "관광지 정보 저장 실패", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "선택된 관광지가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "로그인된 유저가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("관광지 확정 후 숙소 선택")
            }
            Button(
                onClick = {
                    navController.navigate("first")
                }
            ) {
                Text("메인화면으로")
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    navController.navigate("attractionPreference")
                }
            ) {
                Text("못고르겠어요")
            }
            Button(
                onClick = { navController.popBackStack() },
            ) {
                Text("뒤로가기")
            }
        }
    }
}

@Composable
fun RecommendAttractionScreen2(navController: NavHostController) {
    // 예시 관광지 추천 목록 (10개)
    val attractions = listOf(
        "관광지 추천 1", "관광지 추천 2", "관광지 추천 3", "관광지 추천 4", "관광지 추천 5",
        "관광지 추천 6", "관광지 추천 7", "관광지 추천 8", "관광지 추천 9", "관광지 추천 10"
    )
    val context = LocalContext.current

    // 선택된 관광지를 저장할 상태 (다중 선택 가능)
    val selectedAttractions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "추천 관광지2",
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
            items(attractions.size) { index ->
                val attraction = attractions[index]
                // 선택 상태 여부
                val isSelected = selectedAttractions.contains(attraction)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            if (isSelected) {
                                selectedAttractions.remove(attraction)
                            } else {
                                selectedAttractions.add(attraction)
                            }
                        },
                    // 선택 상태일 때 테두리나 elevation을 달리 할 수 있음.
                    elevation = if (isSelected) CardDefaults.cardElevation(defaultElevation = 8.dp)
                    else CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isSelected) Color.LightGray else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = attraction)
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
            Button(
                onClick = {
                    val currentUserId = getCurrentUserId(context)
                    if (currentUserId != null) {
                        if (selectedAttractions.isNotEmpty()) {
                            AttractionDatabase.saveAttractions(
                                currentUserId,
                                selectedAttractions
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "관광지 정보 저장 완료", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigate("accommodationPreference")
                                } else {
                                    Toast.makeText(context, "관광지 정보 저장 실패", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "선택된 관광지가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "로그인된 유저가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("관광지 확정 후 숙소 선택")
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

@Composable
fun RecommendAttractionScreen3(navController: NavHostController) {
    // 예시 관광지 추천 목록 (10개)
    val attractions = listOf(
        "관광지 추천 1", "관광지 추천 2", "관광지 추천 3", "관광지 추천 4", "관광지 추천 5",
        "관광지 추천 6", "관광지 추천 7", "관광지 추천 8", "관광지 추천 9", "관광지 추천 10"
    )
    val context = LocalContext.current

    // 선택된 관광지를 저장할 상태 (다중 선택 가능)
    val selectedAttractions = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "추천 관광지 final",
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
            items(attractions.size) { index ->
                val attraction = attractions[index]
                // 선택 상태 여부
                val isSelected = selectedAttractions.contains(attraction)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            if (isSelected) {
                                selectedAttractions.remove(attraction)
                            } else {
                                selectedAttractions.add(attraction)
                            }
                        },
                    // 선택 상태일 때 테두리나 elevation을 달리 할 수 있음.
                    elevation = if (isSelected) CardDefaults.cardElevation(defaultElevation = 8.dp)
                    else CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isSelected) Color.LightGray else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = attraction)
                    }
                }
            }
        }
        // "숙소 정하자" 버튼: 선택된 관광지만 저장
        Button(
            onClick = {
                val currentUserId = getCurrentUserId(context)
                if (currentUserId != null) {
                    if (selectedAttractions.isNotEmpty()) {
                        AttractionDatabase.saveAttractions(
                            currentUserId,
                            selectedAttractions
                        ) { success ->
                            if (success) {
                                Toast.makeText(context, "관광지 정보 저장 완료", Toast.LENGTH_SHORT).show()
                                navController.navigate("accommodationPreference")
                            } else {
                                Toast.makeText(context, "관광지 정보 저장 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "선택된 관광지가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "로그인된 유저가 없습니다", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "숙소 정하자")
        }
    }
}
