package com.omarchdev.smartqsale.smartqsaleventas.Controles

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.github.javiersantos.bottomdialogs.BottomDialog
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.DetallePedido
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ParamActivitys
import com.omarchdev.smartqsale.smartqsaleventas.R


class Notification(context: Context,activity:Activity?=null) {

    private val context=context
    private val activity=activity

    fun notificationPedidoUnico(message:String,idPedido:Int){

        BottomDialog.Builder(context)
                .setTitle("Advertencia")
                .setContent(message)
                .setPositiveText("Ver pedido")
                .onPositive {
                    it.dismiss()
                    val intent= Intent(context,DetallePedido::class.java)
                    intent.putExtra(ParamActivitys.PARAM_IDPEDIDO,idPedido)
                    intent.putExtra(ParamActivitys.PARAM_ESTADO_PEDIDO_PAGADO,false)
                    context.startActivity(intent)
                }
                .setNegativeText("Salir")
                .setPositiveBackgroundColor(R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                .setCancelable(false)
                .show()

    }

    fun notificacionPedidosMultiples(message:String){
        BottomDialog.Builder(context)
                .setTitle("Nuevos pedidos")
                .setContent(message)
                .setNegativeText("Salir")
                .setCancelable(false)
                .show()
    }
}