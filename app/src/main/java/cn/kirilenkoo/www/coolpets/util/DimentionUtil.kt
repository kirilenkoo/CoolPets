package cn.kirilenkoo.www.coolpets.util

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

fun convertDp2Px(dp: Int, context: Context): Int{
    val resources = context.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}

fun getScreenWidth(): Int {
    var displaymetrics = DisplayMetrics()
    displaymetrics = Resources.getSystem().displayMetrics
    return displaymetrics.widthPixels
}