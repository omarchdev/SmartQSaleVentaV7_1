package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ConfiRetrofitTimeOut
import com.omarchdev.smartqsale.smartqsaleventas.Model.AlmacenProducto
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IStockProductosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsyncStockProductos() {

    public var listenerListadoProductoStock: ListenerListadoProductoStock? = null
    public var listenerStockAlmacenes: ListenerStockAlmacenes? = null

    private val retro = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .client(ConfiRetrofitTimeOut.okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val repository = retro.create(IStockProductosRepository::class.java)
    private val ciaCode = GetJsonCiaTiendaBase64x3()

    interface ListenerListadoProductoStock {
        fun ListadoResultado(lista: ArrayList<mProduct>)
    }

    interface ListenerStockAlmacenes {
        fun ListadoStockAlmacenes(lista: ArrayList<AlmacenProducto>)
    }

    fun ObtenerStockProductoAlmacenes(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.ObtenerCantidadPorAlmacen(ciaCode, TIPO_CONSULTA, id).execute()
                val list = response.body() ?: emptyList()
                launch(Dispatchers.Main) {
                    listenerStockAlmacenes?.ListadoStockAlmacenes(ArrayList(list))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerStockAlmacenes?.ListadoStockAlmacenes(ArrayList())
                }
            }
        }
    }

    fun ObtenerProductosStock() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.ObtenerStockProductos(ciaCode, TIPO_CONSULTA).execute()
                val list = response.body() ?: emptyList()
                launch(Dispatchers.Main) {
                    listenerListadoProductoStock?.ListadoResultado(ArrayList(list))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerListadoProductoStock?.ListadoResultado(ArrayList())
                }
            }
        }
    }

    fun ObtenerProductosConTexto(texto: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.ObtenerStockProductosFiltroTexto(ciaCode, TIPO_CONSULTA, texto).execute()
                val list = response.body() ?: emptyList()
                launch(Dispatchers.Main) {
                    listenerListadoProductoStock?.ListadoResultado(ArrayList(list))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerListadoProductoStock?.ListadoResultado(ArrayList())
                }
            }
        }
    }
}