package com.intel.NLPproject

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun TravelPlanScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("최종 계획", fontSize = 32.sp)

        val context = LocalContext.current
        Button(onClick = {
            val bookingUrl = "https://www.letskorail.com/" // 코레일 예매 URL로 변경
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingUrl))
            context.startActivity(intent)
        }) {
            Text("코레일 예매하기")
        }

        Button(
            onClick = {
            }
        ) {
            Text("저장하기")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
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
    }
}