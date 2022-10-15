package com.example.applock.ui.Activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.applock.R
import com.huawei.hms.ads.AppDownloadButton
import com.huawei.hms.ads.AppDownloadButtonStyle
import com.huawei.hms.ads.VideoOperator.VideoLifecycleListener
import com.huawei.hms.ads.nativead.MediaView
import com.huawei.hms.ads.nativead.NativeAd
import com.huawei.hms.ads.nativead.NativeView

class NativeViewFactory {

    companion object {

        private val TAG = NativeViewFactory::class.java.simpleName

        fun createThreeImagesAdView(nativeAd: NativeAd, parentView: ViewGroup): View? {
            val inflater = LayoutInflater.from(parentView.context)
            val adRootView: View = inflater.inflate(R.layout.native_three_images_template, null)
            val nativeView: NativeView = adRootView.findViewById(R.id.native_three_images)
            nativeView.titleView = adRootView.findViewById(R.id.ad_title)
            nativeView.adSourceView = adRootView.findViewById(R.id.ad_source)
            nativeView.callToActionView = adRootView.findViewById(R.id.ad_call_to_action)
            val imageView1 = adRootView.findViewById<ImageView>(R.id.image_view_1)
            val imageView2 = adRootView.findViewById<ImageView>(R.id.image_view_2)
            val imageView3 = adRootView.findViewById<ImageView>(R.id.image_view_3)

            // Populate a native ad material view.
            (nativeView.titleView as TextView).text = nativeAd.title
            if (null != nativeAd.adSource) {
                (nativeView.adSourceView as TextView).text = nativeAd.adSource
            }
            nativeView.adSourceView.visibility =
                if (null != nativeAd.adSource) View.VISIBLE else View.INVISIBLE
            if (null != nativeAd.callToAction) {
                (nativeView.callToActionView as Button).text = nativeAd.callToAction
            }
            nativeView.callToActionView.visibility =
                if (null != nativeAd.callToAction) View.VISIBLE else View.INVISIBLE
            if (nativeAd.images != null && nativeAd.images.size >= 3) {
                imageView1.setImageDrawable(nativeAd.images[0].drawable)
                imageView2.setImageDrawable(nativeAd.images[1].drawable)
                imageView3.setImageDrawable(nativeAd.images[2].drawable)
            }

            // Register a native ad object.
            nativeView.setNativeAd(nativeAd)
            return nativeView
        }



    }

    /**
     * Custom AppDownloadButton Style
     */
    private class MyAppDownloadStyle(context: Context) :
        AppDownloadButtonStyle(context) {
        init {
            normalStyle.textColor = context.resources.getColor(R.color.white)
            normalStyle.background = context.resources.getDrawable(R.drawable.native_button_rounded_corners_shape)
            processingStyle.textColor = context.resources.getColor(R.color.black)
        }
    }

}