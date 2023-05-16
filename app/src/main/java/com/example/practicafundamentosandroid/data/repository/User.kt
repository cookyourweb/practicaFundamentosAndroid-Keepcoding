package com.example.practicafundamentosandroid.data.repository

import android.content.Context

class User {
    companion object {
        private var sharedPreferencesName = "UserDetails"
        private var TAG_USER_TOKEN = "TAG_USER_TOKEN"

        fun updateToken(newToken: String, context: Context) {
            with(context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE).edit()) {
                putString(TAG_USER_TOKEN, newToken)
                apply()
            }
        }

        fun getToken(context: Context) =
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getString(TAG_USER_TOKEN, "")
    }
}