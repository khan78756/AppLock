package com.example.applock.Service

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.applock.ui.Activity.PatternActivity
import com.example.applock.util.SharedPreferenceManager

class CurrentAppService: AccessibilityService() {

    companion object {
        const val TAG = "CurrentAppService"
    }

    private val preferenceManager by lazy { SharedPreferenceManager(applicationContext) }
    private var currentFocusedPackage = ""


    override fun onServiceConnected() {
        Log.d(TAG, "Service Connected!")
    }

    override fun onAccessibilityEvent(event :AccessibilityEvent?) {
        if (event!!.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.packageName != null && event.className != null) {
                val componentName = ComponentName(event.packageName.toString(), event.className.toString())
                val activityInfo = getActivityName(componentName)
                activityInfo?.let { info ->
                    val tempPackageName = info.packageName.trim()
                    if (tempPackageName.isNotEmpty() && currentFocusedPackage != tempPackageName) {
                        currentFocusedPackage=tempPackageName
                        preferenceManager.specificData.putString("Lock",currentFocusedPackage).apply()

                        var getdat=preferenceManager.getInt

                        if (isPackageSaved(currentFocusedPackage)) {
                            // preferenceManager.flag.putInt("Flag",0).apply()
                            if (getdat == 0){
                              //  preferenceManager.flag.putInt("Flag",1).apply()
                                startActivity(
                                    Intent(applicationContext, PatternActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    })}
                        }
                        else {
                            if (currentFocusedPackage != preferenceManager.getData){
                                preferenceManager.specificData.clear().commit()


                            }}
                    }
                }
            }
        }
    }

    private fun isPackageSaved(pkg: String): Boolean {
        return preferenceManager.readAppsList().contains(pkg)
    }

    private fun getActivityName(componentName: ComponentName): ActivityInfo? {
        return try {
            packageManager.getActivityInfo(componentName, 0)
        }
        catch (e: PackageManager.NameNotFoundException) {
            // e.printStackTrace()
            null
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service Interrupted!")
    }
}