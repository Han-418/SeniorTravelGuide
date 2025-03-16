package com.intel.NLPproject

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AttractionPreferenceScreen(navController: NavHostController) {
    // 여행 컨셉 옵션: 메인 옵션과 해당 세부 옵션
    val travelConceptMap = mapOf(
        "북적이는 핫플레이스" to listOf("액티브", "도심", "미식"),
        "한적하고 조용한 곳" to listOf("휴양형", "문화체험"),
        "상관없음 (추천)" to emptyList()
    )

    // 일정 및 활동량 옵션: 메인 옵션과 해당 세부 옵션
    val scheduleMap = mapOf(
        "여유형 (2-3곳 방문)" to listOf("중간중간 휴식", "일정 후 휴식"),
        "균형형 (4-5곳 방문)" to listOf("중간중간 휴식", "일정 후 휴식"),
        "활동형 (6곳 이상 방문)" to listOf("중간중간 휴식", "일정 후 휴식")
    )

    // 특수 고려사항 옵션 (멀티 선택)
    val specialConsiderations = listOf(
        "반려동물 동반",
        "휠체어 이동 가능",
        "아이들 놀이공간 필요",
        "고령자 동반 (체력 고려)"
    )

    // 선택 상태
    var selectedTravelConcept by remember { mutableStateOf("") }
    var selectedTravelConceptDetail by remember { mutableStateOf("") }
    var selectedSchedule by remember { mutableStateOf("") }
    var selectedScheduleDetail by remember { mutableStateOf("") }
    val selectedSpecialConsiderations = remember { mutableStateListOf<String>() }

    // 미선택: 분홍색, 선택됨: 회색 (색상은 예시로 사용)
    val unselectedColor = Color(0xFFFFC0CB)
    val selectedColor = Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .systemBarsPadding()
    ) {
        // 화면 제목
        Text(
            text = "여행 취향 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [여행 컨셉] 섹션
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("여행 컨셉", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    // 메인 옵션: 한 줄씩 표시
                    travelConceptMap.keys.toList().forEach { option ->
                        val containerColor =
                            if (selectedTravelConcept == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                selectedTravelConcept = option
                                selectedTravelConceptDetail = ""
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                        // 세부 옵션: 선택된 메인 옵션에 대해 Row로 표시
                        val subOptions = travelConceptMap[option] ?: emptyList()
                        if (selectedTravelConcept == option && subOptions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                subOptions.forEach { subOption ->
                                    val subContainerColor =
                                        if (selectedTravelConceptDetail == subOption) selectedColor else unselectedColor
                                    Button(
                                        onClick = { selectedTravelConceptDetail = subOption },
                                        colors = ButtonDefaults.buttonColors(containerColor = subContainerColor),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 2.dp),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = subOption,
                                            textAlign = TextAlign.Center,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // [일정 및 활동량] 섹션
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("일정 및 활동량", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    scheduleMap.keys.toList().forEach { option ->
                        val containerColor =
                            if (selectedSchedule == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                selectedSchedule = option
                                selectedScheduleDetail = ""
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                        val subOptions = scheduleMap[option] ?: emptyList()
                        if (selectedSchedule == option && subOptions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                subOptions.forEach { subOption ->
                                    val subContainerColor =
                                        if (selectedScheduleDetail == subOption) selectedColor else unselectedColor
                                    Button(
                                        onClick = { selectedScheduleDetail = subOption },
                                        colors = ButtonDefaults.buttonColors(containerColor = subContainerColor),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 2.dp),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = subOption,
                                            textAlign = TextAlign.Center,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // [특수 고려사항] 섹션 (멀티 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("특수 고려사항이 있나요? (필요하면 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    specialConsiderations.forEach { option ->
                        val containerColor =
                            if (selectedSpecialConsiderations.contains(option)) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                if (selectedSpecialConsiderations.contains(option)) {
                                    selectedSpecialConsiderations.remove(option)
                                } else {
                                    selectedSpecialConsiderations.add(option)
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Button(
                onClick = { navController.popBackStack() },
            ) {
                Text("뒤로가기")
            }
            // 제출 버튼 (기본 Button)
            Button(
                onClick = {
                    println(
                        "여행 컨셉: $selectedTravelConcept" +
                                if (selectedTravelConceptDetail.isNotEmpty()) " : $selectedTravelConceptDetail" else ""
                    )
                    println(
                        "일정 및 활동량: $selectedSchedule" +
                                if (selectedScheduleDetail.isNotEmpty()) " : $selectedScheduleDetail" else ""
                    )
                    println("특수 고려사항: $selectedSpecialConsiderations")
                    navController.navigate("loading/recommendAttraction2")
                }
            ) {
                Text("제출하기")
            }
        }
    }
}

//@Composable
//fun AttractionPreferenceScreen2(navController: NavHostController) {
//    // 질문 1: 하루에 몇 군데 방문하고 싶으세요? (단일 선택)
//    val visitOptions = listOf(
//        "2~3곳 (느긋한 일정)",
//        "4~5곳 (평범한 일정)",
//        "6곳 이상 (꽉 찬 일정)"
//    )
//    val selectedVisitOption = remember { mutableStateOf("") }
//
//    // 질문 2: 휴식 시간 필요하신가요? (단일 선택)
//    val restOptions = listOf(
//        "카페나 쉬는 장소 자주 들를래요",
//        "일정 끝나고 저녁에 휴식하면 돼요"
//    )
//    val selectedRestOption = remember { mutableStateOf("") }
//
//    // 질문 3: 특수 고려사항이 있나요? (다중 선택)
//    val specialConsiderations = listOf(
//        "반려동물 동반",
//        "휠체어 이동 가능",
//        "아이들 놀이공간 필요",
//        "고령자 동반 (체력 고려)"
//    )
//    val selectedSpecialConsiderations = remember { mutableStateListOf<String>() }
//
//    // 질문 4: 날씨 대비 기능을 사용할까요? (단일 선택)
//    val weatherOptions = listOf(
//        "비 오면 실내 코스로 변경",
//        "그대로 진행"
//    )
//    val selectedWeatherOption = remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//            .systemBarsPadding()
//            .padding(horizontal = 16.dp, vertical = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        // 제목
//        Text(
//            text = "여행 취향 설정2",
//            fontSize = 32.sp,
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        // 질문 1: 하루에 몇 군데 방문하고 싶으세요? (단일 선택)
//        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("하루에 몇 군데 방문하고 싶으세요? (한 개 선택)", fontSize = 18.sp)
//                Spacer(modifier = Modifier.height(8.dp))
//                visitOptions.forEach { option ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    ) {
//                        RadioButton(
//                            selected = selectedVisitOption.value == option,
//                            onClick = { selectedVisitOption.value = option }
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = option)
//                    }
//                }
//            }
//        }
//
//        // 질문 2: 휴식 시간 필요하신가요? (단일 선택)
//        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("휴식 시간 필요하신가요? (한 개 선택)", fontSize = 18.sp)
//                Spacer(modifier = Modifier.height(8.dp))
//                restOptions.forEach { option ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    ) {
//                        RadioButton(
//                            selected = selectedRestOption.value == option,
//                            onClick = { selectedRestOption.value = option }
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = option)
//                    }
//                }
//            }
//        }
//
//        // 질문 3: 특수 고려사항이 있나요? (다중 선택)
//        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("특수 고려사항이 있나요? (필요하면 선택)", fontSize = 18.sp)
//                Spacer(modifier = Modifier.height(8.dp))
//                specialConsiderations.forEach { option ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    ) {
//                        val checked = option in selectedSpecialConsiderations
//                        Checkbox(
//                            checked = checked,
//                            onCheckedChange = { isChecked ->
//                                if (isChecked) {
//                                    selectedSpecialConsiderations.add(option)
//                                } else {
//                                    selectedSpecialConsiderations.remove(option)
//                                }
//                            }
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = option)
//                    }
//                }
//            }
//        }
//
//        // 질문 4: 날씨 대비 기능을 사용할까요? (단일 선택)
//        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("날씨 대비 기능을 사용할까요? (한 개 선택)", fontSize = 18.sp)
//                Spacer(modifier = Modifier.height(8.dp))
//                weatherOptions.forEach { option ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    ) {
//                        RadioButton(
//                            selected = selectedWeatherOption.value == option,
//                            onClick = { selectedWeatherOption.value = option }
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = option)
//                    }
//                }
//            }
//        }
//
//        // 제출 버튼
//        Button(
//            onClick = {
//                // 선택된 결과 확인 (예: 로그 출력 혹은 다음 화면으로 전달)
//                println("방문 장소 수: ${selectedVisitOption.value}")
//                println("휴식 옵션: ${selectedRestOption.value}")
//                println("특수 고려사항: $selectedSpecialConsiderations")
//                println("날씨 대비: ${selectedWeatherOption.value}")
//
//                // 선택 데이터를 다음 화면으로 전달하거나 처리 로직 구현
//                navController.navigate("loading/recommendAttraction3")
//            },
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        ) {
//            Text("제출하기")
//        }
//    }
//}

@Composable
fun AccommodationPreferenceScreen(navController: NavHostController) {
    // 숙소 스타일 옵션: 메인 옵션과 해당 세부 옵션
    val accommodationStyleMap = mapOf(
        "가성비" to listOf("게스트하우스", "펜션", "모텔"),
        "고급" to listOf("호텔", "리조트", "럭셔리 빌라"),
        "특수" to listOf("한옥", "글램핑", "전원주택")
    )
    // 숙소 위치 옵션 (단일 선택)
    val accommodationLocationOptions = listOf(
        "관광지에서 가까운 곳 (도보 이동 가능)",
        "시내 중심 (대중교통 편리)",
        "한적한 곳 (자연 속 숙소)",
        "상관없음"
    )
    // 부가 시설 옵션 (멀티 선택)
    val additionalFacilitiesOptions = listOf(
        "주차장 필수 (자가용 이용)",
        "반려동물 동반 가능",
        "휠체어 이동 가능",
        "야외 테라스/바베큐 가능",
        "상관없음"
    )

    // 선택 상태
    var selectedAccommodationStyle by remember { mutableStateOf("") }
    var selectedAccommodationStyleDetail by remember { mutableStateOf("") }
    var selectedAccommodationLocation by remember { mutableStateOf("") }
    val selectedAdditionalFacilities = remember { mutableStateListOf<String>() }

    // 색상 설정: 미선택은 분홍색, 선택된 경우 회색 (예시)
    val unselectedColor = Color(0xFFFFC0CB)  // 분홍색
    val selectedColor = Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .systemBarsPadding()
    ) {
        // 화면 제목
        Text(
            text = "숙소 취향 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [숙소 스타일] 섹션 (한 개 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("숙소 스타일 (한 개 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    // 메인 옵션: 한 줄씩 표시
                    accommodationStyleMap.keys.toList().forEach { option ->
                        val containerColor =
                            if (selectedAccommodationStyle == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                selectedAccommodationStyle = option
                                selectedAccommodationStyleDetail = ""
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                        // 세부 옵션: 선택된 메인 옵션에 대해 Row로 표시
                        val subOptions = accommodationStyleMap[option] ?: emptyList()
                        if (selectedAccommodationStyle == option && subOptions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                subOptions.forEach { subOption ->
                                    val subContainerColor =
                                        if (selectedAccommodationStyleDetail == subOption) selectedColor else unselectedColor
                                    Button(
                                        onClick = { selectedAccommodationStyleDetail = subOption },
                                        colors = ButtonDefaults.buttonColors(containerColor = subContainerColor),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(vertical = 2.dp),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = subOption,
                                            textAlign = TextAlign.Center,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // [숙소 위치] 섹션 (한 개 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("숙소 위치 (한 개 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    accommodationLocationOptions.forEach { option ->
                        val containerColor =
                            if (selectedAccommodationLocation == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = { selectedAccommodationLocation = option },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // [부가 시설 선택] 섹션 (멀티 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("부가 시설 선택 (필요하면 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    additionalFacilitiesOptions.forEach { option ->
                        val containerColor =
                            if (selectedAdditionalFacilities.contains(option)) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                if (selectedAdditionalFacilities.contains(option)) {
                                    selectedAdditionalFacilities.remove(option)
                                } else {
                                    selectedAdditionalFacilities.add(option)
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
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
                onClick = { navController.popBackStack() },
            ) {
                Text("뒤로가기")
            }
            // 제출 버튼 (기본 Button)
            Button(
                onClick = {
                    println(
                        "숙소 스타일: $selectedAccommodationStyle" +
                                if (selectedAccommodationStyleDetail.isNotEmpty()) " : $selectedAccommodationStyleDetail" else ""
                    )
                    println("숙소 위치: $selectedAccommodationLocation")
                    println("부가 시설: $selectedAdditionalFacilities")
                    navController.navigate("loading/recommendAccommodation")
                }
            ) {
                Text("제출하기")
            }
        }
    }
}

@Composable
fun RestaurantPreferenceScreen(navController: NavHostController) {
    // 식사 스타일 옵션 (최대 2개 선택)
    val mealStyleOptions = listOf(
        "현지 맛집 (지역 특산물)",
        "프랜차이즈",
        "가성비 좋은 식당",
        "고급 레스토랑",
        "카페식 가벼운 식사 (샐러드, 브런치)",
        "상관없음"
    )
    // 식당 위치 옵션 (단일 선택)
    val restaurantLocationOptions = listOf(
        "관광지 근처 식당",
        "숙소 주변 식당",
        "맛집이라면 멀어도 OK",
        "상관없음"
    )
    // 특수 조건 옵션 (멀티 선택)
    val specialConditionsOptions = listOf(
        "반려동물 동반 가능해야 함",
        "아이 동반 가능 (놀이 공간)",
        "노약자에게 편한 자리 (의자, 접근성)",
        "예약 필수 식당 선호",
        "상관없음"
    )

    // 선택 상태
    val selectedMealStyles = remember { mutableStateListOf<String>() } // 최대 2개 선택
    var selectedRestaurantLocation by remember { mutableStateOf("") }
    val selectedSpecialConditions = remember { mutableStateListOf<String>() }

    // 색상 설정: 미선택은 분홍색, 선택되면 회색 (예시)
    val unselectedColor = Color(0xFFFFC0CB) // 분홍색
    val selectedColor = Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .systemBarsPadding()
    ) {
        // 화면 제목
        Text(
            text = "식사 취향 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [식사 스타일] 섹션 (최대 2개 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("식사 스타일 (최대 2개 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    mealStyleOptions.forEach { option ->
                        val containerColor =
                            if (selectedMealStyles.contains(option)) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                if (selectedMealStyles.contains(option)) {
                                    selectedMealStyles.remove(option)
                                } else {
                                    if (selectedMealStyles.size < 2) {
                                        selectedMealStyles.add(option)
                                    }
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // [식당 위치] 섹션 (단일 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("식당 위치 (한 개 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    restaurantLocationOptions.forEach { option ->
                        val containerColor =
                            if (selectedRestaurantLocation == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = { selectedRestaurantLocation = option },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // [특수 조건] 섹션 (멀티 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("특수 조건 (필요하면 선택)", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    specialConditionsOptions.forEach { option ->
                        val containerColor =
                            if (selectedSpecialConditions.contains(option)) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = {
                                if (selectedSpecialConditions.contains(option)) {
                                    selectedSpecialConditions.remove(option)
                                } else {
                                    selectedSpecialConditions.add(option)
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
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
                onClick = { navController.popBackStack() }
            ) {
                Text("뒤로가기")
            }
            // 제출 버튼 (기본 Button)
            Button(
                onClick = {
                    println("식사 스타일: $selectedMealStyles")
                    println("식당 위치: $selectedRestaurantLocation")
                    println("특수 조건: $selectedSpecialConditions")
                    navController.navigate("loading/recommendRestaurants")
                }
            ) {
                Text("제출하기")
            }
        }
    }
}

@Composable
fun TravelTimePreferenceScreen(navController: NavHostController) {
    // 여행 시작 시간 옵션 (단일 선택)
    val startTimeOptions = listOf(
        "아침 일찍 (7~8시)",
        "보통 아침 (9~10시)",
        "여유롭게 늦게 출발 (11~12시 이후)"
    )
    val selectedStartTime = remember { mutableStateOf("") }

    // 여행 종료 시간 옵션 (단일 선택)
    val endTimeOptions = listOf(
        "저녁 6~7시쯤 (조금 일찍 끝내기)",
        "밤 8~9시쯤 (적당한 마무리)",
        "늦게까지 가능 (야경, 밤 코스 포함)"
    )
    val selectedEndTime = remember { mutableStateOf("") }

    // 색상 설정: 미선택은 분홍색, 선택된 경우 회색 (예시)
    val unselectedColor = Color(0xFFFFC0CB) // 분홍색
    val selectedColor = Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .systemBarsPadding()
    ) {
        // 화면 제목
        Text(
            text = "여행 시간 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        // 스크롤 영역 (weight를 사용해 하단 버튼이 항상 보이도록)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [여행 시작 시간] 섹션 (단일 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("여행 시작 시간 (한 개 선택)", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    startTimeOptions.forEach { option ->
                        val containerColor =
                            if (selectedStartTime.value == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = { selectedStartTime.value = option },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            // [여행 종료 시간] 섹션 (단일 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("여행 종료 시간 (한 개 선택)", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    endTimeOptions.forEach { option ->
                        val containerColor =
                            if (selectedEndTime.value == option) selectedColor else unselectedColor
                        OutlinedButton(
                            onClick = { selectedEndTime.value = option },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
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
                onClick = { navController.popBackStack() }
            ) {
                Text("뒤로가기")
            }
            // 제출 버튼 (하단에 항상 보임)
            Button(
                onClick = {
                    println("여행 시작 시간: ${selectedStartTime.value}")
                    println("여행 종료 시간: ${selectedEndTime.value}")
                    navController.navigate("loading/travelPlan")
                }
            ) {
                Text("최종 여행 일정 보기")
            }
        }
    }
}
