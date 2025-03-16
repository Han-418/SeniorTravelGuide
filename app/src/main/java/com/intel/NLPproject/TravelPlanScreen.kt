package com.intel.NLPproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun TravelPlanScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("최종 계획", fontSize = 32.sp)
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