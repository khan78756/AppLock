package com.example.applock.ui.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.applock.R
import com.example.applock.databinding.ActivityPatternBinding
import com.example.applock.util.GlobalVariables
import com.example.applock.util.SharedPreferenceManager
import com.itsxtt.patternlock.PatternLockView

class PatternActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPatternBinding

    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }



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
        binding.btnClear.setOnClickListener {
            editor.clear().commit()
            Intent(applicationContext, PatternActivity::class.java).also {
                startActivity(it)
            }

        }
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
                        if (str == pattern){
                        prefManager.flag(0)
                        prefManager.flag1(0)

                        finish()
                        return true
                        }
                        else{
                            Toast.makeText(applicationContext, "Wrong Pattern", Toast.LENGTH_LONG)
                                .show()
                            return false
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
}