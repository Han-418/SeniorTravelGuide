package com.intel.NLPproject

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AttractionPreferenceScreen(navController: NavHostController) {
    // 여행 컨셉 옵션: 메인 옵션과 해당 세부 옵션
    val travelConceptMap = mapOf(
        "인기 핫플레이스" to listOf("액티브", "도심", "미식"),
        "한적하고 조용한 곳" to listOf("휴양형", "문화체험"),
        "상관없음 (추천)" to emptyList()
    )

    // 일정 및 활동량 옵션: 메인 옵션과 해당 세부 옵션
    val scheduleMap = mapOf(
        "유유자적 (조금만 돌아다님)" to listOf("중간중간 휴식", "일정 후 휴식"),
        "과유불급 (적당히 돌아다님)" to listOf("중간중간 휴식", "일정 후 휴식"),
        "동분서주 (많이 돌아다님)" to listOf("중간중간 휴식", "일정 후 휴식")
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
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFFFFA700)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                "여행 취향 설정",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [여행 컨셉] 섹션
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("여행 컨셉", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    // 메인 옵션: 한 줄씩 표시
                    travelConceptMap.keys.toList().forEach { option ->
                        val isSelected = (selectedTravelConcept == option)
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent

                        Button(
                            onClick = {
                                selectedTravelConcept = option
                                selectedTravelConceptDetail = ""
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        // 세부 옵션: 선택된 메인 옵션에 대해 Row로 표시
                        val subOptions = travelConceptMap[option] ?: emptyList()
                        if (selectedTravelConcept == option && subOptions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                subOptions.forEach { subOption ->
                                    val isSubSelected =
                                        (selectedTravelConceptDetail == subOption)
                                    val subBorderColor =
                                        if (isSubSelected) Color(0xFFF20574) else Color.Transparent
                                    Button(
                                        onClick = { selectedTravelConceptDetail = subOption },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .padding(vertical = 2.dp)
                                            .border(
                                                border = BorderStroke(2.dp, subBorderColor),
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF0DBBCA),
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Text(
                                            text = subOption,
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp,
                                            fontFamily = myFontFamily,
                                            color = Color.White,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // [일정 및 활동량] 섹션
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("하루 활동량", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    scheduleMap.keys.toList().forEach { option ->
                        val isSelected = (selectedSchedule == option)
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = {
                                selectedSchedule = option
                                selectedScheduleDetail = ""
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        val subOptions = scheduleMap[option] ?: emptyList()
                        if (selectedSchedule == option && subOptions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                subOptions.forEach { subOption ->
                                    val isSubSelected = (selectedScheduleDetail == subOption)
                                    val subBorderColor =
                                        if (isSubSelected) Color(0xFFF20574) else Color.Transparent
                                    Button(
                                        onClick = { selectedScheduleDetail = subOption },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .padding(vertical = 2.dp)
                                            .border(
                                                border = BorderStroke(2.dp, subBorderColor),
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF0DBBCA),
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Text(
                                            text = subOption,
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp,
                                            fontFamily = myFontFamily,
                                            color = Color.White,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // [특수 고려사항] 섹션 (멀티 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("특수사항 (필요 시)", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    specialConsiderations.forEach { option ->
                        val isSelected = selectedSpecialConsiderations.contains(option)
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = {
                                if (selectedSpecialConsiderations.contains(option)) {
                                    selectedSpecialConsiderations.remove(option)
                                } else {
                                    selectedSpecialConsiderations.add(option)
                                }
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "back",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
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
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "AI 추천 받기",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun AccommodationPreferenceScreen(navController: NavHostController) {
    // 숙소 스타일 옵션: 메인 옵션과 해당 세부 옵션
    val accommodationStyleMap = mapOf(
        "가성비" to listOf("게스트 하우스", "펜션", "모텔"),
        "고급" to listOf("호텔", "리조트", "럭셔리 빌라"),
        "특수" to listOf("한옥", "글램핑", "전원 주택")
    )
    // 숙소 위치 옵션 (단일 선택)
    val accommodationLocationOptions = listOf(
        "관광지 근처",
        "시내 중심",
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
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFFFFA700)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                "숙소 취향 설정",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [숙소 스타일] 섹션 (한 개 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("숙소 스타일", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    // 메인 옵션: 한 줄씩 표시
                    accommodationStyleMap.keys.toList().forEach { option ->
                        val isSelected = (selectedAccommodationStyle == option)
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent

                        val containerColor =
                            if (selectedAccommodationStyle == option) selectedColor else unselectedColor
                        Button(
                            onClick = {
                                selectedAccommodationStyle = option
                                selectedAccommodationStyleDetail = ""
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        // 세부 옵션: 선택된 메인 옵션에 대해 Row로 표시
                        val subOptions = accommodationStyleMap[option] ?: emptyList()
                        if (selectedAccommodationStyle == option && subOptions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                subOptions.forEach { subOption ->
                                    val isSubSelected =
                                        (selectedAccommodationStyleDetail == subOption)
                                    val subBorderColor =
                                        if (isSubSelected) Color(0xFFF20574) else Color.Transparent
                                    Button(
                                        onClick = { selectedAccommodationStyleDetail = subOption },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(60.dp)
                                            .padding(vertical = 2.dp)
                                            .border(
                                                border = BorderStroke(2.dp, subBorderColor),
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF0DBBCA),
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Text(
                                            text = subOption,
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp,
                                            fontFamily = myFontFamily,
                                            color = Color.White,
                                            modifier = Modifier.fillMaxWidth()
                                                .offset(y = (-1).dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // [숙소 위치] 섹션 (한 개 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("숙소 위치", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    accommodationLocationOptions.forEach { option ->
                        val isSelected = (selectedAccommodationLocation == option)
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = { selectedAccommodationLocation = option },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // [부가 시설 선택] 섹션 (멀티 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("부가 시설 (필요 시)", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    additionalFacilitiesOptions.forEach { option ->
                        val isSelected = selectedAdditionalFacilities.contains(option)
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = {
                                if (selectedAdditionalFacilities.contains(option)) {
                                    selectedAdditionalFacilities.remove(option)
                                } else {
                                    selectedAdditionalFacilities.add(option)
                                }
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "back",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Button(
                onClick = {
                    println(
                        "숙소 스타일: $selectedAccommodationStyle" +
                                if (selectedAccommodationStyleDetail.isNotEmpty()) " : $selectedAccommodationStyleDetail" else ""
                    )
                    println("숙소 위치: $selectedAccommodationLocation")
                    println("부가 시설: $selectedAdditionalFacilities")
                    navController.navigate("loading/recommendAccommodation")
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "AI 추천 받기",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
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
        "카페식 가벼운 식사",
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
        "반려동물 동반 가능",
        "아이 동반 가능 (놀이 공간)",
        "노약자에게 편한 자리",
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
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFFFFA700)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                "식당 취향 설정",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [식사 스타일] 섹션 (최대 2개 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("식사 스타일 (2개 선택)", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    mealStyleOptions.forEach { option ->
                        val isSelected = (selectedMealStyles.contains(option))
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = {
                                if (selectedMealStyles.contains(option)) {
                                    selectedMealStyles.remove(option)
                                } else {
                                    if (selectedMealStyles.size < 2) {
                                        selectedMealStyles.add(option)
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // [식당 위치] 섹션 (단일 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("식당 위치", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    restaurantLocationOptions.forEach { option ->
                        val isSelected = (selectedRestaurantLocation == option)
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = { selectedRestaurantLocation = option },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // [특수 조건] 섹션 (멀티 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("특수 조건 (필요 시)", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    specialConditionsOptions.forEach { option ->
                        val isSelected = selectedSpecialConditions.contains(option)
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = {
                                if (selectedSpecialConditions.contains(option)) {
                                    selectedSpecialConditions.remove(option)
                                } else {
                                    selectedSpecialConditions.add(option)
                                }
                            },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "back",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Button(
                onClick = {
                    println("식사 스타일: $selectedMealStyles")
                    println("식당 위치: $selectedRestaurantLocation")
                    println("특수 조건: $selectedSpecialConditions")
                    navController.navigate("loading/recommendRestaurants")
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "AI 추천 받기",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun TravelTimePreferenceScreen(navController: NavHostController) {
    // 여행 시작 시간 옵션 (단일 선택)
    val startTimeOptions = listOf(
        "오전 7 ~ 8 시",
        "오전 9 ~ 10 시",
        "오전 11 시 이후",
        "상관없음"
    )
    val selectedStartTime = remember { mutableStateOf("") }

    // 여행 종료 시간 옵션 (단일 선택)
    val endTimeOptions = listOf(
        "오후 6 ~ 7 시",
        "오후 8 ~ 9 시",
        "늦게까지 가능",
        "상관없음"
    )
    val selectedEndTime = remember { mutableStateOf("") }

    // 색상 설정: 미선택은 분홍색, 선택된 경우 회색 (예시)
    val unselectedColor = Color(0xFFFFC0CB) // 분홍색
    val selectedColor = Color.Gray
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFFFFA700)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                "여행 시간 설정",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // [여행 시작 시간] 섹션 (단일 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("여행 시작 시간", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    startTimeOptions.forEach { option ->
                        val isSelected = (selectedStartTime.value == option)
                        val borderColor =
                            if (isSelected) Color(0xFFF20574) else Color.Transparent
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = { selectedStartTime.value = option },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            // [여행 종료 시간] 섹션 (단일 선택)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("여행 종료 시간", fontSize = 28.sp, fontFamily = myFontFamily)
                    Spacer(modifier = Modifier.height(8.dp))
                    endTimeOptions.forEach { option ->
                        val isSelected = selectedEndTime.value == option
                        val borderColor2 =
                            if (isSelected) Color(0xFF3B00FF) else Color.Transparent
                        Button(
                            onClick = { selectedEndTime.value = option },
                            modifier = Modifier
                                .width(330.dp)
                                .height(49.dp)
                                .border(
                                    border = BorderStroke(3.dp, borderColor2),
                                    shape = RoundedCornerShape(25.dp)
                                ),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                fontSize = 21.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "back",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Button(
                onClick = {
                    println("여행 시작 시간: ${selectedStartTime.value}")
                    println("여행 종료 시간: ${selectedEndTime.value}")
                    navController.navigate("loading/travelPlan")
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "일정 보기",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}