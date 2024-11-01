package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.pedidoreservaresumen.PedidoReservaResumenFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class PedidoReservaResumen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pedido_reserva_resumen_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =  "Resumen de pedidos"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.elevation = 4f

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