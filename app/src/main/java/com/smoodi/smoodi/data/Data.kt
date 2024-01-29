package com.smoodi.smoodi.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.smoodi.smoodi.R

class Data {
    companion object {
        const val KEY_ALWAYS = "always"
        const val KEY_APPLY = "apply"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_NEXT = "next"
        const val KEY_PASSCODE = "passcode"
        const val KEY_SUPER = "super"
        const val KEY_USERS = "users"

        var meals = emptyMap<String, String>()
        var now = ""
        var id = ""
        var name = ""

        fun isSuper(): Boolean {
            return id == KEY_SUPER
        }
    }

    fun getUserData(activity: AppCompatActivity) {

        val sharedPref = activity.getSharedPreferences(
            activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        
        id = sharedPref.getString(KEY_ID, "") ?: ""
        name = sharedPref.getString(KEY_NAME, "") ?: ""

    }

    fun setUserData(activity: AppCompatActivity, id: String, name: String) {

        activity.getSharedPreferences(
            activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            .edit()
            .apply {
                putString(KEY_ID, id)
                putString(KEY_NAME, name)
                apply()
            }

        Data.id = id
        Data.name = name

    }
}