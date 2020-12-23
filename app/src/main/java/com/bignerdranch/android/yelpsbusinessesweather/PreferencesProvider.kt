package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import com.google.gson.Gson

class PreferencesProvider(context: Context) {
    private val sharedPrefrences =
            context.getSharedPreferences("myPreferences", 0)

    fun putPhoto(key:String, photo: String){
        var prefsEditor = sharedPrefrences.edit()
        var gson = Gson()
        var json = gson.toJson(photo)
        prefsEditor.putString("MyObject", json)
        prefsEditor.commit()    }

    fun getPhoto(key:String): String? {
        var gson = Gson()
        var json = sharedPrefrences.getString("MyObject", "")
//        var obj = gson.fromJson(json, String)
        return json    }
}