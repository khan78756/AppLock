package com.example.applock.ui.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.applock.R
import com.example.applock.databinding.ActivityPatternBinding
import com.example.applock.util.GlobalVariables
import com.example.applock.util.SharedPreferenceManager
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.VideoConfiguration
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.ads.nativead.NativeAd
import com.huawei.hms.ads.nativead.NativeAdConfiguration
import com.huawei.hms.ads.nativead.NativeAdLoader
import com.itsxtt.patternlock.PatternLockView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class PatternActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPatternBinding
    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }


    private lateinit var adScrollView: ScrollView
    private var globalNativeAd: NativeAd? = null
    @SuppressLint("SetTextI18n")
    val buzyCursor=loadingDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          binding= ActivityPatternBinding.inflate(layoutInflater)
        setContentView(binding.root)




        //BannerAdd Load
        loadBannerAdd()

       //lOAD Native Add
        adScrollView = findViewById(R.id.scroll_view_ad)
        var adID="testr6w14o0hqz"
        var adId2="testu7m3hc4gvm"
        loadNativeAd(adId2)




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
                           // Toast.makeText(applicationContext, "Wrong Pattern", Toast.LENGTH_LONG)
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
                        buzyCursor.startLoading()
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

    override fun onPause() {
        super.onPause()
        prefManager.flag(0)
        prefManager.flag1(0)
    }


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


    private fun loadBannerAdd(){
        var bannerView: BannerView? = findViewById(R.id.hw_banner_view)
        bannerView!!.adId = "testw6vs28auh3"
        bannerView!!.bannerAdSize = BannerAdSize.BANNER_SIZE_360_57
        bannerView!!.setBannerRefresh(60)
        val adParam = AdParam.Builder().build()
        bannerView!!.loadAd(adParam)
        bannerView!!.adListener = adListener
        bannerView!!.setBannerRefresh(60)
    }

    private val adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            // Called when an ad is loaded successfully.
          //Toast.makeText(applicationContext,"Successfully Loaded",Toast.LENGTH_LONG).show()
        }

        override fun onAdFailed(errorCode: Int) {
            // Called when an ad fails to be loaded.
          //  Toast.makeText(applicationContext,"Failed to Load Add",Toast.LENGTH_LONG).show()
        }

        override fun onAdOpened() {
            // Called when an ad is opened.
          //  Toast.makeText(applicationContext,"Add is opened",Toast.LENGTH_LONG).show()
        }

        override fun onAdClicked() {
            // Called when a user taps an ad.

           // Toast.makeText(applicationContext,"Add Clicked",Toast.LENGTH_LONG).show()
        }

        override fun onAdLeave() {
            // Called when a user has left the app.

          //  Toast.makeText(applicationContext,"Add leave",Toast.LENGTH_LONG).show()
        }

        override fun onAdClosed() {
            // Called when an ad is closed.
          //  Toast.makeText(applicationContext,"Add Closed",Toast.LENGTH_LONG).show()
        }
    }

    private fun loadNativeAd(adId: String) {

        val builder = NativeAdLoader.Builder(this, adId)
        builder.setNativeAdLoadedListener { nativeAd -> // Call this method when an ad is successfully loaded.
               //  Toast.makeText(applicationContext,"Successfully Loaded add",Toast.LENGTH_LONG).show()
            // Display native ad.
            showNativeAd(nativeAd)
        }.setAdListener(object : AdListener() {
            override fun onAdLoaded() {

            }

            override fun onAdFailed(errorCode: Int) {

               // Toast.makeText(applicationContext,"Failed to Load add",Toast.LENGTH_LONG).show()

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

    private fun createNativeView(nativeAd: NativeAd, parentView: ViewGroup): View? {
        val createType = nativeAd.creativeType
        if (createType == 2 || createType == 102) {
            // Large image
            return NativeViewFactory.createImageOnlyAdView(nativeAd, parentView)
        }
       else if (createType == 8 || createType == 108) {
            // Three small images with text
            return NativeViewFactory.createThreeImagesAdView(nativeAd, parentView)
        }
        else if (createType == 3 || createType == 6) {
            // Large image with text or video with text
          return  NativeViewFactory.createMediumAdView(nativeAd, parentView)
        }else {
            return null
        }
    }

}