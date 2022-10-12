package com.example.applock.Service

import android.app.Application
import com.huawei.hms.ads.HwAds

class AdSampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this)
    }

}