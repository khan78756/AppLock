package com.example.applock.ui.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.applock.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splash3=findViewById<LinearLayout>(R.id.llsplash)

        //prefManager.flag.putInt("Flag",0).apply()



        splash3.alpha=0f

        splash3.animate().setDuration(1500).alpha(1f).withEndAction{
            val intent= Intent(this,PatternActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()

        }
    }
}