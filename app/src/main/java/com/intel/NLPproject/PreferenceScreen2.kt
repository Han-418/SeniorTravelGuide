package com.intel.NLPproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AttractionPreferenceScreen2(navController: NavHostController) {
    // 질문 1: 하루에 몇 군데 방문하고 싶으세요? (단일 선택)
    val visitOptions = listOf(
        "2~3곳 (느긋한 일정)",
        "4~5곳 (평범한 일정)",
        "6곳 이상 (꽉 찬 일정)"
    )
    val selectedVisitOption = remember { mutableStateOf("") }

    // 질문 2: 휴식 시간 필요하신가요? (단일 선택)
    val restOptions = listOf(
        "카페나 쉬는 장소 자주 들를래요",
        "일정 끝나고 저녁에 휴식하면 돼요"
    )
    val selectedRestOption = remember { mutableStateOf("") }

    // 질문 3: 특수 고려사항이 있나요? (다중 선택)
    val specialConsiderations = listOf(
        "반려동물 동반",
        "휠체어 이동 가능",
        "아이들 놀이공간 필요",
        "고령자 동반 (체력 고려)"
    )
    val selectedSpecialConsiderations = remember { mutableStateListOf<String>() }

    // 질문 4: 날씨 대비 기능을 사용할까요? (단일 선택)
    val weatherOptions = listOf(
        "비 오면 실내 코스로 변경",
        "그대로 진행"
    )
    val selectedWeatherOption = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 제목
        Text(
            text = "여행 취향 설정2",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 질문 1: 하루에 몇 군데 방문하고 싶으세요? (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("하루에 몇 군데 방문하고 싶으세요? (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                visitOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedVisitOption.value == option,
                            onClick = { selectedVisitOption.value = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        }

        // 질문 2: 휴식 시간 필요하신가요? (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("휴식 시간 필요하신가요? (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                restOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedRestOption.value == option,
                            onClick = { selectedRestOption.value = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        }

        // 질문 3: 특수 고려사항이 있나요? (다중 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("특수 고려사항이 있나요? (필요하면 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                specialConsiderations.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        val checked = option in selectedSpecialConsiderations
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedSpecialConsiderations.add(option)
                                } else {
                                    selectedSpecialConsiderations.remove(option)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        }

        // 질문 4: 날씨 대비 기능을 사용할까요? (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("날씨 대비 기능을 사용할까요? (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                weatherOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedWeatherOption.value == option,
                            onClick = { selectedWeatherOption.value = option }
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
                // 선택된 결과 확인 (예: 로그 출력 혹은 다음 화면으로 전달)
                println("방문 장소 수: ${selectedVisitOption.value}")
                println("휴식 옵션: ${selectedRestOption.value}")
                println("특수 고려사항: $selectedSpecialConsiderations")
                println("날씨 대비: ${selectedWeatherOption.value}")

                // 선택 데이터를 다음 화면으로 전달하거나 처리 로직 구현
                navController.navigate("recommendAttraction3")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("제출하기")
        }
    }
}
