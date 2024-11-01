package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listadozonaservicio.ListadoZonaServicioFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ListadoZonaServicio : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_zona_servicio_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Zonas de servicio"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListadoZonaServicioFragment.newInstance())
                .commitNow()
        }
    }
}