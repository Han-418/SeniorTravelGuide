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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.firebase.FirebaseApp
import com.intel.NLPproject.ui.theme.SeniorTravelGuideTheme
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
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
        startDestination = "splash",
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("phoneLogin") { PhoneLoginScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("recommendAttraction") { RecommendAttractionScreen(navController) }
        composable("recommendTransportation") { RecommendTransportationScreen(navController) }
        composable("recommendRestaurants") { RecommendRestaurantsScreen(navController) }
        composable("preference") { PreferenceScreen(navController) }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    // 각 질문별 옵션 목록
    val destinationOptions = listOf(
        "근교 (차로 1~2시간)",
        "부산 / 경상권",
        "강원도",
        "제주도",
        "전라도",
        "충청도",
        "잘 모르겠음 (AI 추천)"
    )
    val travelPeriodOptions = listOf(
        "당일치기",
        "1박 2일",
        "2박 3일",
        "3박 이상"
    )
    val companionOptions = listOf(
        "혼자",
        "부부 / 커플",
        "가족 (아이 포함)",
        "부모님 모시고 (어르신 포함)",
        "친구들과"
    )
    val transportationOptions = listOf(
        "자가용",
        "기차 / 버스",
        "비행기",
        "상관없음 (추천)"
    )
    val budgetOptions = listOf(
        "최대한 저렴하게",
        "평균적으로",
        "가격 상관없음"
    )
    val subregionOptionsMap = mapOf(
        "부산 / 경상권" to listOf("부산", "대구", "울산"),
        "강원도" to listOf("강릉", "속초", "원주"),
        "제주도" to listOf("제주시", "서귀포시"),
        "전라도" to listOf("전주", "광주", "순천", "여수"),
        "충청도" to listOf("대전", "청주", "천안")
    )
    // 각 질문의 선택 상태
    val selectedDestination = remember { mutableStateOf("") }
    val selectedTravelPeriod = remember { mutableStateOf("") }
    val selectedCompanion = remember { mutableStateOf("") }
    val selectedTransportation = remember { mutableStateOf("") }
    val selectedBudget = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Text("여행 계획기", fontSize = 50.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        // 질문 1: 어디로 가실래요?
        CascadingDropdownQuestion(
            question = "어디로 가실래요?",
            options = destinationOptions,
            subOptionsMap = subregionOptionsMap,
            selectedOption = selectedDestination
        )

        // 질문 2: 여행 기간은 몇 박 몇 일로 생각하세요?
        DropdownQuestion(
            question = "여행 기간은 어떻게 되시나요?",
            options = travelPeriodOptions,
            selectedOption = selectedTravelPeriod
        )
        // 질문 3: 누구와 함께 가세요?
        DropdownQuestion(
            question = "누구와 함께 가세요?",
            options = companionOptions,
            selectedOption = selectedCompanion
        )
        // 질문 4: 이동 방법은?
        DropdownQuestion(
            question = "이동 방법은?",
            options = transportationOptions,
            selectedOption = selectedTransportation
        )
        // 질문 5: 예산은 어느 정도 생각하세요?
        DropdownQuestion(
            question = "예산은 어느 정도 생각하세요?",
            options = budgetOptions,
            selectedOption = selectedBudget
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                navController.navigate("recommendAttraction")
            }
        ) {
            Text("제출하기")
        }
        Button(onClick = {
            // Firebase 로그아웃
            AuthManager.logout()
            // 카카오 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("Kakao", "카카오 로그아웃 실패: $error")
                } else {
                    Log.d("Kakao", "카카오 로그아웃 성공")
                }
            }
            // 네이버 로그아웃
            NaverIdLoginSDK.logout()
            // 로그아웃 후 로그인 화면으로 이동 (기존 스택을 모두 제거)
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        }) {
            Text("로그아웃")
        }
    }
}

@Composable
fun DropdownQuestion(
    question: String,
    options: List<String>,
    selectedOption: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    // 메뉴가 화면 너비의 90%를 차지하므로
    val menuWidth = screenWidth * 0.9f
    // 메뉴를 중앙에 배치하기 위한 오프셋
    val offsetX = (screenWidth - menuWidth) / 2

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 버튼을 누르면 드롭다운 메뉴가 펼쳐짐
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp)
                .height(50.dp)
        ) {
            Text(text = if (selectedOption.value.isEmpty()) question else selectedOption.value, fontSize = 20.sp)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = offsetX, y = 0.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
                .background(color = Color.Transparent)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption.value = option
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun CascadingDropdownQuestion(
    question: String,
    options: List<String>,
    subOptionsMap: Map<String, List<String>>,
    selectedOption: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    // currentOptions: 현재 보여줄 옵션 목록. 초기에는 전체 옵션이 들어감.
    var currentOptions by remember { mutableStateOf(options) }
    // currentParent: 만약 세부지역 메뉴로 전환된 경우, 상위(부모) 지역을 저장.
    var currentParent by remember { mutableStateOf<String?>(null) }

    // 화면 너비 계산(드롭다운 중앙 정렬용)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val menuWidth = screenWidth * 0.9f
    val offsetX = (screenWidth - menuWidth) / 2

    Box(modifier = Modifier.fillMaxWidth()) {
        // 드롭다운을 여는 버튼
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(50.dp)
        ) {
            Text(
                text = if (selectedOption.value.isEmpty()) question else selectedOption.value,
                fontSize = 20.sp
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                // 드롭다운이 닫힐 때 초기 상태로 복귀
                currentOptions = options
                currentParent = null
            },
            offset = DpOffset(x = offsetX, y = 0.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(color = Color.Transparent)
        ) {
            currentOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        // 만약 아직 상위 옵션이 선택되지 않았고, 선택한 옵션에 세부지역이 있다면
                        if (currentParent == null && subOptionsMap.containsKey(option)) {
                            currentParent = option
                            currentOptions = subOptionsMap[option] ?: emptyList()
                        } else {
                            // 최종 선택: 상위 옵션이 있다면 "상위: 하위" 형태로, 그렇지 않으면 단일 값
                            selectedOption.value = if (currentParent != null) {
                                "$currentParent: $option"
                            } else {
                                option
                            }
                            // 선택 완료 후 상태 초기화
                            currentOptions = options
                            currentParent = null
                            expanded = false
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color.Black
                    )
                )
            }
        }
    }
}