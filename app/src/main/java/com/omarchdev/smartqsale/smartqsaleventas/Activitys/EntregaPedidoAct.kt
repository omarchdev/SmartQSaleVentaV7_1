package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle

import com.omarchdev.smartqsale.smartqsaleventas.R

class EntregaPedidoAct : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrega_pedido)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}