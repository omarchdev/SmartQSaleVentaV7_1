package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.bannerweb.BannerWebFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class BannerWeb : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banner_web_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, BannerWebFragment.newInstance())
                    .commitNow()
        }

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar!!.setTitle("Banner web en tienda")
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}