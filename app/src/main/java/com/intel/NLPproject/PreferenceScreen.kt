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
                navController.navigate("loading/recommendAttraction2")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("제출하기")
        }
    }
}

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
                navController.navigate("loading/recommendAttraction3")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("제출하기")
        }
    }
}

@Composable
fun AccommodationPreferenceScreen(navController: NavHostController) {
    // 질문 1: 숙소 스타일 (한 개 선택)
    val accommodationStyles = listOf(
        "가성비 숙소 (최대한 저렴하게)",
        "기본 호텔 (무난하고 깔끔한 곳)",
        "고급 호텔 (뷰 & 조식 필수)",
        "자연 속 숙소 (펜션, 한옥, 전원주택)",
        "온천/스파 포함 숙소",
        "모르겠음 (추천받기)"
    )
    val selectedAccommodationStyle = remember { mutableStateOf("") }

    // 질문 2: 숙소 위치 (한 개 선택)
    val accommodationLocations = listOf(
        "관광지에서 가까운 곳 (도보 이동 가능)",
        "시내 중심 (대중교통 편리)",
        "한적한 곳 (자연 속 숙소)",
        "숙소 위치는 상관없음, 추천해줘"
    )
    val selectedAccommodationLocation = remember { mutableStateOf("") }

    // 질문 3: 이동 거리 (한 개 선택)
    val travelDistances = listOf(
        "걸어서 이동 가능해야 함 (10분 이내)",
        "차로 10~20분 정도",
        "30분 이상 이동 가능",
        "이동 거리 상관없음"
    )
    val selectedTravelDistance = remember { mutableStateOf("") }

    // 질문 4: 부가 시설 선택 (필요하면 선택; 다중 선택)
    val additionalFacilities = listOf(
        "주차장 필수 (자가용 이용)",
        "반려동물 동반 가능해야 함",
        "휠체어 이동 가능해야 함",
        "야외 테라스/바베큐 가능",
        "조용한 숙소 (가족 단위)",
        "상관없음"
    )
    val selectedAdditionalFacilities = remember { mutableStateListOf<String>() }

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
            text = "숙소 취향 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 질문 1: 숙소 스타일 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1️⃣ 숙소 스타일 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                accommodationStyles.forEach { style ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedAccommodationStyle.value == style,
                            onClick = { selectedAccommodationStyle.value = style }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = style)
                    }
                }
            }
        }

        // 질문 2: 숙소 위치 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2️⃣ 숙소 위치 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                accommodationLocations.forEach { location ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedAccommodationLocation.value == location,
                            onClick = { selectedAccommodationLocation.value = location }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = location)
                    }
                }
            }
        }

        // 질문 3: 이동 거리 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3️⃣ 이동 거리 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                travelDistances.forEach { distance ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedTravelDistance.value == distance,
                            onClick = { selectedTravelDistance.value = distance }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = distance)
                    }
                }
            }
        }

        // 질문 4: 부가 시설 선택 (다중 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4️⃣ 부가 시설 선택 (필요하면 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                additionalFacilities.forEach { facility ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        val checked = facility in selectedAdditionalFacilities
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedAdditionalFacilities.add(facility)
                                } else {
                                    selectedAdditionalFacilities.remove(facility)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = facility)
                    }
                }
            }
        }

        // 제출 버튼
        Button(
            onClick = {
                // 선택 결과 디버깅 로그 출력
                println("숙소 스타일: ${selectedAccommodationStyle.value}")
                println("숙소 위치: ${selectedAccommodationLocation.value}")
                println("이동 거리: ${selectedTravelDistance.value}")
                println("부가 시설: $selectedAdditionalFacilities")
                // 결과 데이터를 다음 화면 또는 추천 로직에 전달
                navController.navigate("loading/recommendAccommodation")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("제출하기")
        }
    }
}

