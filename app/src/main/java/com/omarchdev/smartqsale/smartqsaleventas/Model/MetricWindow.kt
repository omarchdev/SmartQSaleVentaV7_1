package com.omarchdev.smartqsale.smartqsaleventas.Model

import kotlin.jvm.internal.Intrinsics
import android.app.Activity
import android.util.DisplayMetrics
import kotlin.NotImplementedError


class MetricWindow(activity: Activity?) : ScreenInterface {

    var activity=activity

    init {
        Intrinsics.checkParameterIsNotNull(activity, "activity")
    }

    override fun orientation(): Int {
        val sb = StringBuilder()
        sb.append("An operation is not implemented: ")
        sb.append("not implemented")
        throw NotImplementedError(sb.toString())
    }


    override fun width(): Float {
        return getScreenDimension(activity!!).widthPixels.toFloat() / getScreenDensity(activity!!)
    }

    override  fun height(): Float {
        return getScreenDimension(activity!!).heightPixels.toFloat() / getScreenDensity(activity!!)
    }




    fun Orientation(): Int {
        val resources =activity!!.resources
        Intrinsics.checkExpressionValueIsNotNull(resources, "activity.resources")
        return resources.configuration.orientation
    }

    private fun getScreenDimension(activity2: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val windowManager = activity2.windowManager
        Intrinsics.checkExpressionValueIsNotNull(windowManager, "activity.windowManager")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    private fun getScreenDensity(activity2: Activity): Float {
        val resources = activity2.resources
        Intrinsics.checkExpressionValueIsNotNull(resources, "activity.resources")
        return resources.displayMetrics.density
    }
}

interface ScreenInterface {
    fun height(): Float

    fun orientation(): Int

    fun width(): Float
}
