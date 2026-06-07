package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsyncReporteProductos {

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iProductoRepository = retro.create(IProductoRepository::class.java)

    interface ListenerReporteVentasProducto {
        fun ResultadoVentasProductos(listadoProductos: List<mProduct>)
        fun ErrorConsultaProductosVentas()
    }

    fun ObtenerReporteVentasProductoMonto(fechaInicio: String, fechaFinal: String, listenerReporteVentasProducto: ListenerReporteVentasProducto) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val list = iProductoRepository.ObtenerProductosVentasMonto(codeCia, BASECONN.TIPO_CONSULTA, fechaInicio, fechaFinal).execute().body()
                launch(Dispatchers.Main) {
                    if (list == null) {
                        listenerReporteVentasProducto.ErrorConsultaProductosVentas()
                    } else {
                        listenerReporteVentasProducto.ResultadoVentasProductos(list)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerReporteVentasProducto.ErrorConsultaProductosVentas()
                }
            }
        }
    }

    fun ObtenerReporteVentasProductoUnidades(fechaInicio: String, fechaFinal: String, listenerReporteVentasProducto: ListenerReporteVentasProducto) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val list = iProductoRepository.ObtenerProductosVentasCantidad(codeCia, BASECONN.TIPO_CONSULTA, fechaInicio, fechaFinal).execute().body()
                launch(Dispatchers.Main) {
                    if (list == null) {
                        listenerReporteVentasProducto.ErrorConsultaProductosVentas()
                    } else {
                        listenerReporteVentasProducto.ResultadoVentasProductos(list)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerReporteVentasProducto.ErrorConsultaProductosVentas()
                }
            }
        }
    }

    fun ObtenerResumenVentasProgramadas(desde: String, hasta: String) {
        GlobalScope.launch {
            launch(Dispatchers.Main) {

            }
        }
    }

    fun ObtenerProductosVentasProgramadas(desde: String, hasta: String) {
    }
}