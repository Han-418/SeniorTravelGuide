package com.intel.NLPproject.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.intel.NLPproject.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SeniorTravelGuideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // 동적 색상 사용 시 true로 설정, 여기서는 my_color를 사용하기 위해 false로 둡니다.
    content: @Composable () -> Unit
) {
    // XML 리소스에서 my_color를 불러옴
    val myColor = colorResource(id = R.color.my_color)
    // 필요에 따라 다른 색상도 불러올 수 있음
    val teal200 = colorResource(id = R.color.teal_200)
    val teal700 = colorResource(id = R.color.teal_700)

    // 커스텀 Light ColorScheme 정의
    val customLightColorScheme = lightColorScheme(
        primary = myColor,
        onPrimary = Color.White,
        secondary = teal200,
        onSecondary = Color.Black,
        background = Color(0xFFF2F2F2),
        onBackground = Color.Black,
        surface = Color.White,
        onSurface = Color.Black
    )

    // 커스텀 Dark ColorScheme 정의
    val customDarkColorScheme = darkColorScheme(
        primary = myColor,
        onPrimary = Color.White,
        secondary = teal700,
        onSecondary = Color.Black,
        background = Color(0xFF121212),
        onBackground = Color.White,
        surface = Color(0xFF1E1E1E),
        onSurface = Color.White
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> customDarkColorScheme
        else -> customLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
