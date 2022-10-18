package com.example.applock.ui.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.applock.R
import com.example.applock.databinding.ActivityPatternBinding
import com.example.applock.util.GlobalVariables
import com.example.applock.util.SharedPreferenceManager
import com.huawei.hms.ads.*
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.ads.nativead.*
import com.itsxtt.patternlock.PatternLockView
import java.io.File


class PatternActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPatternBinding
    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }


    private lateinit var adScrollView: ScrollView
    private var globalNativeAd: NativeAd? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          binding= ActivityPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adScrollView = findViewById(R.id.scroll_view_ad)
        var adID="testr6w14o0hqz"
        loadAd(adID)


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
                if(prefManager.readFlag2() == null || prefManager.readFlag2() == "")
                {
                    Intent(applicationContext, PatternActivity::class.java).also {
                        startActivity(it)
                        prefManager.flag2(pattern)
                        GlobalVariables.flagConfirm=true
                        overridePendingTransition(R.anim.sliderightin,R.anim.slideoutleft)


                        finish()
                        return true
                    }
                }else
                {
                    if (prefManager.readFlag() == 1){
                        return if (str == pattern){
                            prefManager.flag(0)
                            prefManager.flag1(0)
                            Toast.makeText(applicationContext, "Wrong Pattern", Toast.LENGTH_LONG)
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
                           // trimCache(applicationContext)
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


    /**
     * Load a native ad.
     *
     * @param adId ad slot ID.
     */
    private fun loadAd(adId: String) {
        val builder = NativeAdLoader.Builder(this, adId)
        builder.setNativeAdLoadedListener { nativeAd -> // Call this method when an ad is successfully loaded.

            // Display native ad.
            showNativeAd(nativeAd)
        }.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailed(errorCode: Int) {
                // Call this method when an ad fails to be loaded.
            }
        })
        val videoConfiguration = VideoConfiguration.Builder()
            .setStartMuted(true)
            .build()
        val adConfiguration = NativeAdConfiguration.Builder()
            .setChoicesPosition(NativeAdConfiguration.ChoicesPosition.BOTTOM_RIGHT) // Set custom attributes.
            .setVideoConfiguration(videoConfiguration)
            .setRequestMultiImages(true)
            .build()
        val nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build()
        nativeAdLoader.loadAd(AdParam.Builder().build())
    }

    /**
     * Display native ad.
     *
     * @param nativeAd native ad object that contains ad materials.
     */
    private fun showNativeAd(nativeAd: NativeAd) {
        // Destroy the original native ad.
        if (null != globalNativeAd) {
            globalNativeAd!!.destroy()
        }
        globalNativeAd = nativeAd
        val nativeView: View = createNativeView(nativeAd, adScrollView)!!
        if (nativeView != null) {
            globalNativeAd!!.setDislikeAdListener { // Call this method when an ad is closed
                adScrollView.removeView(nativeView)
            }

            // Add NativeView to the app UI.
            adScrollView.removeAllViews()
            adScrollView.addView(nativeView)
        }
    }

    /**
     * Create a nativeView by creativeType and fill in ad material.
     *
     * @param nativeAd   native ad object that contains ad materials.
     * @param parentView parent view of nativeView.
     */
    private fun createNativeView(nativeAd: NativeAd, parentView: ViewGroup): View? {
        val createType = nativeAd.creativeType
       if (createType == 8 || createType == 108) {
            // Three small images with text
            return NativeViewFactory.createThreeImagesAdView(nativeAd, parentView)
        } else {
            return null
        }
    }

}