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
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView
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


        //========================================================================================//
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this)
        // Obtain BannerView.
        var bannerView: BannerView? = findViewById(R.id.hw_banner_view)
        // Set the ad unit ID and ad dimensions. "testw6vs28auh3" is a dedicated test ad unit ID.
        bannerView!!.adId = "testw6vs28auh3"
        bannerView!!.bannerAdSize = BannerAdSize.BANNER_SIZE_360_57
        // Set the refresh interval to 60 seconds.
        bannerView!!.setBannerRefresh(60)
        // Create an ad request to load an ad.
        val adParam = AdParam.Builder().build()
        bannerView!!.loadAd(adParam)
        //=================================================================================//

        //CHECK IF COME FROM OUTER SIDE THEN DESTROY THIS ACTIVITY

        //==================================================================================//

        //CHECK IF PASSWORD IS NOT ALREADY SET
        if(!GlobalVariables.flagConfirm){
            if (prefManager.readFlag2()==null || prefManager.readFlag2()=="")
                binding.tvPattern.text="Draw New Password"
            else{
                binding.tvPattern.text="Enter Password"
            }
        }
        else{
            binding.tvPattern.text="Confirm Password"
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
               val str = prefManager.readFlag2()


                //IN CASE OF NEW PASSWORD ENTERING
                if(prefManager.readFlag2() ==null || prefManager.readFlag2() == "")
                {
                    Intent(applicationContext, PatternActivity::class.java).also {
                        startActivity(it)

                        /*editor.apply {
                            putString("Lock", pattern)
                            apply()


                        }*/
                        prefManager.flag2(pattern)
                        GlobalVariables.flagConfirm=true


                        finish()
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
                            trimCache(applicationContext)
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
                Toast.makeText(this,"FeedBack",Toast.LENGTH_LONG).show()
                true
            }
            R.id.item22 -> {
                Toast.makeText(this,"Help",Toast.LENGTH_LONG).show()
                true
            }
            R.id.item33 -> {
                Toast.makeText(this,"About",Toast.LENGTH_LONG).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun trimCache(context: Context) {
        try {
            val dir = context.cacheDir
            if (dir != null && dir.isDirectory) {
                deleteDir(dir)
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }

        // The directory is now empty so delete it
        return dir!!.delete()
    }



}