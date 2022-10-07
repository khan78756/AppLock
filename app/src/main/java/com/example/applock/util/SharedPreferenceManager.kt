package com.example.applock.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceManager(context: Context) {

    private companion object {
        const val APPS_KEY = "saved_apps"
        const val APPS_KEY1 = "Lock"
        const val APPS_KEY2 = "Flag"
        const val APPS_KEY3 = "Flag1"

    }


    private val sharedPreferences = context.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferences1 = context.getSharedPreferences("prefss", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferences2 = context.getSharedPreferences("prefssss", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferences3 = context.getSharedPreferences("prefsssss", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferences4 = context.getSharedPreferences("prefssssss", AppCompatActivity.MODE_PRIVATE)




    fun saveAppsList(apps: ArrayList<String>) {
        val editor = sharedPreferences.edit()
        editor.putString(APPS_KEY, apps.joinToString("|"))
        editor.apply()
    }



    fun readAppsList(): ArrayList<String> {
        val raw = sharedPreferences.getString(APPS_KEY, "") ?: ""
        return ArrayList(raw.split("|"))
    }




    fun specificData(str: String){
        val editor1=sharedPreferences1.edit()
        editor1.putString(APPS_KEY1,str)
        editor1.apply()
    }

    fun readString() :String?
    {
        val raw2=sharedPreferences1.getString(APPS_KEY1, null)
        return raw2
    }



    fun flag(flag: Int){
        val editor2=sharedPreferences2.edit()
        editor2.putInt(APPS_KEY2,flag)
        editor2.apply()
    }

    fun readFlag() :Int
    {
        val raw2=sharedPreferences2.getInt(APPS_KEY2,0)
        return raw2
    }

    fun flag1(flag: Int){
        val editor3=sharedPreferences3.edit()
        editor3.putInt(APPS_KEY3,flag)
        editor3.apply()
    }

    fun readFlag1() :Int
    {
        val raw3=sharedPreferences3.getInt(APPS_KEY3,0)
        return raw3
    }



}