package com.example.applock.ui.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.applock.R
import com.example.applock.databinding.ActivityPatternBinding
import com.example.applock.util.GlobalVariables
import com.example.applock.util.SharedPreferenceManager
import com.itsxtt.patternlock.PatternLockView
import java.io.File


class PatternActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPatternBinding
    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          binding= ActivityPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //FOR THE PURPOSE OF PASSWORD SAVING

        val  sharedPref=getSharedPreferences("myPref", Context.MODE_PRIVATE )
        val editor=sharedPref.edit()
        //=================================================================================//

        //CHECK IF COME FROM OUTER SIDE THEN DESTROY THIS ACTIVITY

        //==================================================================================//

        //CHECK IF PASSWORD IS NOT ALREADY SET
        if(!GlobalVariables.flagConfirm){
            if (sharedPref.getString("Lock",null)==null)
                binding.tvPattern.text="Draw New Password"
            else{
                binding.tvPattern.text="Enter Password"
            }
        }
        else{
            binding.tvPattern.text="Confirm Password"
        }
        //====================================================================================//




        //FOR THE PURPOSE OF RESET PASSWORD
       /* binding.btnClear.setOnClickListener {
           *//* editor.clear().apply()
            Intent(applicationContext, PatternActivity::class.java).also {
                startActivity(it)
                finish()
            }*//*
          //  deleteCache(this)

        }*/
        //==================================================================================//

        //ON THE START OF PASSWORD DRAW
        binding.Patternview.setOnPatternListener(object : PatternLockView.OnPatternListener {
            override fun onStarted() {
                super.onStarted()
            }

            override fun onProgress(ids: ArrayList<Int>) {
                super.onProgress(ids)
            }

            @SuppressLint("SetTextI18n")
            override fun onComplete(ids: ArrayList<Int>):Boolean{

                val pattern = ids.toString()
                //IN CASE OF PREVIOUS PASSWORD
                val str = sharedPref.getString("Lock", null)


                //IN CASE OF NEW PASSWORD ENTERING
                if(sharedPref.getString("Lock",null)==null)
                {
                    Intent(applicationContext, PatternActivity::class.java).also {
                        startActivity(it)

                        editor.apply {
                            putString("Lock", pattern)
                            apply()
                            GlobalVariables.flagConfirm=true
                            finish()

                        }
                        return true
                    }
                }else
                {


                    if (prefManager.readFlag() == 1){
                        return if (str == pattern){
                            prefManager.flag(0)
                            prefManager.flag1(0)
                            finish()
                            true
                        }
                        else{
                            Toast.makeText(applicationContext, "Wrong Pattern", Toast.LENGTH_LONG)
                                .show()
                            false
                        }
                    }
                    else
                    {
                    if (str == pattern) {
                        Intent(applicationContext, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                        return true
                    } else {
                        Toast.makeText(applicationContext, "Wrong Pattern", Toast.LENGTH_LONG)
                            .show()
                        return false
                    }}
                } } })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
      /*  prefManager.flag(0)
        prefManager.flag1(0)
        prefManager.flag2(1)*/
       // super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.item11 -> {
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
                true
            }
            R.id.item22 -> {
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
                true
            }
            R.id.item33 -> {
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}