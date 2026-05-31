package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listadozonaservicio.ListadoZonaServicioFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ListadoZonaServicio : ActivityParent() {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listado_zona_servicio_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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