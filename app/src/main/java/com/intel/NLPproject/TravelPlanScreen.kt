package com.intel.NLPproject

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserId
import com.intel.NLPproject.firebase.AccommodationDatabase
import com.intel.NLPproject.firebase.AttractionDatabase
import com.intel.NLPproject.firebase.RestaurantDatabase

@Composable
fun TravelPlanScreen(navController: NavHostController) {
    val listOfAll = listOf(
        // 관광지
        "카멜리아 힐", "아쿠아플라넷 제주", "한라산", "새별오름", "함덕 해수욕장",
        "용눈이오름", "큰엉해안경승지", "휴애리자연생활공원",
        // 숙소
        "에가톳 캐빈", "머큐어앰배서더 제주", "유탑유블레스호텔제주", "라마다 제주함덕호텔",
        // 식당
        "갈치옥 함덕", "백가네 제주 한상", "터틀리애", "동백국수"
    )
    val listOfAllImages = mapOf(
        // 관광지
        "카멜리아 힐" to R.drawable.camelliahill,
        "아쿠아플라넷 제주" to R.drawable.aquaplanet,
        "한라산" to R.drawable.hallamount,
        "새별오름" to R.drawable.saebyul,
        "함덕 해수욕장" to R.drawable.hamduk,
        "용눈이오름" to R.drawable.yongnoone,
        "큰엉해안경승지" to R.drawable.keunung,
        "휴애리자연생활공원" to R.drawable.hueree,
        // 숙소
        "에가톳 캐빈" to R.drawable.egattot,
        "머큐어앰배서더 제주" to R.drawable.mercurejeju,
        "유탑유블레스호텔제주" to R.drawable.youtop,
        "라마다 제주함덕호텔" to R.drawable.ramada,
        // 식당
        "갈치옥 함덕" to R.drawable.galchiok,
        "백가네 제주 한상" to R.drawable.backga,
        "터틀리애" to R.drawable.turtleliae,
        "동백국수" to R.drawable.dongback
    )
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    navController.navigate("detailPlan")
                },
                modifier = Modifier
                    .width(330.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(19.dp)
            ) {
                Text(
                    "일정 확인",
                    fontSize = 19.sp,
                    fontFamily = myFontFamily,
                    modifier = Modifier.offset(y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
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
                    navController.navigate("first")
                },
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
    val listOfAll = listOf(
        // 관광지
        "카멜리아 힐", "아쿠아플라넷 제주", "한라산", "새별오름", "함덕 해수욕장",
        "용눈이오름", "큰엉해안경승지", "휴애리자연생활공원",
        // 숙소
        "에가톳 캐빈", "머큐어앰배서더 제주", "유탑유블레스호텔", "라마다 제주함덕호텔",
        // 식당
        "갈치옥 함덕", "백가네 제주 한상", "터틀리애", "동백국수"
    )
    val listOfAllImages = mapOf(
        // 관광지
        "카멜리아 힐" to R.drawable.camelliahill,
        "아쿠아플라넷 제주" to R.drawable.aquaplanet,
        "한라산" to R.drawable.hallamount,
        "새별오름" to R.drawable.saebyul,
        "함덕 해수욕장" to R.drawable.hamduk,
        "용눈이오름" to R.drawable.yongnoone,
        "큰엉해안경승지" to R.drawable.keunung,
        "휴애리자연생활공원" to R.drawable.hueree,
        // 숙소
        "에가톳 캐빈" to R.drawable.egattot,
        "머큐어앰배서더 제주" to R.drawable.mercurejeju,
        "유탑유블레스호텔" to R.drawable.youtop,
        "라마다 제주함덕호텔" to R.drawable.ramada,
        // 식당
        "갈치옥 함덕" to R.drawable.galchiok,
        "백가네 제주 한상" to R.drawable.backga,
        "터틀리애" to R.drawable.turtleliae,
        "동백국수" to R.drawable.dongback
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
                val itemWidth = (maxWidth - horizontalPadding - spacing) / columns

                // 텍스트와 Spacer의 추가 높이 (필요에 따라 조절)
                val extraHeight = 30.dp

                // 각 아이템의 전체 높이는 카드의 높이(itemWidth) + extraHeight
                val rowCount = kotlin.math.ceil(data.size / columns.toFloat()).toInt()
                val gridHeight =
                    (itemWidth + extraHeight) * rowCount + spacing * (rowCount - 1) + 16.dp // 추가 여백

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    userScrollEnabled = false,
                    modifier = Modifier
                        .height(gridHeight)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    items(data.size) { index ->
                        val item = data[index]
                        val isSelected = selectedItems.contains(item)
                        Column {
                            Text(
                                text = item,
                                fontFamily = myFontFamily,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
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
                                    Image(
                                        painter = painterResource(
                                            id = listOfAllImages[item] ?: R.drawable.backlogo
                                        ),
                                        contentDescription = item,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
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