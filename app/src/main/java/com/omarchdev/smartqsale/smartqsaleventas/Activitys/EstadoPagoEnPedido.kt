package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.estadopagoenpedido.EstadoPagoEnPedidoFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class EstadoPagoEnPedido : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.estado_pago_en_pedido_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EstadoPagoEnPedidoFragment.newInstance())
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
