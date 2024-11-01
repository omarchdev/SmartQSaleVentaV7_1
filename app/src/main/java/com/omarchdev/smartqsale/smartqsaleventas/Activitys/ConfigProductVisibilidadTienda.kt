package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configproductvisibilidadtienda.ConfigProductVisibilidadTiendaFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigProductVisibilidadTienda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title=intent.getStringExtra("nombreProducto")
        setContentView(R.layout.config_product_visibilidad_tienda_activity)
        supportActionBar?.elevation=4f
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle( title)
        supportActionBar?.setSubtitle("Visibilidad por tienda")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigProductVisibilidadTiendaFragment.newInstance())
                    .commitNow()
        }
    }
}
