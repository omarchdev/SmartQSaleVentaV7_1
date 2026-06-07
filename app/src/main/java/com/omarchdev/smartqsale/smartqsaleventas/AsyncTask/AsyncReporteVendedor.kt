package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.app.AlertDialog
import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ConfiRetrofitTimeOut
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedorProducto
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVendedorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal

class AsyncReporteVendedor(context: Context) {

    private var context = context
    private val generarPdfGenerator = PdfGenerator(context)
    private val html = Html()
    private var lista = ArrayList<mVendedorProducto>()
    private var texto = ""

    private val codeCia = GetJsonCiaTiendaBase64x3()
    private val retro = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .client(ConfiRetrofitTimeOut.okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val repository = retro.create(IVendedorRepository::class.java)

    private fun ObtenerNombreTienda(idTienda: Int): String {
        var nombreTienda = ""
        Constantes.Tiendas.tiendaList.forEach {
            if (it.idTienda == idTienda) {
                nombreTienda = it.nombreTienda
            }
        }
        return nombreTienda
    }

    private fun GenerarReporteVendedor(lista: List<mVendedorProducto>): String {
        html.limpiarHtml()
        html.AgregarTituloCentral(Constantes.Empresa.nombreTienda)
        html.AgregarTituloIntermedio("Reporte de Ventas por Vendedor")
        val listaCabeceras = ArrayList<String>()
        listaCabeceras.add("Cod Product")
        listaCabeceras.add("Nombre del producto")
        listaCabeceras.add("Unidades")
        listaCabeceras.add("Total ${Constantes.DivisaPorDefecto.SimboloDivisa}")

        var fila = ArrayList<String>()
        var tabla = ArrayList<ArrayList<String>>()
        var i = 0
        var tempTienda = 0
        var tempFecha = ""
        var tempVendedor = 0
        var totalVendedor = BigDecimal(0)

        tempFecha = lista.get(0).fechaProceso
        tempVendedor = lista.get(0).vendedor.idVendedor
        tempTienda = lista.get(0).idTienda

        html.SaltoLinea()
        html.AgregarSubtitulo(ObtenerNombreTienda(tempTienda))
        html.AgregarSubtitulo(lista.get(i).fechaProceso)
        html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")

        while (lista.size > i) {
            if (tempTienda == lista.get(i).idTienda) {
                if (tempFecha.equals(lista.get(i).fechaProceso)) {
                    if (tempVendedor == lista.get(i).vendedor.idVendedor) {
                        fila = ArrayList<String>()
                        totalVendedor = totalVendedor.add(lista.get(i).product.precioVenta)
                        fila.add("${lista.get(i).product.getcKey()}")
                        fila.add("${lista.get(i).product.getcProductName()}/${lista.get(i).product.descripcionVariante}")
                        fila.add("${String.format("%.2f", lista.get(i).product.getdQuantity())}")
                        fila.add("${String.format("%.2f", lista.get(i).product.precioVenta)}")
                        tabla.add(fila)
                        i++
                    } else {
                        fila = ArrayList<String>()
                        fila.add("Total")
                        fila.add("")
                        fila.add("")
                        fila.add("${String.format("%.2f", totalVendedor)}")
                        tabla.add(fila)
                        html.AgregarTabla(listaCabeceras, tabla)

                        tabla = ArrayList<ArrayList<String>>()
                        html.SaltoLinea()
                        totalVendedor = BigDecimal(0)
                        tempVendedor = lista.get(i).vendedor.idVendedor
                        html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")
                    }
                } else {
                    tempFecha = lista.get(i).fechaProceso
                    tempVendedor = lista.get(i).vendedor.idVendedor

                    fila = ArrayList<String>()
                    fila.add("Total")
                    fila.add("")
                    fila.add("")
                    fila.add("${String.format("%.2f", totalVendedor)}")
                    tabla.add(fila)
                    html.AgregarTabla(listaCabeceras, tabla)

                    tabla = ArrayList<ArrayList<String>>()
                    html.SaltoPagina()

                    totalVendedor = BigDecimal(0)
                    html.AgregarSubtitulo(lista.get(i).fechaProceso)
                    html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")
                }
            } else {
                fila = ArrayList<String>()
                fila.add("Total")
                fila.add("")
                fila.add("")
                fila.add("${String.format("%.2f", totalVendedor)}")
                tabla.add(fila)
                html.AgregarTabla(listaCabeceras, tabla)

                tabla = ArrayList<ArrayList<String>>()
                html.SaltoLinea()
                html.SaltoPagina()
                totalVendedor = BigDecimal(0)
                tempFecha = lista.get(i).fechaProceso
                tempVendedor = lista.get(i).vendedor.idVendedor
                tempTienda = lista.get(i).idTienda
                html.AgregarSubtitulo(ObtenerNombreTienda(tempTienda))
                html.AgregarSubtitulo(lista.get(i).fechaProceso)
                html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")
            }
        }
        fila = ArrayList<String>()
        fila.add("Total")
        fila.add("")
        fila.add("")
        fila.add("${String.format("%.2f", totalVendedor)}")
        tabla.add(fila)
        html.AgregarTabla(listaCabeceras, tabla)

        return html.ObtenerHtml()
    }

    fun ReporteTodasTiendas(fechaInit: String, fechaFind: String, tipoInforme: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            texto = ""
            lista.clear()
            try {
                val response = if (tipoInforme == 100) {
                    repository.GetReporteDetalleTodasTiendas(fechaInit, fechaFind, codeCia, TIPO_CONSULTA).execute()
                } else {
                    repository.GetReporteVendedorVentaTodasTiendasAcumulado(fechaInit, fechaFind, codeCia, TIPO_CONSULTA).execute()
                }
                
                val resultList = response.body()
                launch(Dispatchers.Main) {
                    if (resultList != null && resultList.isNotEmpty()) {
                        lista.addAll(resultList)
                        texto = GenerarReporteVendedor(lista)
                        generarPdfGenerator.GenerarPdf(texto, "ReporteVentasVendedor")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun ReporteVendedorPorTienda(fechaInit: String, fechaFin: String, idTienda: Int, idVendedor: Int, tipoReporte: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            texto = ""
            lista.clear()
            try {
                val response = if (tipoReporte == 200) {
                    repository.GetReporteVendedorVentaAcumulado(idVendedor, fechaInit, fechaFin, idTienda, codeCia, TIPO_CONSULTA).execute()
                } else {
                    repository.GetReporteVendedorVenta(idVendedor, fechaInit, fechaFin, idTienda, codeCia, TIPO_CONSULTA).execute()
                }
                
                val resultList = response.body()
                launch(Dispatchers.Main) {
                    if (resultList != null && resultList.isNotEmpty()) {
                        lista.addAll(resultList)
                        texto = GenerarReporteVendedor(lista)
                        generarPdfGenerator.GenerarPdf(texto, "ReportePorVendedor")
                    } else {
                        AlertDialog.Builder(context).setTitle("Advertencia")
                            .setMessage("No existen resultados para generar el reporte")
                            .setPositiveButton("Salir", null).create().show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}