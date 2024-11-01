package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebimage.ConfigWebImageFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigWebImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_web_image_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigWebImageFragment.newInstance())
                    .commitNow()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}
