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
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
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
        composable("recommendAttraction2") { RecommendAttractionScreen2(navController) }
        composable("recommendAttraction3") { RecommendAttractionScreen3(navController) }
        composable("recommendAccommodation") { RecommendAccommodationScreen(navController) }
        composable("recommendRestaurants") { RecommendRestaurantsScreen(navController) }
        composable("recommendTransportation") { RecommendTransportationScreen(navController) }
        composable("attractionPreference") { AttractionPreferenceScreen(navController) }
//        composable("attractionPreference2") { AttractionPreferenceScreen2(navController) }
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
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    // 각 질문별 옵션 목록
    val destinationOptions = listOf(
        "직접 입력",
        "경상도",
        "강원도",
        "제주도",
        "전라도",
        "충청도"
    )
    val travelPeriodOptions = listOf(
        "당일치기",
        "1박 2일",
        "2박 3일",
        "3박 이상"
    )
    val companionOptions = listOf(
        "혼자",
        "부부/커플",
        "가족들과",
        "부모님과",
        "친구들과"
    )
    val transportationOptions = listOf(
        "자가용",
        "기차",
        "버스",
        "비행기",
        "상관없음"
    )
    val budgetOptions = listOf(
        "저렴하게",
        "평균적",
        "상관없음"
    )
    val subregionOptionsMap = mapOf(
        "경상도" to listOf("부산", "대구", "울산", "포항", "경주", "안동"),
        "강원도" to listOf("강릉", "속초", "원주", "춘천", "동해", "양양"),
        "제주도" to listOf("제주시", "서귀포시", "애월읍", "한림읍", "우도면", "성산읍"),
        "전라도" to listOf("전주", "광주", "순천", "여수", "목포", "곡성"),
        "충청도" to listOf("대전", "청주", "천안", "논산", "보령", "괴산")
    )
    // 각 질문의 선택 상태
    val selectedDestination = remember { mutableStateOf("") }
    val selectedTravelPeriod = remember { mutableStateOf("") }
    val selectedCompanion = remember { mutableStateOf("") }
    val selectedTransportation = remember { mutableStateOf("") }
    val selectedBudget = remember { mutableStateOf("") }
    // 직접 입력 다이얼로그를 위한 상태
    val showCustomInputDialog = remember { mutableStateOf(false) }
    val customDestinationInput = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Text("여행한잔", fontSize = 50.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        // 질문 1: 목적지 선택 (부산/경상권 등 서브 옵션 포함)
        GridCascadingQuestion(
            question = "어디로 가실래요?",
            options = destinationOptions,
            subOptionsMap = subregionOptionsMap,
            selectedOption = selectedDestination
        )
        // "직접 입력" 옵션 선택 시 다이얼로그 띄우기
        if (selectedDestination.value == "직접 입력" && !showCustomInputDialog.value) {
            showCustomInputDialog.value = true
        }
        if (showCustomInputDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showCustomInputDialog.value = false
                    // 취소 시 selectedDestination 초기화
                    selectedDestination.value = ""
                },
                title = { Text("목적지 직접 입력") },
                text = {
                    Column {
                        Text("여행 가고 싶은 곳을 입력하세요:")
                        OutlinedTextField(
                            value = customDestinationInput.value,
                            onValueChange = { customDestinationInput.value = it },
                            placeholder = { Text("예: 서울, 부산, 제주 등") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // 입력한 값으로 선택 업데이트 후 다이얼로그 닫기
                            selectedDestination.value = customDestinationInput.value
                            showCustomInputDialog.value = false
                        }
                    ) {
                        Text("확인")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showCustomInputDialog.value = false
                            // 취소 시에도 selectedDestination 값을 초기화하여 다이얼로그가 재발동되지 않도록 함
                            selectedDestination.value = ""
                        }
                    ) {
                        Text("취소")
                    }
                }
            )
        }

        // 질문 2: 여행 기간은 몇 박 몇 일로?
        GridQuestion(
            question = "여행 기간은 어떻게 되시나요?",
            options = travelPeriodOptions,
            selectedOption = selectedTravelPeriod
        )
        // 질문 3: 누구와 함께 가세요?
        GridQuestion(
            question = "누구와 함께 가세요?",
            options = companionOptions,
            selectedOption = selectedCompanion
        )
        // 질문 4: 이동 방법은?
        GridQuestion(
            question = "이동 방법은?",
            options = transportationOptions,
            selectedOption = selectedTransportation
        )
        // 질문 5: 예산은 어느 정도 생각하세요?
        GridQuestion(
            question = "예산은 어느 정도 생각하세요?",
            options = budgetOptions,
            selectedOption = selectedBudget
        )

        Spacer(modifier = Modifier.size(30.dp))
        Row {
            LogoutButton(navController)
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    // 모든 질문 선택 완료 후 결과 페이지로 이동
                    navController.navigate("loading/recommendAttraction")
                }
            ) {
                Text("제출하기")
            }
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
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = offsetX, y = 0.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
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
    selectedOption: MutableState<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // 현재 보여줄 옵션 목록. 초기에는 전체 옵션이 들어감.
    var currentOptions by remember { mutableStateOf(options) }
    // 만약 세부지역 메뉴로 전환된 경우, 상위(부모) 지역을 저장.
    var currentParent by remember { mutableStateOf<String?>(null) }

    // 화면 너비 계산 (드롭다운 중앙 정렬용)
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
            offset = DpOffset(x = offsetX, y = 141.dp),
            properties = PopupProperties(clippingEnabled = false),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(color = Color.Transparent)
        ) {
            currentOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        // 아직 상위 옵션이 선택되지 않았고, 선택한 옵션에 세부지역이 있다면
                        if (currentParent == null && subOptionsMap.containsKey(option)) {
                            currentParent = option
                            currentOptions = subOptionsMap[option] ?: emptyList()
                        } else {
                            // 최종 선택: 상위 옵션이 있다면 "상위: 하위" 형태로, 그렇지 않으면 단일 값
                            val finalOption = if (currentParent != null) {
                                "$currentParent: $option"
                            } else {
                                option
                            }
                            selectedOption.value = finalOption
                            onOptionSelected(finalOption)
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

@Composable
fun LogoutButton(navController: NavController) {
    val context = LocalContext.current  // Composable 컨텍스트 내에서 미리 호출
    Button(onClick = {
        LogoutManager.logout(context)
        navController.navigate("login") {
            popUpTo("main") { inclusive = true }
        }
    }) {
        Text("로그아웃")
    }
}

@Composable
fun GridQuestion(
    question: String,
    options: List<String>,
    selectedOption: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        // 기본 버튼
        OutlinedButton(
            onClick = { expanded = !expanded },
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
        // 버튼 클릭 시 그리드 형태로 옵션 보여주기
        if (expanded) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3열
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp), // 필요에 따라 높이 조정
                contentPadding = PaddingValues(8.dp)
            ) {
                items(options.size) { index ->
                    val option = options[index]
                    Button(
                        onClick = {
                            selectedOption.value = option
                            expanded = false
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun GridCascadingQuestion(
    question: String,
    options: List<String>,
    subOptionsMap: Map<String, List<String>>,
    selectedOption: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    // 초기 옵션은 전체 옵션, sub옵션 선택 시 변경됨
    var currentOptions by remember { mutableStateOf(options) }
    var currentParent by remember { mutableStateOf<String?>(null) }
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = !expanded },
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
        if (expanded) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(currentOptions.size) { index ->
                    val option = currentOptions[index]
                    Button(
                        onClick = {
                            // 만약 아직 상위 옵션이 선택되지 않았고, 해당 옵션에 서브 옵션이 있다면
                            if (currentParent == null && subOptionsMap.containsKey(option)) {
                                currentParent = option
                                currentOptions = subOptionsMap[option] ?: emptyList()
                            } else {
                                selectedOption.value = if (currentParent != null) {
                                    "$currentParent: $option"
                                } else {
                                    option
                                }
                                // 선택 후 초기 상태로 복귀
                                currentOptions = options
                                currentParent = null
                                expanded = false
                            }
                        },
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }
}