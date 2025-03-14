package com.intel.NLPproject.firebase

import com.google.firebase.database.FirebaseDatabase

object AccommodationDatabase {
    private val db = FirebaseDatabase.getInstance().getReference("accommodations")

    // 주어진 userId의 노드에 숙소 목록을 저장합니다.
    fun saveAccommodations(userId: String, accommodations: List<String>, onComplete: (Boolean) -> Unit) {
        db.child(userId).setValue(accommodations)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}