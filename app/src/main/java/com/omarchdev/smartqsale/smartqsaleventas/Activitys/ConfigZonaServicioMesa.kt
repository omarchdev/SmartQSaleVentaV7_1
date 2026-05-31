package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.main.ConfigZonaServicioMesaFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigZonaServicioMesa : ActivityParent() {



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_zona_servicio_mesa_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Configuración"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ConfigZonaServicioMesaFragment.newInstance())
                .commitNow()
        }
    }
}