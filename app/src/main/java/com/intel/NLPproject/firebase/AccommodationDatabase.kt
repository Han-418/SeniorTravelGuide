package com.intel.NLPproject.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

object AccommodationDatabase {
    private val db = FirebaseDatabase.getInstance().getReference("accommodations")

    // 주어진 userId의 노드에 숙소 목록을 저장합니다.
    fun saveAccommodations(
        userId: String,
        accommodations: List<String>,
        onComplete: (Boolean) -> Unit
    ) {
        db.child(userId).setValue(accommodations)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getAccommodations(userId: String, onComplete: (List<String>?) -> Unit) {
        db.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Firebase의 GenericTypeIndicator를 사용해 List<String>으로 데이터를 가져옵니다.
                val list = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                onComplete(list)
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }
}