package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.detallepedido.DetallePedidoFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class DetallePedidoAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_pedido_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DetallePedidoFragment.newInstance())
                    .commitNow()
        }
    }
}
