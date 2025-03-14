package com.intel.NLPproject.firebase

import com.google.firebase.database.FirebaseDatabase

object AttractionDatabase {
    // "attractions" 노드 아래에 사용자별 데이터를 저장
    private val db = FirebaseDatabase.getInstance().getReference("attractions")

    // userId에 해당하는 노드에 attraction 목록을 저장합니다.
    fun saveAttractions(userId: String, attractions: List<String>, onComplete: (Boolean) -> Unit) {
        db.child(userId).setValue(attractions)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
