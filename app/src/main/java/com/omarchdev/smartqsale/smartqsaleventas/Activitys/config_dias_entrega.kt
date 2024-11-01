package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configdiasentrega.ConfigDiasEntregaFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class config_dias_entrega : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_dias_entrega_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigDiasEntregaFragment.newInstance())
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
