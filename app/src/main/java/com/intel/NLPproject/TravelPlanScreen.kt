package com.intel.NLPproject

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.firebase.AccommodationDatabase
import com.intel.NLPproject.firebase.AttractionDatabase
import com.intel.NLPproject.firebase.RestaurantDatabase

@Composable
fun TravelPlanScreen(navController: NavHostController) {
    val context = LocalContext.current
    // TokenManager를 통해 현재 사용자 UID 가져오기
    val userId = TokenManager.getCurrentUserId(context) ?: "defaultUserId"

    // 데이터 상태 변수
    var attractions by remember { mutableStateOf<List<String>>(emptyList()) }
    var accommodations by remember { mutableStateOf<List<String>>(emptyList()) }
    var restaurants by remember { mutableStateOf<List<String>>(emptyList()) }

    // 선택 및 설명 다이얼로그 상태 관리
    val selectedAttractions = remember { mutableStateListOf<String>() }
    var currentAttractionDescription by remember { mutableStateOf("") }
    var showAttractionDialog by remember { mutableStateOf(false) }

    val selectedAccommodations = remember { mutableStateListOf<String>() }
    var currentAccommodationDescription by remember { mutableStateOf("") }
    var showAccommodationDialog by remember { mutableStateOf(false) }

    val selectedRestaurants = remember { mutableStateListOf<String>() }
    var currentRestaurantDescription by remember { mutableStateOf("") }
    var showRestaurantDialog by remember { mutableStateOf(false) }
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )


    // 데이터베이스에서 데이터 불러오기
    LaunchedEffect(userId) {
        AttractionDatabase.getAttractions(userId) { list ->
            attractions = list ?: emptyList()
        }
        AccommodationDatabase.getAccommodations(userId) { list ->
            accommodations = list ?: emptyList()
        }
        RestaurantDatabase.getRestaurants(userId) { list ->
            restaurants = list ?: emptyList()
        }
    }

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
                "최종 선택지",
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
                .height(550.dp)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 관광지 섹션
            DataGridSection(
                title = "관광지",
                data = attractions,
                selectedItems = selectedAttractions,
                onIconClick = { item ->
                    currentAttractionDescription = item
                    showAttractionDialog = true
                }
            )

            // 숙소 섹션
            DataGridSection(
                title = "숙소",
                data = accommodations,
                selectedItems = selectedAccommodations,
                onIconClick = { item ->
                    currentAccommodationDescription = item
                    showAccommodationDialog = true
                }
            )

            // 식당 섹션
            DataGridSection(
                title = "식당",
                data = restaurants,
                selectedItems = selectedRestaurants,
                onIconClick = { item ->
                    currentRestaurantDescription = item
                    showRestaurantDialog = true
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val bookingUrl = "https://www.letskorail.com/" // 코레일 예매 URL로 변경
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingUrl))
                context.startActivity(intent)
            }) {
                Text("코레일 예매")
            }

            Button(
                onClick = {
                    navController.navigate("detailPlan")
                }
            ) {
                Text("일정 확인")
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    navController.navigate("first")
                }
            ) {
                Text("메인화면으로")
            }
            Button(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text("뒤로가기")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DataGridSection(
    title: String,
    data: List<String>,
    selectedItems: MutableList<String>,
    onIconClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(modifier = modifier) {
        Text(
            title,
            fontSize = 28.sp,
            fontFamily = myFontFamily,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (data.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("저장된 $title 정보가 없습니다.", fontFamily = myFontFamily)
            }
        } else {
            // BoxWithConstraints를 이용하여 사용 가능한 너비를 계산합니다.
            BoxWithConstraints {
                val columns = 2
                val horizontalPadding = 32.dp  // Column의 양쪽 패딩 (16.dp * 2)
                val spacing = 16.dp             // LazyVerticalGrid의 간격
                // 사용 가능한 너비에서 패딩과 간격을 빼서 아이템 너비를 구함
                val itemWidth = (maxWidth - horizontalPadding - spacing) / columns
                // 아이템은 정사각형이므로 높이도 itemWidth
                val rowCount = kotlin.math.ceil(data.size / columns.toFloat()).toInt()
                // 전체 그리드 높이: 각 행의 높이(itemWidth) + 행 사이 간격(행수 - 1)
                val gridHeight = itemWidth * rowCount + spacing * (rowCount - 1) + 16.dp // 추가 여백

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    userScrollEnabled = false, // 내부 스크롤 비활성화
                    modifier = Modifier
                        .height(gridHeight)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    items(data.size) { index ->
                        val item = data[index]
                        val isSelected = selectedItems.contains(item)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
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
                                Text(text = item)
                                IconButton(
                                    onClick = { onIconClick(item) },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "$title 설명 보기"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
