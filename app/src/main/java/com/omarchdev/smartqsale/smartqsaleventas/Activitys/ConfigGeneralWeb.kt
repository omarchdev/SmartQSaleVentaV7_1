package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configgeneralweb.ConfigGeneralWebFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigGeneralWeb : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_general_web_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigGeneralWebFragment.newInstance())
                    .commitNow()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

       supportActionBar?.elevation = 4f
        supportActionBar?.setTitle( "Configuración web" )
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
