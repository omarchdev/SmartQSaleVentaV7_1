package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncReporteProductos{


    interface ListenerReporteVentasProducto{

        fun ResultadoVentasProductos(listadoProductos:List<mProduct>)
        fun ErrorConsultaProductosVentas()

    }


    fun ObtenerReporteVentasProductoMonto(fechaInicio:String,fechaFinal:String,listenerReporteVentasProducto: ListenerReporteVentasProducto){
         GlobalScope.launch  {
            var listaProductos:List<mProduct>?=BdConnectionSql.getSinglentonInstance().ObtenerProductosVentasMonto(fechaInicio,fechaFinal)
           launch(Dispatchers.Main){
                    when(listaProductos){
                        null->{
                            listenerReporteVentasProducto?.ErrorConsultaProductosVentas()
                        }else->{
                            listenerReporteVentasProducto?.ResultadoVentasProductos(listaProductos)
                        }
                    }
            }
        }

    }

    fun ObtenerReporteVentasProductoUnidades(fechaInicio:String,fechaFinal:String,listenerReporteVentasProducto: ListenerReporteVentasProducto){
         GlobalScope.launch  {
            var listaProductos:List<mProduct>?=BdConnectionSql.getSinglentonInstance().ObtenerProductosVentasCantidad(fechaInicio,fechaFinal)
            listaProductos?.size
           launch(Dispatchers.Main){
                when(listaProductos){
                    null->{
                        listenerReporteVentasProducto?.ErrorConsultaProductosVentas()
                    }else->{
                        listenerReporteVentasProducto?.ResultadoVentasProductos(listaProductos)
                     }
                }

            }
        }
    }

    fun ObtenerResumenVentasProgramadas(desde:String,hasta:String){

         GlobalScope.launch  {

            var acumulados= BdConnectionSql.getSinglentonInstance().ObtenerProductosProgramadosAcumulado(desde,hasta)
            var ref=BdConnectionSql.getSinglentonInstance().ObtenerProductosProgramadosAcumuladoSinReferencia(desde,hasta)
           launch(Dispatchers.Main){

            }
        }

    }

    fun ObtenerProductosVentasProgramadas(desde:String,hasta:String){

        var list=BdConnectionSql.getSinglentonInstance().ObtenerDetalleProductosProgramados(desde,hasta)


    }

}