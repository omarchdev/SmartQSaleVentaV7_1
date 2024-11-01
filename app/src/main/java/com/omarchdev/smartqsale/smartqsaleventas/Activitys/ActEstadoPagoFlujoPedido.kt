package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.estadopagoflujopedido.EstadoPagoFlujoPedidoFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ActEstadoPagoFlujoPedido :ActivityParent(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.estado_pago_flujo_pedido_activity)
        if (savedInstanceState == null) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setTitle( "Estado de pago")
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EstadoPagoFlujoPedidoFragment.newInstance())
                    .commitNow()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

}
