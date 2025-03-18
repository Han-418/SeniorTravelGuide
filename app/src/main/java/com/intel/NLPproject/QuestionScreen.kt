package com.intel.NLPproject

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate
import java.util.Calendar

@Composable
fun QuestionScreen(navController: NavHostController) {
    // 각 질문별 옵션 목록
    val destinationOptions = listOf(
        "경상도",
        "강원도",
        "제주도",
        "전라도",
        "충청도"
    )
    val companionOptions = listOf(
        "혼자",
        "부부/커플",
        "가족들과 함께",
        "부모님과 함께",
        "친구들과 함께"
    )
    val transportationOptions = listOf(
        "자가용",
        "기차",
        "버스",
        "비행기",
        "상관없음"
    )
    val budgetOptions = listOf(
        "저렴하게",
        "평균적",
        "상관없음"
    )
    val subregionOptionsMap = mapOf(
        "경상도" to listOf("부산", "대구", "울산", "경주", "안동", "AI 추천"),
        "강원도" to listOf("강릉", "속초", "원주", "동해", "양양", "AI 추천"),
        "제주도" to listOf("제주시", "서귀포시", "애월읍", "우도면", "성산읍", "AI 추천"),
        "전라도" to listOf("전주", "광주", "순천", "여수", "목포", "AI 추천"),
        "충청도" to listOf("대전", "청주", "천안", "논산", "보령", "AI 추천")
    )

    // 각 질문의 선택 상태
    // 선택된 도착지(대분류)와 소분류
    var selectedDestination by remember { mutableStateOf("") }
    var selectedSubregion by remember { mutableStateOf("") }
    var selectedCompanion by remember { mutableStateOf("") }
    var selectedTransportation by remember { mutableStateOf("") }
    var selectedBudget by remember { mutableStateOf("") }
    val selectedDeparture = remember { mutableStateOf<LocalDate?>(null) }
    val selectedReturn = remember { mutableStateOf<LocalDate?>(null) }

    // 직접 입력 다이얼로그를 위한 상태들
    var customDestinationText by remember { mutableStateOf("") } // 버튼에 표시될 텍스트
    var customCompanionText by remember { mutableStateOf("") }
    var customTransportationInput by remember { mutableStateOf("") }
    var customBudgetText by remember { mutableStateOf("") }
    var showCustomInputDialog by remember { mutableStateOf(false) }
    var customDestinationInput by remember { mutableStateOf("") }
    var customCompanionInput by remember { mutableStateOf("") }
    var customTransportationText by remember { mutableStateOf("") }
    var customBudgetInput by remember { mutableStateOf("") }

    val context = LocalContext.current
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )
    var currentStep by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 헤더 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .background(color = Color(0xFFFFA700)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = when (currentStep) {
                    1 -> "어디로 여행을 떠나시나요?"
                    2 -> "여행 기간이 어떻게 되시나요?"
                    3 -> "누구와 함께 가시나요?"
                    4 -> "이동수단은 어떻게 되시나요?"
                    5 -> "예산은 어떻게 생각하세요?"
                    else -> ""
                },
                fontSize = 26.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(570.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentStep) {

                // destination
                1 -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showCustomInputDialog = true },
                        modifier = Modifier
                            .width(300.dp)
                            .height(45.dp)
                            .shadow(elevation = 8.dp, spotColor = Color(0x0D000000)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = if (customDestinationText.isEmpty()) "직접 입력하기" else customDestinationText,
                                fontSize = 20.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(530.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // 1) 대분류 목록 표시
                        destinationOptions.forEach { destination ->
                            DestinationItem(
                                text = destination,
                                isSelected = (selectedDestination == destination),
                                onClick = {
                                    selectedDestination = destination
                                    // 대분류가 바뀌면 소분류 선택값 초기화
                                    selectedSubregion = ""
                                },
                                myFontFamily = myFontFamily
                            )
                            // 선택된 대분류 아래에 세부 옵션 그리드 표시
                            if (selectedDestination == destination) {
                                val subregions = subregionOptionsMap[destination] ?: emptyList()
                                SubregionGrid(
                                    subregions = subregions,
                                    selectedSubregion = selectedSubregion,
                                    onSubregionClick = { selectedSubregion = it }
                                )
                            }
                        }

                        // 직접 입력 다이얼로그 표시
                        if (showCustomInputDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    showCustomInputDialog = false
                                    // 취소 시 선택값 초기화
                                    selectedDestination = ""
                                    customDestinationInput = ""
                                },
                                title = { Text("목적지 직접 입력") },
                                text = {
                                    Column {
                                        Text("여행 가고 싶은 곳을 입력하세요")
                                        OutlinedTextField(
                                            value = customDestinationInput,
                                            onValueChange = { customDestinationInput = it },
                                            placeholder = { Text("예: 서울, 부산, 제주 등") }
                                        )
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            // 텍스트 필드에서 입력한 값으로 업데이트 후 다이얼로그 닫기
                                            selectedDestination = customDestinationInput
                                            customDestinationText = customDestinationInput
                                            showCustomInputDialog = false
                                            customDestinationInput = ""
                                        }
                                    ) {
                                        Text("확인")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showCustomInputDialog = false
                                            // 취소 시 선택값 초기화
                                            selectedDestination = ""
                                            customDestinationInput = ""
                                        }
                                    ) {
                                        Text("취소")
                                    }
                                }
                            )
                        }
                    }
                }

                // departure / return
                2 -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TravelPeriodQuestion(
                        question = "여행 기간은 어떻게 되시나요?",
                        selectedDeparture = selectedDeparture,
                        selectedReturn = selectedReturn
                    )
                }

                // companion
                3 -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showCustomInputDialog = true },
                        modifier = Modifier
                            .width(300.dp)
                            .height(45.dp)
                            .shadow(elevation = 8.dp, spotColor = Color(0x0D000000)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // customCompanionText가 비어있으면 "직접 입력하기", 값이 있으면 그 값
                            Text(
                                text = if (customCompanionText.isEmpty()) "직접 입력하기" else customCompanionText,
                                fontSize = 20.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(530.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // 동행자 기본 옵션 표시
                        companionOptions.forEach { option ->
                            DestinationItem(
                                text = option,
                                isSelected = (selectedCompanion == option),
                                onClick = { selectedCompanion = option },
                                myFontFamily = myFontFamily
                            )
                        }

                        // 직접 입력 다이얼로그
                        if (showCustomInputDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    showCustomInputDialog = false
                                    // 취소 시 선택값 초기화
                                    selectedCompanion = ""
                                    customCompanionInput = ""
                                },
                                title = { Text("동행자 직접 입력") },
                                text = {
                                    Column {
                                        Text("함께 가고 싶은 사람을 입력하세요")
                                        OutlinedTextField(
                                            value = customCompanionInput,
                                            onValueChange = { customCompanionInput = it },
                                            placeholder = { Text("예: 친구들, 가족 등") }
                                        )
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            // 입력한 값으로 선택 업데이트 후 다이얼로그 닫기
                                            selectedCompanion = customCompanionInput
                                            customCompanionText = customCompanionInput
                                            showCustomInputDialog = false
                                            customCompanionInput = ""
                                        }
                                    ) {
                                        Text("확인")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showCustomInputDialog = false
                                            // 취소 시 선택값 초기화
                                            selectedCompanion = ""
                                            customCompanionInput = ""
                                        }
                                    ) {
                                        Text("취소")
                                    }
                                }
                            )
                        }
                    }
                }

                // transportation
                4 -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showCustomInputDialog = true },
                        modifier = Modifier
                            .width(300.dp)
                            .height(45.dp)
                            .shadow(elevation = 8.dp, spotColor = Color(0x0D000000)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = if (customTransportationText.isEmpty()) "직접 입력하기" else customTransportationText,
                                fontSize = 20.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(530.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // 교통수단 기본 옵션 표시
                        transportationOptions.forEach { option ->
                            DestinationItem(
                                text = option,
                                isSelected = (selectedTransportation == option),
                                onClick = { selectedTransportation = option },
                                myFontFamily = myFontFamily
                            )
                        }

                        // 직접 입력 다이얼로그
                        if (showCustomInputDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    showCustomInputDialog = false
                                    // 취소 시 선택값 초기화
                                    selectedTransportation = ""
                                    customTransportationInput = ""
                                },
                                title = { Text("교통수단 직접 입력") },
                                text = {
                                    Column {
                                        Text("원하는 교통수단을 입력하세요")
                                        OutlinedTextField(
                                            value = customTransportationInput,
                                            onValueChange = { customTransportationInput = it },
                                            placeholder = { Text("예: 자가용, 기차, 버스 등") }
                                        )
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            // 입력한 값으로 선택 업데이트 후 다이얼로그 닫기
                                            selectedTransportation = customTransportationInput
                                            customTransportationText = customTransportationInput
                                            showCustomInputDialog = false
                                            customTransportationInput = ""
                                        }
                                    ) {
                                        Text("확인")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showCustomInputDialog = false
                                            // 취소 시 선택값 초기화
                                            selectedTransportation = ""
                                            customTransportationInput = ""
                                        }
                                    ) {
                                        Text("취소")
                                    }
                                }
                            )
                        }
                    }
                }

                // budget
                5 -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF0DBBCA))
                                .height(5.dp)
                                .width(61.dp)
                        ) { }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showCustomInputDialog = true },
                        modifier = Modifier
                            .width(300.dp)
                            .height(45.dp)
                            .shadow(elevation = 8.dp, spotColor = Color(0x0D000000)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFFFFF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = if (customBudgetText.isEmpty()) "직접 입력하기" else customBudgetText,
                                fontSize = 20.sp,
                                fontFamily = myFontFamily,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(530.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // 예산 기본 옵션 표시
                        budgetOptions.forEach { option ->
                            DestinationItem(
                                text = option,
                                isSelected = (selectedBudget == option),
                                onClick = { selectedBudget = option },
                                myFontFamily = myFontFamily
                            )
                        }

                        // 직접 입력 다이얼로그
                        if (showCustomInputDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    showCustomInputDialog = false
                                    // 취소 시 선택값 초기화
                                    selectedBudget = ""
                                    customBudgetInput = ""
                                },
                                title = { Text("예산 직접 입력") },
                                text = {
                                    Column {
                                        Text("원하는 예산을 입력하세요")
                                        OutlinedTextField(
                                            value = customBudgetInput,
                                            onValueChange = { customBudgetInput = it },
                                            placeholder = { Text("예: 저렴하게, 평균적, 상관없음 등") }
                                        )
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            // 입력한 값으로 선택 업데이트 후 다이얼로그 닫기
                                            selectedBudget = customBudgetInput
                                            customBudgetText = customBudgetInput
                                            showCustomInputDialog = false
                                            customBudgetInput = ""
                                        }
                                    ) {
                                        Text("확인")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showCustomInputDialog = false
                                            // 취소 시 선택값 초기화
                                            selectedBudget = ""
                                            customBudgetInput = ""
                                        }
                                    ) {
                                        Text("취소")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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
                        if (currentStep == 1) {
                            navController.popBackStack()
                        } else {
                            currentStep--
                        }
                    }
            )
            if (currentStep == 5) {
                // currentStep이 5인 경우: 예산 검증 후 다음 화면으로 이동하는 Button 표시
                Button(
                    onClick = {
                        if (selectedBudget.isEmpty()) {
                            Toast.makeText(context, "예산을 선택해주세요", Toast.LENGTH_SHORT).show()
                        } else {
                            navController.navigate("loading/recommendAttraction")
                        }
                    },
                    modifier = Modifier
                        .width(160.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(19.dp),
                ) {
                    Text(
                        text = "AI 추천 받기",
                        fontSize = 19.sp,
                        fontFamily = myFontFamily,
                        modifier = Modifier.offset(y = (-2).dp)
                    )
                }
            } else {
                // currentStep이 5가 아닌 경우: 기존 이미지 버튼으로 각 단계별 검증 후 currentStep 증가
                Image(
                    painter = painterResource(R.drawable.next),
                    contentDescription = "next question",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(160.dp)
                        .height(45.dp)
                        .clickable {
                            when (currentStep) {
                                1 -> {
                                    if (selectedDestination.isEmpty()) {
                                        Toast.makeText(context, "여행지를 선택해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        currentStep++
                                    }
                                }

                                2 -> {
                                    if (selectedDeparture.value == null || selectedReturn.value == null) {
                                        Toast.makeText(context, "여행 기간을 선택해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        currentStep++
                                    }
                                }

                                3 -> {
                                    if (selectedCompanion.isEmpty()) {
                                        Toast.makeText(context, "동행자를 선택해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        currentStep++
                                    }
                                }

                                4 -> {
                                    if (selectedTransportation.isEmpty()) {
                                        Toast.makeText(context, "교통수단을 선택해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        currentStep++
                                    }
                                }
                            }
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

// 대분류 옵션 버튼 Composable
@Composable
fun DestinationItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    myFontFamily: FontFamily
) {
    val borderColor = if (isSelected) Color(0xFFF20574) else Color.Transparent
    Button(
        onClick = onClick,
        modifier = Modifier
            .shadow(elevation = 8.dp, spotColor = Color(0x0D000000))
            .width(375.dp)
            .height(64.dp)
            .border(
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(4.dp)
            ),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFFFFFFF),
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            fontFamily = myFontFamily,
            fontSize = 21.sp,
            color = Color.Black
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

// 세부 옵션 그리드: 3열 × 2행 버튼 배치
@Composable
fun SubregionGrid(
    subregions: List<String>,
    selectedSubregion: String?,
    onSubregionClick: (String) -> Unit
) {
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        // 첫 번째 행 (인덱스 0~2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until 3) {
                if (i < subregions.size) {
                    Button(
                        onClick = { onSubregionClick(subregions[i]) },
                        modifier = Modifier
                            .width(110.dp)
                            .height(48.dp)
                            .then(
                                if (selectedSubregion == subregions[i])
                                    Modifier.border(
                                        border = BorderStroke(2.dp, Color(0xFFF20574)),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                else Modifier
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0DBBCA),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = subregions[i],
                            fontSize = 16.sp,
                            fontFamily = myFontFamily,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 두 번째 행 (인덱스 3~5)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 3 until 6) {
                if (i < subregions.size) {
                    Button(
                        onClick = { onSubregionClick(subregions[i]) },
                        modifier = Modifier
                            .width(110.dp)
                            .height(48.dp)
                            .then(
                                if (selectedSubregion == subregions[i])
                                    Modifier.border(
                                        border = BorderStroke(2.dp, Color(0xFFF20574)),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                else Modifier
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0DBBCA),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = subregions[i],
                            fontSize = 16.sp,
                            fontFamily = myFontFamily,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun TravelPeriodQuestion(
    question: String,
    selectedDeparture: MutableState<LocalDate?>,
    selectedReturn: MutableState<LocalDate?>
) {
    val context = LocalContext.current
    val showDeparturePicker = remember { mutableStateOf(false) }
    val showReturnPicker = remember { mutableStateOf(false) }
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(150.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("출발", fontFamily = myFontFamily, fontSize = 40.sp)
                Spacer(modifier = Modifier.height(68.dp))
                Button(
                    onClick = { showDeparturePicker.value = true },
                    modifier = Modifier
                        .padding(4.dp)
                        .height(45.dp)
                        .width(150.dp)
                ) {
                    Text(
                        text = selectedDeparture.value?.toString() ?: "날짜 선택",
                        fontSize = 18.sp,
                        fontFamily = myFontFamily,
                        color = Color.Black
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("도착", fontFamily = myFontFamily, fontSize = 40.sp)
                Spacer(modifier = Modifier.height(68.dp))
                Button(
                    onClick = { showReturnPicker.value = true },
                    modifier = Modifier
                        .padding(4.dp)
                        .height(45.dp)
                        .width(150.dp)
                ) {
                    Text(
                        text = selectedReturn.value?.toString() ?: "날짜 선택",
                        fontSize = 18.sp,
                        fontFamily = myFontFamily,
                        color = Color.Black
                    )
                }
            }
        }
    }

    // DatePickerDialog for 출발일
    if (showDeparturePicker.value) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.MyDatePickerDialogTheme,
            { _, year, month, dayOfMonth ->
                selectedDeparture.value = LocalDate.of(year, month + 1, dayOfMonth)
                showDeparturePicker.value = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener {
            showDeparturePicker.value = false
        }
        datePickerDialog.show()
    }

    // DatePickerDialog for 도착일
    if (showReturnPicker.value) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.MyDatePickerDialogTheme,
            { _, year, month, dayOfMonth ->
                selectedReturn.value = LocalDate.of(year, month + 1, dayOfMonth)
                showReturnPicker.value = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener {
            showReturnPicker.value = false
        }
        datePickerDialog.show()
    }
}