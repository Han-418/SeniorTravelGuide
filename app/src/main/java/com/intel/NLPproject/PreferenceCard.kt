package com.intel.NLPproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.intel.NLPproject.ui.theme.SeniorTravelGuideTheme

class PreferenceCard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeniorTravelGuideTheme {
                PreferenceCard()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SeniorTravelGuideTheme {
        PreferenceCard()
    }
}
@Composable
fun PreferenceCard(onComplete: (String) -> Unit) {
    // 카드 형식의 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "여행 취향을 선택해 주세요!", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onComplete("유유자적") }) {
                        Text(text = "유유자적(조금만 돌아다님)")
                    }
                    Button(onClick = { onComplete("과유불급") }) {
                        Text(text = "과유불급(적당히 돌아다님)")
                    }
                    Button(onClick = { onComplete("동분서주") }) {
                        Text(text = "동분서주(많이 돌아다님)")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
