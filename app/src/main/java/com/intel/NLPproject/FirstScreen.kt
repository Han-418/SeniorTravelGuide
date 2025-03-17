package com.intel.NLPproject

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.intel.NLPproject.TokenManager.getCurrentUserKey
import com.intel.NLPproject.firebase.UserInfoDatabase
import com.intel.NLPproject.ui.theme.testFamily

@Composable
fun FirstScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("여행한잔", fontSize = 50.sp, fontFamily = testFamily, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("image")
        }
        Button(
            onClick = {
                navController.navigate("main")
            }
        ) {
            Text("시작하기")
        }
        LogoutButton(navController)
        ShowCurrentUserButton()
    }
}

@Composable
fun ShowCurrentUserButton() {
    val context = LocalContext.current

    Button(onClick = {
        val currentUserKey = getCurrentUserKey()
        if (currentUserKey != null) {
            // DB에서 해당 key로 사용자 정보 조회
            UserInfoDatabase().getUserInfo(currentUserKey) { userInfo ->
                if (userInfo != null) {
                    Toast.makeText(context, "현재 유저: ${userInfo.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "로그인된 유저가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("현재 유저 확인")
    }
}