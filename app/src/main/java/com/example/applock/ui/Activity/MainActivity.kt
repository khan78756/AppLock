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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.applock.R
import com.example.applock.Service.CurrentAppService
import com.example.applock.databinding.ActivityMainBinding
import com.example.applock.ui.Adapter.AppListAdapter
import com.example.applock.util.SharedPreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val appAdapter = AppListAdapter(applicationContext, prefManager.readAppsList())
        binding.rvApps.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = appAdapter
            setItemViewCacheSize(100)
        }

        binding.fabSave.setOnClickListener {
            prefManager.saveAppsList(appAdapter.getCheckedApps())
            runAfterDelay(800L) {
                Toast.makeText(this,"Successfully Locked",Toast.LENGTH_LONG).show()
            }
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

    override fun onDestroy() {
        super.onDestroy()
      trimCache(this)

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
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }, 500L)
                Toast.makeText(applicationContext, com.example.applock.R.string.toast_miui_alert, Toast.LENGTH_SHORT).show()
                runAfterDelay(500L) {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    })
                }
            }
        }.show()
    }

    private fun runAfterDelay(delay: Long, methodToRun: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            methodToRun()
        }, delay)
    }

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
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
                true
            }
            R.id.item2 -> {
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
                true
            }
            R.id.item3 -> {
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
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
                Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
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