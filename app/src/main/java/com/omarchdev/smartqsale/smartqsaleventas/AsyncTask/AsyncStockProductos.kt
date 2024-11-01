package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.AlmacenProducto
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncStockProductos(){

    public var listenerListadoProductoStock:ListenerListadoProductoStock?=null
    public var listenerStockAlmacenes:ListenerStockAlmacenes?=null

    interface ListenerListadoProductoStock{
        fun ListadoResultado(lista:ArrayList<mProduct>)
    }

    interface ListenerStockAlmacenes{

        fun ListadoStockAlmacenes(lista:ArrayList<AlmacenProducto>)

    }

    fun ObtenerStockProductoAlmacenes(id:Int){

         GlobalScope.launch  {

            var lista=BdConnectionSql.getSinglentonInstance().ObtenerCantidadPorAlmacen(id)
           launch(Dispatchers.Main){
                listenerStockAlmacenes?.ListadoStockAlmacenes(ArrayList(lista))

            }

        }

    }

    fun ObtenerProductosStock(){

         GlobalScope.launch  {

            var listado=BdConnectionSql.getSinglentonInstance().ObtenerStockProductos()
           launch(Dispatchers.Main){
                listenerListadoProductoStock?.ListadoResultado(ArrayList(listado))
            }
        }

    }

    fun ObtenerProductosConTexto(texto:String){
         GlobalScope.launch  {

            var listado=BdConnectionSql.getSinglentonInstance(). ObtenerStockProductosFiltroTexto(texto)
           launch(Dispatchers.Main){
                listenerListadoProductoStock?.ListadoResultado(ArrayList(listado))
            }
        }
    }

}