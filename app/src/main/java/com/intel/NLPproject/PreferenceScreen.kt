package com.intel.NLPproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AttractionPreferenceScreen(navController: NavHostController) {
    // 원하시는 여행 스타일? (최대 2개 선택)
    val travelStyles = listOf(
        "조용히 쉬는 여행 (자연, 산책 위주)",
        "조금 걷는 코스 (등산, 트레킹)",
        "도심 관광 (명소, 쇼핑)",
        "문화 체험 (역사, 전통시장, 박물관)",
        "먹거리 위주 (맛집 탐방)"
    )
    val selectedTravelStyles = remember { mutableStateListOf<String>() }

    // 하루 활동량 (단일 선택)
    val activityLevels = listOf(
        "조금만 걸을래요 (쉬엄쉬엄)",
        "적당히 걸어도 괜찮아요",
        "많이 돌아다녀도 좋습니다"
    )
    val selectedActivityLevel = remember { mutableStateOf("") }

    // 사람 많은 곳 vs 조용한 곳? (단일 선택)
    val placePreferences = listOf(
        "북적이는 곳 (핫플)",
        "사람 적은 곳 (한적함)",
        "상관없음"
    )
    val selectedPlacePreference = remember { mutableStateOf("") }

    // 피하고 싶은 요소 (다중 선택)
    val avoidOptions = listOf(
        "높은 곳 (전망대, 케이블카 등)",
        "배 타기 (섬 여행, 유람선 등)",
        "너무 시끄러운 곳",
        "오래 걷는 곳"
    )
    val selectedAvoids = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 제목
        Text(
            text = "여행 취향 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 질문 1: 여행 스타일 (최대 2개 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1️⃣ 원하시는 여행 스타일? (최대 2개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                travelStyles.forEach { style ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        val checked = style in selectedTravelStyles
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    if (selectedTravelStyles.size < 2) {
                                        selectedTravelStyles.add(style)
                                    }
                                    // 만약 이미 2개 선택된 상태면 추가 선택은 무시하거나 토스트 메시지 등으로 안내할 수 있습니다.
                                } else {
                                    selectedTravelStyles.remove(style)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = style)
                    }
                }
            }
        }

        // 질문 2: 하루 활동량 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2️⃣ 하루 활동량은 어느 정도가 적당할까요?", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                activityLevels.forEach { level ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedActivityLevel.value == level,
                            onClick = { selectedActivityLevel.value = level }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = level)
                    }
                }
            }
        }

        // 질문 3: 사람 많은 곳 vs 조용한 곳 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3️⃣ 사람 많은 곳 vs 조용한 곳?", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                placePreferences.forEach { pref ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedPlacePreference.value == pref,
                            onClick = { selectedPlacePreference.value = pref }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = pref)
                    }
                }
            }
        }

        // 질문 4: 피하고 싶은 요소 (다중 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4️⃣ 피하고 싶은 요소가 있나요? (필요하면 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                avoidOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        val checked = option in selectedAvoids
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedAvoids.add(option)
                                } else {
                                    selectedAvoids.remove(option)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        }

        // 제출 버튼
        Button(
            onClick = {
                // 선택 결과 확인 (디버깅용)
                println("여행 스타일: $selectedTravelStyles")
                println("하루 활동량: ${selectedActivityLevel.value}")
                println("사람 많은 곳 vs 조용한 곳: ${selectedPlacePreference.value}")
                println("피하고 싶은 요소: $selectedAvoids")
                // 선택된 데이터를 다음 화면이나 처리 로직에 전달합니다.
                navController.navigate("recommendAttraction2")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("제출하기")
        }
    }
}
