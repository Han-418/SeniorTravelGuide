package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserId
import com.intel.NLPproject.api.Recommendation
import com.intel.NLPproject.api.RetrofitClient
import com.intel.NLPproject.firebase.AttractionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import coil.compose.AsyncImage // 네트워크 이미지를 로딩하기 위한 Coil 라이브러리 (프로젝트에 추가되어 있어야 함)

@Composable
fun RecommendAttractionScreen(navController: NavHostController, taskId: String) {
    // 서버로부터 받은 추천 결과 (Recommendation: attraction_name, average_sentiment_score, short_review, image_url)
    var recommendations by remember { mutableStateOf<List<Recommendation>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var pollingCount by remember { mutableStateOf(0) }
    val maxPollingAttempts = 20           // 최대 폴링 횟수
    val pollingIntervalMs = 5000L         // 폴링 주기 (1초=1000L)
    val coroutineScope = rememberCoroutineScope()

    // 선택된 추천 항목 (다중 선택 가능)
    val selectedRecommendations = remember { mutableStateListOf<Recommendation>() }
    // 상세 설명 다이얼로그를 위한 현재 선택 Recommendation
    var currentRecommendation by remember { mutableStateOf<Recommendation?>(null) }
    var showDescriptionDialog by remember { mutableStateOf(false) }

    // 폴링 로직: taskId를 사용하여 서버에서 작업 결과를 받아옴.
    LaunchedEffect(taskId) {
        fun pollTaskStatus() {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.cloudApiService.getTaskStatus(taskId)
                    if (response.isSuccessful && response.code() == 200) {
                        val body = response.body()
                        recommendations = body?.recommendations ?: emptyList()
                        isLoading = false
                    } else if (response.code() == 202) {
                        // 작업 진행 중: 재시도
                        if (pollingCount < maxPollingAttempts) {
                            pollingCount++
                            delay(pollingIntervalMs)
                            pollTaskStatus() // 재귀 호출
                        } else {
                            isLoading = false
                        }
                    } else {
                        isLoading = false
                    }
                } catch (e: Exception) {
                    isLoading = false
                    println("Retrofit Error: ${e.message}")
                }
            }
        }
        pollTaskStatus()
    }

    // 기존의 폰트 설정
    val myFontFamily = FontFamily(Font(R.font.notoserifkrblack))
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
                "관광지 추천 목록",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }

        // 로딩 중이면 진행 표시기, 데이터가 없으면 메시지, 데이터가 있으면 추천 목록 표시
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (recommendations.isEmpty()) {
                Text("추천 결과가 없습니다.")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(550.dp)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(recommendations.size) { index ->
                        val rec = recommendations[index]
                        // 각 카드 클릭 시 선택 토글 (selectedRecommendations)와 상세보기 다이얼로그 열기
                        val isSelected = selectedRecommendations.contains(rec)
                        Column {
                            Text(
                                text = rec.attraction_name,
                                fontFamily = myFontFamily,
                                fontSize = 17.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clickable {
                                        if (isSelected) {
                                            selectedRecommendations.remove(rec)
                                        } else {
                                            selectedRecommendations.add(rec)
                                        }
                                    }
                                    .border(
                                        width = 3.dp,
                                        color = if (isSelected) Color(0xFFF20574) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                elevation = if (isSelected)
                                    CardDefaults.cardElevation(defaultElevation = 8.dp)
                                else CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(if (isSelected) Color.LightGray else Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // 네트워크 이미지 로딩: 서버에서 받은 image_url 사용 (실패 시 placeholder 처리)
                                    AsyncImage(
                                        model = rec.image_url,
                                        contentDescription = rec.attraction_name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    // 우측 상단 아이콘 버튼: 상세 설명 다이얼로그 표시
                                    IconButton(
                                        onClick = {
                                            currentRecommendation = rec
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
                }
            }
        }

        // 하단 버튼 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = { navController.navigate("attractionPreference") },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    "못고르겠어요",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
            Button(
                onClick = {
                    val currentUserId = getCurrentUserId(context)
                    if (currentUserId != null) {
                        if (selectedRecommendations.isNotEmpty()) {
                            // 서버에서 받은 추천 결과의 attraction_name 리스트 저장
                            AttractionDatabase.saveAttractions(
                                currentUserId,
                                selectedRecommendations.map { it.attraction_name }
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
                },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    "관광지 확정",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
                    .clickable { navController.popBackStack() }
            )
            Button(
                onClick = { navController.navigate("first") },
                modifier = Modifier
                    .width(160.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    text = "메인화면으로",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 상세 설명 다이얼로그 (AlertDialog)
        currentRecommendation?.let { rec ->
            if (showDescriptionDialog) {
                AlertDialog(
                    onDismissRequest = { showDescriptionDialog = false },
                    title = {
                        Text(rec.attraction_name, fontSize = 30.sp, fontFamily = myFontFamily)
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .height(500.dp)
                                .padding(top = 8.dp, bottom = 2.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // 상단 이미지 영역 (네트워크 이미지 사용)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            ) {
                                AsyncImage(
                                    model = rec.image_url,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            // 중간 정보 영역: 위치, 평점, 날씨 등 (일부는 고정 텍스트)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.3f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth(0.2f),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.location),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .offset(x = (-10).dp)
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth(0.8f),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                rec.address,
                                                fontFamily = myFontFamily,
                                                fontSize = 17.sp,
                                                lineHeight = 19.sp
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Divider(thickness = 1.dp, color = Color.Black)
                                Spacer(modifier = Modifier.height(5.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.7f),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(130.dp)
                                            .fillMaxHeight(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text("평점", fontFamily = myFontFamily, fontSize = 22.sp)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text("${rec.average_sentiment_score} / 10", fontSize = 22.sp, fontFamily = myFontFamily)
                                    }
                                    Column(
                                        modifier = Modifier
                                            .width(130.dp)
                                            .fillMaxHeight(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text("날씨", fontFamily = myFontFamily, fontSize = 22.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(R.drawable.wind2),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(200.dp)
                                                .offset(x = 5.dp, y = 5.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            // 하단 설명 영역: 서버에서 받은 short_review 출력
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(125.dp)
                                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    rec.short_review,
                                    fontSize = 18.sp,
                                    fontFamily = myFontFamily,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.8.sp,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .padding(start = 2.dp, end = 2.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showDescriptionDialog = false }) {
                            Text("닫기")
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 2.dp, bottom = 2.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}
