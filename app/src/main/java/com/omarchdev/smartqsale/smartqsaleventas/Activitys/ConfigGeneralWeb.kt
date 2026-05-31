package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configgeneralweb.ConfigGeneralWebFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigGeneralWeb : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_general_web_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigGeneralWebFragment.newInstance())
                    .commitNow()
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            getSupportActionBar()!!.setElevation(4f)
            getSupportActionBar()!!.setTitle("Configuración web")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
