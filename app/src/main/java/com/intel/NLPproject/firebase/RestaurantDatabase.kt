package com.intel.NLPproject.firebase

import com.google.firebase.database.FirebaseDatabase

object RestaurantDatabase {
    private val db = FirebaseDatabase.getInstance().getReference("restaurants")

    // userId 노드 아래에 선택한 식당 목록(List<String>)을 저장합니다.
    fun saveRestaurants(userId: String, restaurants: List<String>, onComplete: (Boolean) -> Unit) {
        db.child(userId).setValue(restaurants)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}