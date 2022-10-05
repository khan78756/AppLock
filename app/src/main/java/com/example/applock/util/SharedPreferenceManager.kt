package com.example.applock.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceManager(context: Context) {

    private companion object {
        const val APPS_KEY = "saved_apps"

    }


    private val sharedPreferences = context.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferences1 = context.getSharedPreferences("prefss", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferences2 = context.getSharedPreferences("prefssss", AppCompatActivity.MODE_PRIVATE)




    fun saveAppsList(apps: ArrayList<String>) {
        val editor = sharedPreferences.edit()
        editor.putString(APPS_KEY, apps.joinToString("|"))
        editor.apply()
    }

    fun readAppsList(): ArrayList<String> {
        val raw = sharedPreferences.getString(APPS_KEY, "") ?: ""
        return ArrayList(raw.split("|"))
    }

    var specificData = sharedPreferences1.edit()
    var getData=sharedPreferences1.getString("Lock",null)

    var flag=sharedPreferences2.edit()

    val getInt=sharedPreferences2.getInt("Flag",0)










}