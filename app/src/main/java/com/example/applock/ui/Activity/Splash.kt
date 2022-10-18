package com.example.applock.ui.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.applock.R
import com.example.applock.util.SharedPreferenceManager

class Splash : AppCompatActivity() {


    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash2)

        //REMOVE ACTION BAR
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val splash3=findViewById<LinearLayout>(R.id.llsplash)

        prefManager.flag(0)
        prefManager.flag1(0)
        prefManager.specificData("")



        splash3.alpha=0f

        splash3.animate().setDuration(1500).alpha(1f).withEndAction{
            val intent= Intent(this,PatternActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            overridePendingTransition(R.anim.sliderightin,R.anim.slideoutleft)
            finish()

        }
    }
}