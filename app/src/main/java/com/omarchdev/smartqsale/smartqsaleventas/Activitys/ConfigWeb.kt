package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configweb.ConfigWebFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigWeb : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_web_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container,ConfigWebFragment.newInstance() )
                    .commitNow()
        }

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Configuración web")
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

}
