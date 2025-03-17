package com.intel.NLPproject

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 숫자만 추출하고 최대 11자리로 제한 (예: "01033333333")
        val trimmed = text.text.filter { it.isDigit() }.take(11)
        // 입력된 숫자의 길이에 따라 변환된 텍스트 생성
        val transformedText = when {
            trimmed.length <= 3 -> trimmed
            trimmed.length <= 7 -> trimmed.substring(0, 3) + "-" + trimmed.substring(3)
            else -> trimmed.substring(0, 3) + "-" + trimmed.substring(3, 7) + "-" + trimmed.substring(7)
        }

        // 원본과 변환된 텍스트 사이의 오프셋 매핑을 정의
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    // 숫자가 3개 이하이면 그대로 매핑
                    trimmed.length <= 3 -> offset
                    // 숫자가 4~7개이면, 3자 이후부터 하이픈이 삽입되어 +1
                    trimmed.length <= 7 -> if (offset <= 3) offset else offset + 1
                    // 숫자가 8개 이상이면, 3자리와 7자리 위치에 하이픈이 삽입되어 +2
                    else -> if (offset <= 3) offset else if (offset <= 7) offset + 1 else offset + 2
                }
            }
            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    trimmed.length <= 3 -> offset
                    trimmed.length <= 7 -> if (offset <= 3) offset else offset - 1
                    else -> if (offset <= 3) offset else if (offset <= 8) offset - 1 else offset - 2
                }
            }
        }
        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
