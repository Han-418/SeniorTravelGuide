package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    // 예시 식당 추천 목록 (10개)
    val restaurants = listOf(
        "식당 추천 1", "식당 추천 2", "식당 추천 3", "식당 추천 4", "식당 추천 5",
        "식당 추천 6", "식당 추천 7", "식당 추천 8", "식당 추천 9", "식당 추천 10"
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
                        },
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
                        Text(text = restaurant)
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

    }
}