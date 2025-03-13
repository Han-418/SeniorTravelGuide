package com.intel.NLPproject.firebase

import com.google.firebase.database.FirebaseDatabase
import com.intel.NLPproject.models.UserInfo

class UserInfoDatabase {
    private val db = FirebaseDatabase.getInstance().getReference("users")

    // 사용자 정보를 저장 (저장 시 UID가 있다면 해당 키를 사용, 없으면 새로운 키를 생성)
    fun saveUserInfo(userInfo: UserInfo, onComplete: (Boolean) -> Unit) {
        val key = if (userInfo.uid.isNotBlank()) userInfo.uid else db.push().key ?: return
        val newUserInfo = UserInfo(
            uid = key,
            name = userInfo.name,
            birthDate = userInfo.birthDate,
            gender = userInfo.gender,
            phoneNumber = userInfo.phoneNumber
        )
        db.child(key).setValue(newUserInfo)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // 저장된 사용자 정보를 모두 조회
    fun getUserInfos(onResult: (List<UserInfo>) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            val userInfos = snapshot.children.mapNotNull { child ->
                val uid = child.child("uid").getValue(String::class.java) ?: ""
                val name = child.child("name").getValue(String::class.java) ?: "이름 없음"
                val birthDate = child.child("birthDate").getValue(String::class.java) ?: "생년월일 없음"
                val gender = child.child("gender").getValue(String::class.java) ?: "성별 미정"
                val phoneNumber = child.child("phoneNumber").getValue(String::class.java) ?: "전화번호 없음"
                UserInfo(uid = uid, name = name, birthDate = birthDate, gender = gender, phoneNumber = phoneNumber)
            }
            onResult(userInfos)
        }
    }

    // 특정 사용자 정보를 삭제 (UID로 삭제)
    fun deleteUserInfo(uid: String, onComplete: (Boolean) -> Unit) {
        db.child(uid).removeValue()
            .addOnCompleteListener { task -> onComplete(task.isSuccessful) }
            .addOnFailureListener { onComplete(false) }
    }

    // 특정 전화번호가 이미 등록되어 있는지 확인하는 함수
    fun checkUserExists(phoneNumber: String, onResult: (Boolean) -> Unit) {
        db.get()
            .addOnSuccessListener { snapshot ->
                val exists = snapshot.children.any { child ->
                    child.child("phoneNumber").getValue(String::class.java) == phoneNumber
                }
                onResult(exists)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}