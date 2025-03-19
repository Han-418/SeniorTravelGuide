package com.intel.NLPproject.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

object AttractionDatabase {
    // "attractions" 노드 아래에 사용자별 데이터를 저장
    private val db = FirebaseDatabase.getInstance().getReference("attractions")

    // userId에 해당하는 노드에 attraction 목록을 저장합니다.
    fun saveAttractions(userId: String, attractions: List<String>, onComplete: (Boolean) -> Unit) {
        db.child(userId).setValue(attractions)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // 데이터를 불러오는 함수 추가
    fun getAttractions(userId: String, onComplete: (List<String>?) -> Unit) {
        db.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase는 제네릭 타입을 사용하여 데이터를 불러올 수 있습니다.
                val list = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                onComplete(list)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }
}
