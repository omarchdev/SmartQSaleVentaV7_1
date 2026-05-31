package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebimage.ConfigWebImageFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigWebImage : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_web_image_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigWebImageFragment.newInstance())
                    .commitNow()
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setTitle("Configuración web")
            getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}
