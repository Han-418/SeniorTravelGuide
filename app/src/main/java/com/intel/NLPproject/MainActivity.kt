@file:OptIn(ExperimentalAnimationApi::class)

package com.intel.NLPproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.intel.NLPproject.ui.theme.SeniorTravelGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeniorTravelGuideTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "main",
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }
    ) {
        composable("main") { MainScreen(navController) }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("여행 계획기", fontSize = 50.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

    }
}