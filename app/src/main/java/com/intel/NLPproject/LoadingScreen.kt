package com.intel.NLPproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    navController: NavHostController,
    destination: String,
    loadingDelay: Long = 2000L // 로딩 딜레이 (예: 2초)
) {
    LaunchedEffect(key1 = destination) {
        delay(loadingDelay)
        navController.popBackStack() // 현재 로딩 화면 제거
        navController.navigate(destination)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp)
        )
    }
}
