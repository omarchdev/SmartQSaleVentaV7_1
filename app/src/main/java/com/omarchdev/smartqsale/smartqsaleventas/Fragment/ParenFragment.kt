package com.omarchdev.smartqsale. smartqsaleventa.Fragment

import android.content.pm.ActivityInfo
import androidx.fragment.app.Fragment

class ParenFragment: Fragment() {

    override fun onStart() {
        super.onStart()
        activity?.requestedOrientation  = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}