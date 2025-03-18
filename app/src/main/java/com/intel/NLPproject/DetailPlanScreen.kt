package com.intel.NLPproject

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DetailPlanScreen(navController: NavHostController) {
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )
    val context = LocalContext.current

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
                "여행 일정",
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
                .height(560.dp)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("여행 일정 예시", fontSize = 40.sp)
        }


        Button(onClick = {
            val bookingUrl = "https://www.letskorail.com/" // 코레일 예매 URL로 변경
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingUrl))
            context.startActivity(intent)
        }) {
            Text("코레일 예매")
        }

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
        Spacer(modifier = Modifier.height(4.dp))
    }
}