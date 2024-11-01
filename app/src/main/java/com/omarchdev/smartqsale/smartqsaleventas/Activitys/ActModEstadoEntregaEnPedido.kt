package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.actmodestadoentregaenpedido.ActModEstadoEntregaEnPedidoFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ActModEstadoEntregaEnPedido : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_mod_estado_entrega_en_pedido_activity)
        if (savedInstanceState == null) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setTitle("Estado de entrega" )
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ActModEstadoEntregaEnPedidoFragment.newInstance())
                    .commitNow()

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

}
