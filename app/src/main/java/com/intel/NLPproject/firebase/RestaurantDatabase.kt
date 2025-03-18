package com.intel.NLPproject.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

object RestaurantDatabase {
    private val db = FirebaseDatabase.getInstance().getReference("restaurants")

    // userId 노드 아래에 선택한 식당 목록(List<String>)을 저장합니다.
    fun saveRestaurants(userId: String, restaurants: List<String>, onComplete: (Boolean) -> Unit) {
        db.child(userId).setValue(restaurants)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
    fun getRestaurants(userId: String, onComplete: (List<String>?) -> Unit) {
        db.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                onComplete(list)
            }
            override fun onCancelled(error: DatabaseError) {
                onComplete(null)
            }
        })
    }
}