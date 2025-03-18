package com.intel.NLPproject

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate

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
        "가족들과",
        "부모님과",
        "친구들과"
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

    // 다른 질문 관련 상태들 (필요한 경우 사용)
    val selectedCompanion = remember { mutableStateOf("") }
    val selectedTransportation = remember { mutableStateOf("") }
    val selectedBudget = remember { mutableStateOf("") }

    // 직접 입력 다이얼로그를 위한 상태들
    var customDestinationText by remember { mutableStateOf("") } // 버튼에 표시될 텍스트
    var showCustomInputDialog by remember { mutableStateOf(false) }
    var customDestinationInput by remember { mutableStateOf("") }

    val selectedDeparture = remember { mutableStateOf<LocalDate?>(null) }
    val selectedReturn = remember { mutableStateOf<LocalDate?>(null) }
    val context = LocalContext.current
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

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
                .background(color = Color(0xFFFFA700))
        ) {
            Text("")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "직접 입력하기" 버튼 (버튼 텍스트는 customDestinationText가 비어있으면 "직접 입력하기", 값이 있으면 그 값으로 표시)
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(530.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

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
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = "back",
                modifier = Modifier
                    .width(160.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Image(
                painter = painterResource(R.drawable.next),
                contentDescription = "next question",
                modifier = Modifier
                    .width(160.dp)
                    .clickable {
                        // 다음 질문으로 이동하는 로직
                        if (selectedDestination.isEmpty()) {
                            Toast.makeText(context, "여행지를 선택해주세요", Toast.LENGTH_SHORT).show()
                        } else {
                            // 다음 질문으로 이동하는 로직

                        }
                    }
            )
        }
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
                        Text(text = subregions[i], fontSize = 16.sp, fontFamily = myFontFamily)
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
                        Text(text = subregions[i], fontSize = 16.sp, fontFamily = myFontFamily)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
