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
import com.intel.NLPproject.firebase.RestaurantDatabase

@Composable
fun RecommendRestaurantsScreen(navController: NavHostController) {
    var showDescriptionDialog by remember { mutableStateOf(false) }
    var currentDescription by remember { mutableStateOf("") }
    // 예시 식당 추천 목록 (10개)
    val restaurants = listOf(
        "갈치옥 함덕", "백가네 제주 한상", "터틀리애", "동백국수"
    )
    val restaurantsImages = mapOf(
        "갈치옥 함덕" to R.drawable.galchiok,
        "백가네 제주 한상" to R.drawable.backga,
        "터틀리애" to R.drawable.turtleliae,
        "동백국수" to R.drawable.dongback
    )
    val context = LocalContext.current

    // 선택된 식당을 저장할 상태 (다중 선택 가능)
    val selectedRestaurants = remember { mutableStateListOf<String>() }
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
                "식당 추천 목록",
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
            items(restaurants.size) { index ->
                val restaurant = restaurants[index]
                // 해당 식당이 선택되었는지 확인
                val isSelected = selectedRestaurants.contains(restaurant)
                Column {
                    Text(
                        text = restaurant,
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
                                    selectedRestaurants.remove(restaurant)
                                } else {
                                    selectedRestaurants.add(restaurant)
                                }
                            }
                            .border(
                                width = 3.dp,
                                color = if (isSelected) Color(0xFFF20574) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        // 선택된 항목은 elevation이나 배경색 등으로 구분
                        elevation = if (isSelected)
                            CardDefaults.cardElevation(defaultElevation = 8.dp)
                        else
                            CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (isSelected) Color.LightGray else Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(
                                    id = restaurantsImages[restaurant] ?: R.drawable.backlogo
                                ),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = {
                                    currentDescription = restaurant
                                    showDescriptionDialog = true
                                },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "식당 설명 보기"
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
            // 버튼: 선택한 식당 목록을 저장하고 다음 화면으로 이동
            Button(
                onClick = {
                    val currentUserId = getCurrentUserId(context)
                    if (currentUserId != null) {
                        if (selectedRestaurants.isNotEmpty()) {
                            RestaurantDatabase.saveRestaurants(
                                currentUserId,
                                selectedRestaurants
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "식당 정보 저장 완료", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigate("travelTimePreference")
                                } else {
                                    Toast.makeText(context, "식당 정보 저장 실패", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "선택된 식당이 없습니다.", Toast.LENGTH_SHORT).show()
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
                    text = "식당 확정",
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
                                    id = restaurantsImages[currentDescription]
                                        ?: R.drawable.backlogo
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
                                .height(150.dp)
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
                                            .fillMaxWidth(0.3f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.location),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(60.dp)
                                                .offset(x = (-5).dp)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.7f),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "제주시 조천읍 함덕리 산14-13",
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
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.7f),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 평점
                                Spacer(modifier = Modifier.width(28.dp))
                                Text("평점", fontFamily = myFontFamily, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(42.dp))
                                Text("3.9 / 5", fontSize = 40.sp, fontFamily = myFontFamily)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                                .padding(4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "제주 전통 한상을 정갈하게 즐길 수 있어 식사 시간이 특별해지는 식당입니다.",
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