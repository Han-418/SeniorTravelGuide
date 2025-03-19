@file:OptIn(ExperimentalAnimationApi::class)

package com.intel.NLPproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp
import com.intel.NLPproject.ui.theme.SeniorTravelGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            val navController = rememberNavController()
            SeniorTravelGuideTheme {
                MyApp(navController)
            }
        }
    }
}

@Composable
fun MyApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { navBackStackEntry ->
            LoginScreen(
                onKakaoLoginSuccess = { accessToken ->
                    // 카카오 로그인 성공 시 처리 (예: 로그 출력, 추가 동작 등)
                    Log.d("LoginScreen", "카카오 로그인 성공! AccessToken: $accessToken")
                },
                onKakaoLoginError = { error ->
                    // 카카오 로그인 실패 시 처리 (예: 에러 로그, 사용자 알림 등)
                    Log.e("LoginScreen", "카카오 로그인 실패: ${error.message}")
                },
                navController = navController
            )
        }
        composable("phoneLogin") { PhoneLoginScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("recommendAttraction") { RecommendAttractionScreen(navController) }
        composable("recommendAttraction2") { RecommendAttractionScreen2(navController) }
        composable("recommendAccommodation") { RecommendAccommodationScreen(navController) }
        composable("recommendRestaurants") { RecommendRestaurantsScreen(navController) }
        composable("attractionPreference") { AttractionPreferenceScreen(navController) }
        composable("accommodationPreference") { AccommodationPreferenceScreen(navController) }
        composable("restaurantPreference") { RestaurantPreferenceScreen(navController) }
        composable("travelTimePreference") { TravelTimePreferenceScreen(navController) }
        composable("travelPlan") { TravelPlanScreen(navController) }
        composable(
            route = "loading/{destination}",
            arguments = listOf(navArgument("destination") { type = NavType.StringType })
        ) { backStackEntry ->
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            LoadingScreen(navController, destination = destination)
        }
        composable("first") { FirstScreen(navController) }
        composable("question") { QuestionScreen(navController) }
        composable("detailPlan") { DetailPlanScreen(navController) }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )

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
                .background(color = Color(0xFFFFA700))
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "top logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 22.dp)
            )
        }
        Spacer(modifier = Modifier.height(80.dp))
        // button1
        Button(
            onClick = {},
            modifier = Modifier
                .width(330.dp)
                .height(49.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("어디로 여행을 떠나시나요?", fontSize = 21.sp, color = Color.Black, fontFamily = myFontFamily)
        }
        Spacer(modifier = Modifier.height(33.dp))
        // button2
        Button(
            onClick = {
            },
            modifier = Modifier
                .width(330.dp)
                .height(49.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                "여행 기간이 어떻게 되시나요?",
                fontSize = 21.sp,
                color = Color.Black,
                fontFamily = myFontFamily
            )
        }
        Spacer(modifier = Modifier.height(33.dp))
        // button3
        Button(
            onClick = {
            },
            modifier = Modifier
                .width(330.dp)
                .height(49.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("누구와 함께 가시나요?", fontSize = 21.sp, color = Color.Black, fontFamily = myFontFamily)
        }
        Spacer(modifier = Modifier.height(33.dp))
        // button4
        Button(
            onClick = {
            },
            modifier = Modifier
                .width(330.dp)
                .height(49.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                "이동수단은 어떻게 되시나요?",
                fontSize = 21.sp,
                color = Color.Black,
                fontFamily = myFontFamily
            )
        }
        Spacer(modifier = Modifier.height(33.dp))
        // button5
        Button(
            onClick = {
            },
            modifier = Modifier
                .width(330.dp)
                .height(49.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("예산은 어떻게 생각하세요?", fontSize = 21.sp, color = Color.Black, fontFamily = myFontFamily)
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {
                // 모든 질문 선택 완료 후 결과 페이지로 이동
                navController.navigate("question")
            },
            modifier = Modifier
                .width(247.dp)
                .height(48.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF20574), // 버튼 배경색 (예: my_color 값)
                contentColor = Color.White           // 버튼 텍스트 색상
            )
        ) {
            Text("일정 짜러 가기", fontSize = 21.sp, color = Color(0xFFFFFFFF), fontFamily = myFontFamily)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .width(247.dp)
                .height(48.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF20574), // 버튼 배경색 (예: my_color 값)
                contentColor = Color.White           // 버튼 텍스트 색상
            )
        ) {
            Text("뒤로가기", fontSize = 21.sp, fontFamily = myFontFamily, color = Color(0xFFFFFFFF))
        }

    }
}

@Composable
fun LogoutButton(navController: NavController) {
    val myFontFamily = FontFamily(
        Font(R.font.notoserifkrblack)
    )
    val context = LocalContext.current  // Composable 컨텍스트 내에서 미리 호출
    Button(
        onClick = {
            LogoutManager.logout(context)
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        },
        modifier = Modifier
            .width(247.dp)
            .height(48.dp),
        shape = RoundedCornerShape(100.dp)
    ) {
        Text("로그아웃", fontSize = 21.sp, fontFamily = myFontFamily, color = Color.Black)
    }
}