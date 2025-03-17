package com.intel.NLPproject

import android.content.Context

fun saveStableUid(context: Context, key: String, uid: String?) {
    val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString(key, uid).apply()
}

fun loadStableUid(context: Context, key: String): String? {
    val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    return prefs.getString(key, null)
}