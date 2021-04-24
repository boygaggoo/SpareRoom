package com.spareroom.android.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.spareroom.android.R


object Util {

    fun isConnected(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    fun loadImage(view: ImageView, url: String?, progressDrawable: CircularProgressDrawable?) {
        val options: RequestOptions = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher_round)
        Glide.with(view.context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(view)
    }

    fun getProgressDrawable(context: Context?): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.setStrokeWidth(10f)
        circularProgressDrawable.setCenterRadius(50f)
        circularProgressDrawable.start()
        return circularProgressDrawable
    }
}