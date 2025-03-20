package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserId
import com.intel.NLPproject.firebase.AttractionDatabase

// 데이터 클래스는 WebCrawlApiService.kt에 정의되어 있음
// data class WebCrawlResponse(val query: String, val results: List<CrawlResult>)
// data class CrawlResult(val title: String, val description: String)

@Composable
fun RecommendAttractionScreen(navController: NavHostController) {
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var currentDescription by remember { mutableStateOf("") }
    // 예시 관광지 추천 목록 (10개)
    val attractions = listOf(
//        "예시 1", "예시 2", "예시 3", "예시 4", "예시 5", "예시 6"
        "카멜리아 힐", "아쿠아플라넷 제주", "한라산", "새별오름", "함덕 해수욕장",
        "용눈이오름", "큰엉해안경승지", "휴애리자연생활공원"
    )
    val attractionImages = mapOf(
        "카멜리아 힐" to R.drawable.camelliahill,
        "아쿠아플라넷 제주" to R.drawable.aquaplanet,
        "한라산" to R.drawable.hallamount,
        "새별오름" to R.drawable.saebyul,
        "함덕 해수욕장" to R.drawable.hamduk,
        "용눈이오름" to R.drawable.yongnoone,
        "큰엉해안경승지" to R.drawable.keunung,
        "휴애리자연생활공원" to R.drawable.hueree
    )
    val context = LocalContext.current

    // 선택된 관광지를 저장할 상태 (다중 선택 가능)
    val selectedAttractions = remember { mutableStateListOf<String>() }
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
                "관광지 추천 목록",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .height(550.dp)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(attractions.size) { index ->
                val attraction = attractions[index]
                // 선택 상태 여부
                val isSelected = selectedAttractions.contains(attraction)
                Column {
                    Text(
                        text = attraction,
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
                                    selectedAttractions.remove(attraction)
                                } else {
                                    selectedAttractions.add(attraction)
                                }
                            }
                            .border(
                                width = 3.dp,
                                color = if (isSelected) Color(0xFFF20574) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        // 선택 상태일 때 테두리나 elevation을 달리 할 수 있음.
                        elevation = if (isSelected) CardDefaults.cardElevation(defaultElevation = 8.dp)
                        else CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (isSelected) Color.LightGray else Color.White),
                            contentAlignment = Alignment.Center
                        ) {
//                        Text(text = attraction)
                            Image(
                                painter = painterResource(
                                    id = attractionImages[attraction] ?: R.drawable.backlogo
                                ),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = {
                                    currentDescription = attraction
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    navController.navigate("attractionPreference")
                },
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
                        if (selectedAttractions.isNotEmpty()) {
                            AttractionDatabase.saveAttractions(
                                currentUserId,
                                selectedAttractions
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
        Spacer(modifier = Modifier.height(8.dp))
        // 관광지 설명 다이얼로그
        if (showDescriptionDialog) {
            AlertDialog(
                onDismissRequest = { showDescriptionDialog = false },
                title = { Text(currentDescription, fontSize = 30.sp, fontFamily = myFontFamily) },
                text = {
                    Column(
                        modifier = Modifier
                            .height(500.dp)
                            .padding(top = 8.dp, bottom = 2.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = painterResource(
                                    id = attractionImages[currentDescription] ?: R.drawable.backlogo
                                ),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
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
                                Row() {
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
                                            "제주도 제주시 조천읍 함덕리 산14-1",
                                            fontFamily = myFontFamily,
                                            fontSize = 17.sp,
                                            lineHeight = 19.sp
                                        )

                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Divider(
                                thickness = 1.dp,
                                color = Color.Black
                            )
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
                                    // 평점
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text("평점", fontFamily = myFontFamily, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("7.0 / 10", fontSize = 22.sp, fontFamily = myFontFamily)
                                }
                                Column(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    // 날씨
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "실외 시설이 갖춰진 함덕해수욕장은 부부와 함께하는 여행에 적합한 관광지입니다.",
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
                    TextButton(
                        onClick = { showDescriptionDialog = false }
                    ) {
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

@Composable
fun RecommendAttractionScreen2(navController: NavHostController) {
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var currentDescription by remember { mutableStateOf("") }
    // 예시 관광지 추천 목록 (10개)
    val attractions = listOf(
//        "예시 1", "예시 2", "예시 3", "예시 4", "예시 5", "예시 6"
        "카멜리아 힐", "아쿠아플라넷 제주", "새별오름", "함덕 해수욕장",
        "휴애리자연생활공원"
    )
    val attractionImages = mapOf(
        "카멜리아 힐" to R.drawable.camelliahill,
        "아쿠아플라넷 제주" to R.drawable.aquaplanet,
        "새별오름" to R.drawable.saebyul,
        "함덕 해수욕장" to R.drawable.hamduk,
        "휴애리자연생활공원" to R.drawable.hueree
    )

    val context = LocalContext.current

    // 선택된 관광지를 저장할 상태 (다중 선택 가능)
    val selectedAttractions = remember { mutableStateListOf<String>() }
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
                "관광지 추천 목록",
                fontSize = 28.sp,
                fontFamily = myFontFamily,
                color = Color.Black,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .height(550.dp)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(attractions.size) { index ->
                val attraction = attractions[index]
                // 선택 상태 여부
                val isSelected = selectedAttractions.contains(attraction)
                Column {
                    Text(
                        text = attraction,
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
                                    selectedAttractions.remove(attraction)
                                } else {
                                    selectedAttractions.add(attraction)
                                }
                            }
                            .border(
                                width = 3.dp,
                                color = if (isSelected) Color(0xFFF20574) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        // 선택 상태일 때 테두리나 elevation을 달리 할 수 있음.
                        elevation = if (isSelected) CardDefaults.cardElevation(defaultElevation = 8.dp)
                        else CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (isSelected) Color.LightGray else Color.White),
                            contentAlignment = Alignment.Center
                        ) {
//                        Text(text = attraction)
                            Image(
                                painter = painterResource(
                                    id = attractionImages[attraction] ?: R.drawable.backlogo
                                ),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = {
                                    currentDescription = attraction
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    val currentUserId = getCurrentUserId(context)
                    if (currentUserId != null) {
                        if (selectedAttractions.isNotEmpty()) {
                            AttractionDatabase.saveAttractions(
                                currentUserId,
                                selectedAttractions
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
                    .width(330.dp)
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
        if (showDescriptionDialog) {
            AlertDialog(
                onDismissRequest = { showDescriptionDialog = false },
                title = { Text(currentDescription, fontSize = 30.sp, fontFamily = myFontFamily) },
                text = {
                    Column(
                        modifier = Modifier
                            .height(500.dp)
                            .padding(top = 8.dp, bottom = 2.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = painterResource(
                                    id = attractionImages[currentDescription] ?: R.drawable.backlogo
                                ),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
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
                                Row() {
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
                                            "제주도 제주시 조천읍 함덕리 산14-1",
                                            fontFamily = myFontFamily,
                                            fontSize = 17.sp,
                                            lineHeight = 19.sp
                                        )

                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Divider(
                                thickness = 1.dp,
                                color = Color.Black
                            )
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
                                    // 평점
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text("평점", fontFamily = myFontFamily, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("7.0 / 10", fontSize = 22.sp, fontFamily = myFontFamily)
                                }
                                Column(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    // 날씨
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(125.dp)
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "바람이 많이 불 것으로 예상되어 실내활동을 추천하는 함덕해수욕장은 부부와 함께하는 여행에 적합합니다.",
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
                    TextButton(
                        onClick = { showDescriptionDialog = false }
                    ) {
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
