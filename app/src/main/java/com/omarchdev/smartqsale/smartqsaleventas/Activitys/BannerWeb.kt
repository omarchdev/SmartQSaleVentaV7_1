package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.bannerweb.BannerWebFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class BannerWeb : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banner_web_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, BannerWebFragment.newInstance())
                    .commitNow()
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            getSupportActionBar()!!.setTitle("Banner web en tienda")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}