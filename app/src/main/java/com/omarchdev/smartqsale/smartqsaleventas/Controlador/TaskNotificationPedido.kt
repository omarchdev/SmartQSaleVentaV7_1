package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ConfigTienda.iTiempoLecturaPedido
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ParamNameMap.*
import com.omarchdev.smartqsale.smartqsaleventas.Controles.Notification
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit



sealed class  Result<out R>{
    data class Success<out T>(val data:T):Result<T>()
    data class Error(val exception:Exception):Result<Nothing>()
}


class TaskNotificationPedido(context: Context,activity: Activity?=null) {

    private val context=context
    private val notificacion= Notification(context)

    interface IPedidosNotification{
        fun nuevosPedidosMultiples(mensajeResultado:String)
        fun nuevoPedidoUnico(idPedido:Int,mensajeResultado: String)


    }
    val executors=Executors.newSingleThreadScheduledExecutor()
    var iPedidosNotification:IPedidosNotification?=null
    private var inicioTarea=false

    fun taskPedidoNuevos(){

        if(iTiempoLecturaPedido>0){
            executors.scheduleAtFixedRate({
                try{
                    var response=getPedidosNuevos()
                    when(response){
                        is Result.Success<HashMap<String, Any>>->{
                            var result=  response.data
                            var tienePedidos:Boolean= result[TIENE_PEDIDO] as Boolean
                            var mensaje:String=result[MESSAGE_NOT].toString()
                            var nroPedidos:Int=result[NRO_PEDIDOS] as Int
                            var idPedido:Int=result[ID_PEDIDO] as Int
                            Handler(Looper.getMainLooper()).post {
                                if (tienePedidos) {
                                    if (nroPedidos == 1) {
                                        notificacion.notificationPedidoUnico(mensaje,idPedido)
                                        //    iPedidosNotification?.nuevoPedidoUnico(idPedido,mensaje)
                                    } else if (nroPedidos > 1) {
                                        //     iPedidosNotification?.nuevosPedidosMultiples(mensaje)
                                        notificacion.notificacionPedidosMultiples(mensaje)
                                    }
                                }
                            }
                        }
                    }
                }
                catch (ex:Exception){
                    Log.e("ERRORTEST",ex.toString())
                }


            }, 10, ((iTiempoLecturaPedido*60) as Int).toLong(), TimeUnit.SECONDS)

        }

    }

    fun cancelTask(){
        executors
    }

    fun getPedidosNuevos():Result<HashMap<String, Any>>{
       var r: HashMap<String, Any> ?= BdConnectionSql.getSinglentonInstance().GetPedidosNotificacion()

        return Result.Success(r!!)
    }
}