@Composable
fun RestaurantPreferenceScreen(navController: NavHostController) {
    // 질문 1: 식사 스타일 (최대 2개 선택)
    val mealStyles = listOf(
        "현지 맛집 (지역 특산물)",
        "프랜차이즈 (무난한 선택)",
        "가성비 좋은 식당 (싸고 푸짐)",
        "고급 레스토랑 (특별한 경험)",
        "카페식 가벼운 식사 (샐러드, 브런치)",
        "모르겠음 (추천받기)"
    )
    val selectedMealStyles = remember { mutableStateListOf<String>() }

    // 질문 2: 식당 위치 (한 개 선택)
    val restaurantLocations = listOf(
        "관광지 근처 식당 (이동 적게)",
        "숙소 주변 식당 (편하게 먹고 쉬기)",
        "맛집이라면 멀어도 OK",
        "아무 데나 추천해줘"
    )
    val selectedRestaurantLocation = remember { mutableStateOf("") }

    // 질문 3: 대기 시간 고려 (한 개 선택)
    val waitingTimeOptions = listOf(
        "줄 서는 핫플 가능",
        "한적한 식당이 좋음",
        "상관없음"
    )
    val selectedWaitingTimeOption = remember { mutableStateOf("") }

    // 질문 4: 특수 조건 (필요하면 선택, 다중 선택)
    val specialConditions = listOf(
        "반려동물 동반 가능해야 함",
        "아이 동반 가능 (놀이 공간 있음)",
        "노약자에게 편한 자리 (의자, 접근성)",
        "예약 필수 식당 선호",
        "상관없음"
    )
    val selectedSpecialConditions = remember { mutableStateListOf<String>() }

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
            text = "식당 취향 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 질문 1: 식사 스타일 (최대 2개 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1️⃣ 식사 스타일 (최대 2개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                mealStyles.forEach { style ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        val checked = style in selectedMealStyles
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    if (selectedMealStyles.size < 2) {
                                        selectedMealStyles.add(style)
                                    }
                                    // 이미 2개 선택되었다면 추가 선택은 무시하거나 토스트로 안내할 수 있습니다.
                                } else {
                                    selectedMealStyles.remove(style)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = style)
                    }
                }
            }
        }

        // 질문 2: 식당 위치 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2️⃣ 식당 위치 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                restaurantLocations.forEach { location ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedRestaurantLocation.value == location,
                            onClick = { selectedRestaurantLocation.value = location }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = location)
                    }
                }
            }
        }

        // 질문 3: 대기 시간 고려 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3️⃣ 대기 시간 고려 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                waitingTimeOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedWaitingTimeOption.value == option,
                            onClick = { selectedWaitingTimeOption.value = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        }

        // 질문 4: 특수 조건 (필요하면 선택, 다중 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4️⃣ 특수 조건 (필요하면 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                specialConditions.forEach { condition ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        val checked = condition in selectedSpecialConditions
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedSpecialConditions.add(condition)
                                } else {
                                    selectedSpecialConditions.remove(condition)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = condition)
                    }
                }
            }
        }

        // 제출 버튼
        Button(
            onClick = {
                // 선택된 결과 확인 (디버깅 또는 데이터 전달용)
                println("식사 스타일: $selectedMealStyles")
                println("식당 위치: ${selectedRestaurantLocation.value}")
                println("대기 시간 고려: ${selectedWaitingTimeOption.value}")
                println("특수 조건: $selectedSpecialConditions")
                // 선택 데이터를 다음 화면이나 추천 로직에 전달합니다.
                navController.navigate("loading/recommendRestaurants")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("제출하기")
        }
    }
}

@Composable
fun TravelTimePreferenceScreen(navController: NavHostController) {
    // 질문 1: 여행 시작 시간 (한 개 선택)
    val startTimeOptions = listOf(
        "아침 일찍 (7~8시)",
        "보통 아침 (9~10시)",
        "여유롭게 늦게 출발 (11~12시 이후)"
    )
    val selectedStartTime = remember { mutableStateOf("") }

    // 질문 2: 여행 종료 시간 (한 개 선택)
    val endTimeOptions = listOf(
        "저녁 6~7시쯤 (조금 일찍 끝내기)",
        "밤 8~9시쯤 (적당한 마무리)",
        "늦게까지 가능 (야경, 밤 코스 포함)"
    )
    val selectedEndTime = remember { mutableStateOf("") }

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
            text = "여행 시간 설정",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 질문 1: 여행 시작 시간 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1️⃣ 여행 시작 시간 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                startTimeOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedStartTime.value == option,
                            onClick = { selectedStartTime.value = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        }

        // 질문 2: 여행 종료 시간 (단일 선택)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2️⃣ 여행 종료 시간 (한 개 선택)", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                endTimeOptions.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedEndTime.value == option,
                            onClick = { selectedEndTime.value = option }
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
                // 선택된 결과 확인 (디버깅 혹은 데이터 전달용)
                println("여행 시작 시간: ${selectedStartTime.value}")
                println("여행 종료 시간: ${selectedEndTime.value}")
                // 다음 화면 또는 추천 로직으로 내비게이션
                navController.navigate("loading/travelPlan")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("최종 여행 일정 보기")
        }
    }
}