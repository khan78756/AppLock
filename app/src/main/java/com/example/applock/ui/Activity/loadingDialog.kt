package com.example.applock.ui.Activity

import android.app.Activity
import android.app.AlertDialog
import com.example.applock.R

class loadingDialog(val nActivity:Activity) {
    private lateinit var isdialog:AlertDialog
    fun startLoading(){
        //set view
        val inflater=nActivity.layoutInflater
        val dialogview=inflater.inflate(R.layout.progress_bar,null)
        //set dialog
        val builder=AlertDialog.Builder(nActivity)
        builder.setView(dialogview)
        builder.setCancelable(false)
        isdialog=builder.create()
        isdialog.show()

    }
    fun isdismiss(){
        isdialog.dismiss()
    }
}