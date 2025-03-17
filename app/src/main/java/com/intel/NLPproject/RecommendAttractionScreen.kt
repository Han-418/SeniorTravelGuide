package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserId
import com.intel.NLPproject.firebase.AttractionDatabase

// 데이터 클래스는 WebCrawlApiService.kt에 정의되어 있음
// data class WebCrawlResponse(val query: String, val results: List<CrawlResult>)
// data class CrawlResult(val title: String, val description: String)

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var currentDescription by remember { mutableStateOf("") }
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
                        IconButton(
                            onClick = {
                                currentDescription = attraction
                                showDescriptionDialog = true
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "관광지 설명 보기"
                            )
                        }
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
        // 관광지 설명 다이얼로그
        if (showDescriptionDialog) {
            AlertDialog(
                onDismissRequest = { showDescriptionDialog = false },
                title = { Text(currentDescription, fontSize = 32.sp) },
                text = {
                    Column(
                        modifier = Modifier
                            .height(500.dp)
                            .padding(top = 8.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .border(2.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                        ) {
                            Text("Image")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(2.dp)
                            ) { }
                            Divider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp),
                                color = Color.Black
                            )
                            Column(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(2.dp)
                            ) { }
                            Divider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp),
                                color = Color.Black
                            )
                            Column(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(2.dp)
                            ) { }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .border(2.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "제주특별자치도 서귀포시 성산읍 고성리에 있는 해안 지형. 섭지란 재사가 많이 배출되는 지세란 뜻이며 코지는 제주 방언이 아니라 섭지 어쩌구",
                                fontSize = 16.sp
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showDescriptionDialog = false }
                    ) {
                        Text("닫기")
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, bottom = 8.dp),
                containerColor = Color.White
            )
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
