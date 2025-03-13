package com.intel.NLPproject

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun TravelPlanScreen(navController: NavHostController) {
    Column {
        Text("최종 계획", fontSize = 32.sp)
    }
}