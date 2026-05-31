package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.pedidoreservaresumen.PedidoReservaResumenFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class PedidoReservaResumen : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedido_reserva_resumen_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setTitle("Resumen de pedidos")
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
            getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            getSupportActionBar()!!.setElevation(4f)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PedidoReservaResumenFragment.newInstance())
                    .commitNow()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}