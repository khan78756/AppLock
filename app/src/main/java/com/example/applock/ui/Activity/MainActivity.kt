package com.example.applock.ui.Activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.accessibility.AccessibilityManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.applock.R
import com.example.applock.Service.CurrentAppService
import com.example.applock.databinding.ActivityMainBinding
import com.example.applock.ui.Adapter.AppListAdapter
import com.example.applock.util.SharedPreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.ads.splash.SplashView
import java.io.File


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }




    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //=========================================================================================//
        val appAdapter = AppListAdapter(applicationContext, prefManager.readAppsList())
        binding.rvApps.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = appAdapter
            setItemViewCacheSize(100)
        }




        //========================================================================================//
        // Initialize the HUAWEI Ads SDK.
       // HwAds.init(this)
        // Obtain BannerView.
/*
        var bannerView: BannerView? = findViewById(R.id.hw_banner_view)
        // Set the ad unit ID and ad dimensions. "testw6vs28auh3" is a dedicated test ad unit ID.
        bannerView!!.adId = "testw6vs28auh3"
        bannerView!!.bannerAdSize = BannerAdSize.BANNER_SIZE_360_57
        // Set the refresh interval to 60 seconds.
        bannerView!!.setBannerRefresh(60)
        // Create an ad request to load an ad.
        val adParam = AdParam.Builder().build()
        bannerView!!.loadAd(adParam)
*/




        //============================================================================================//

        binding.fabSave.setOnClickListener {
            prefManager.saveAppsList(appAdapter.getCheckedApps())

            Intent(this,SplashActivity::class.java).also {
                startActivity(it)
                finish()
            }

           /* runAfterDelay(800L) {
                Toast.makeText(this,"Successfully Locked",Toast.LENGTH_LONG).show()
            }*/
        }

    }

    override fun onResume() {
        super.onResume()
        if (!isServiceEnabled()) {
            promptServiceOff()
        }
        else if (isRunningMiui()) {
            promptExtraMiuiPermission()
        }
    }

    override fun onStop() {
        trimCache(this)
        super.onStop()
    }

    private fun promptServiceOff() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(com.example.applock.R.string.alert_title))
            setMessage(getString(com.example.applock.R.string.alert_message))
            setPositiveButton(getString(com.example.applock.R.string.alert_enable)) { _, _ ->
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            setCancelable(false)
        }.show()
    }

    private fun isServiceEnabled(): Boolean {
        val am = applicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (service in enabledServices) {
            if (service.resolveInfo.serviceInfo.name.contains(CurrentAppService::class.qualifiedName.toString())) {
                return true
            }
        }
        return false
    }

    @SuppressLint("PrivateApi")
    private fun isRunningMiui(): Boolean {
        val c = Class.forName("android.os.SystemProperties")
        val get = c.getMethod("get", String::class.java)
        val miui = get.invoke(c, "ro.miui.ui.version.name") as String?
        return (miui != null && miui.isNotEmpty())
    }

    private fun promptExtraMiuiPermission() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(com.example.applock.R.string.alert_miui_title))
            setMessage(getString(com.example.applock.R.string.alert_miui_message))
            setPositiveButton(com.example.applock.R.string.alert_enable) { _, _ ->
                Toast.makeText(applicationContext, getString(com.example.applock.R.string.toast_miui_alert), Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
               /* Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }, 5L)*/
                Toast.makeText(applicationContext, com.example.applock.R.string.toast_miui_alert, Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                })
              /*  runAfterDelay(5L) {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    })
                }*/
            }
        }.show()
    }

    private fun runAfterDelay(delay: Long, methodToRun: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            methodToRun()
        }, delay)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val builder= AlertDialog.Builder(this)
        builder.setPositiveButton("Yes"){_,_ ->
          super.onBackPressed()
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Exit")
        builder.setIcon(com.example.applock.R.drawable.exitre)
        builder.setMessage("Are you sure you want to Exit?")
        builder.create().show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(com.example.applock.R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.item1 -> {
                Toast.makeText(this,"Feedback",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item2 -> {
                Toast.makeText(this,"Help",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item3 -> {
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item4 -> {
                val builder= AlertDialog.Builder(this)
                builder.setPositiveButton("Yes"){_,_ ->
                    finish()
                }
                builder.setNegativeButton("No"){_,_ ->}
                builder.setTitle("Exit")
                builder.setIcon(com.example.applock.R.drawable.exitre)
                builder.setMessage("Are you sure you want to Exit?")
                builder.create().show()
                true
            }

            R.id.item5 -> {
                prefManager.flag2("")
                Intent(applicationContext, PatternActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
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