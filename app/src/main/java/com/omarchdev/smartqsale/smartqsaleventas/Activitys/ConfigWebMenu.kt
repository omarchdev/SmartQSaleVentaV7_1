package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebmenu.ConfigWebMenuFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigWebMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_web_menu_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigWebMenuFragment.newInstance())
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
