package com.digitalsamurai.gorastudiotest

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService


object NetworkAccess {

    public fun isNetworkConnected(context : Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
}