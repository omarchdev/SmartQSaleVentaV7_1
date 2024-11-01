package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.app.AlertDialog
import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedorProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal


class AsyncReporteVendedor(context: Context){

    private var context=context
    private val generarPdfGenerator=PdfGenerator(context)
    private val html=Html()
    private var lista=ArrayList<mVendedorProducto>()
    private var texto=""

    private fun ObtenerNombreTienda(idTienda:Int):String{
       var nombreTienda=""

        Constantes.Tiendas.tiendaList.forEach {

            if(it.idTienda==idTienda){
                nombreTienda=it.nombreTienda
            }

        }
        nombreTienda.length
        return nombreTienda
    }

    private fun GenerarReporteVendedor(lista:List<mVendedorProducto>):String{
        //Tipo Reporte= 100->Detalle      200->acumulado
        html.limpiarHtml()
        html.AgregarTituloCentral(Constantes.Empresa.nombreTienda)
        html.AgregarTituloIntermedio("Reporte de Ventas por Vendedor")
        val listaCabeceras=ArrayList<String>()
         listaCabeceras.add("Cod Product")
        listaCabeceras.add("Nombre del producto")
        listaCabeceras.add("Unidades")
        listaCabeceras.add("Total ${Constantes.DivisaPorDefecto.SimboloDivisa}")

        var fila=ArrayList<String>()
        var tabla=ArrayList<ArrayList<String>>()
        var i=0
        var tempTienda=0
        var tempFecha=""
        var tempVendedor=0
        var totalVendedor=BigDecimal(0)
        var totalFecha=BigDecimal(0)

        tempFecha=lista.get(0).fechaProceso
        tempVendedor=lista.get(0).vendedor.idVendedor
        tempTienda=lista.get(0).idTienda

        html.SaltoLinea()
        html.AgregarSubtitulo(ObtenerNombreTienda(tempTienda))
        html.AgregarSubtitulo(lista.get(i).fechaProceso)
        html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")

        while(lista.size>i){
            if(tempTienda==lista.get(i).idTienda){
                if(tempFecha.equals(lista.get(i).fechaProceso)){
                    if(tempVendedor==lista.get(i).vendedor.idVendedor){
                        fila= ArrayList<String>()
                        totalVendedor=totalVendedor.add(lista.get(i).product.precioVenta)
                        fila.add("${lista.get(i).product.getcKey()}")
                        fila.add("${lista.get(i).product.getcProductName()}/${lista.get(i).product.descripcionVariante}")
                        fila.add("${ String.format("%.2f",lista.get(i).product.getdQuantity())}")
                        fila.add("${String.format("%.2f",lista.get(i).product.precioVenta)}")
                        tabla.add(fila)
                        i++
                    }
                    else{

                        fila=ArrayList<String>()
                        fila.add("Total")
                        fila.add("")
                        fila.add("")
                        fila.add("${String.format("%.2f",totalVendedor)}")
                        tabla.add(fila)
                        html.AgregarTabla(listaCabeceras, tabla)

                        tabla=ArrayList<ArrayList<String>>()
                        html.SaltoLinea()
                        totalVendedor= BigDecimal(0)
                        tempVendedor=lista.get(i).vendedor.idVendedor
                        html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")
                    }

                }
                else{
                    tempFecha=lista.get(i).fechaProceso
                    tempVendedor=lista.get(i).vendedor.idVendedor

                    fila=ArrayList<String>()
                      fila.add("Total")
                    fila.add("")
                    fila.add("")
                    fila.add("${String.format("%.2f",totalVendedor)}")
                    tabla.add(fila)
                    html.AgregarTabla(listaCabeceras, tabla)

                    tabla=ArrayList<ArrayList<String>>()
                    html.SaltoPagina()

                      totalVendedor= BigDecimal(0)
                    html.AgregarSubtitulo(lista.get(i).fechaProceso)
                    html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")

                }
            }
            else{
                fila=ArrayList<String>()
                fila.add("Total")
                fila.add("")
                fila.add("")
                fila.add("${String.format("%.2f",totalVendedor)}")
                tabla.add(fila)
                  html.AgregarTabla(listaCabeceras,tabla)

                tabla=ArrayList<ArrayList<String>>()
                html.SaltoLinea()
                html.SaltoPagina()
               totalVendedor= BigDecimal(0)
                tempFecha=lista.get(i).fechaProceso
                tempVendedor=lista.get(i).vendedor.idVendedor
                tempTienda=lista.get(i).idTienda
                html.AgregarSubtitulo(ObtenerNombreTienda(tempTienda))
                html.AgregarSubtitulo(lista.get(i).fechaProceso)
                html.AgregarTituloIntermedio("${lista.get(i).vendedor.primerNombre} ")

            }
        }
        fila=ArrayList<String>()
        fila.add("Total")
        fila.add("")
        fila.add("")
        fila.add("${String.format("%.2f",totalVendedor)}")
        tabla.add(fila)
        html.AgregarTabla(listaCabeceras,tabla)

        return html.ObtenerHtml()
    }


    fun ReporteTodasTiendas(fechaInit:String, fechaFind:String, tipoInforme:Int){
        //tipoInforme 100-> detalle  // tipo 200->acumulado
        GlobalScope.launch {
           texto=""
            lista.clear()
            if(tipoInforme==100) {
                lista.addAll(BdConnectionSql.getSinglentonInstance().ReporteDetalleTodasTiendas(fechaInit, fechaFind))
            }
            else if(tipoInforme==200){
                lista.addAll(BdConnectionSql.getSinglentonInstance().
                        obtenerReporteVendedorVentaTodasTiendasAcumulado(fechaInit,fechaFind))
            }
            if(lista.size>0) {
               texto= GenerarReporteVendedor(lista)
            }
           launch(Dispatchers.Main){

                if(lista.size>0){
                    generarPdfGenerator.GenerarPdf(texto,"ReporteVentasVendedor")
                }else{

                }
            }
        }

    }

    fun ReporteVendedorPorTienda(fechaInit: String,fechaFin:String,idTienda: Int,idVendedor:Int,tipoReporte:Int){

        //tipo reporte 100->detalle 200 acumulado
         GlobalScope.launch {
            texto=""
            lista.clear()
            if(tipoReporte==200) {
                lista.addAll(BdConnectionSql.getSinglentonInstance().
                        obtenerReporteVendedorVentaAcumulado(idVendedor, fechaInit, fechaFin, idTienda))
            }else if(tipoReporte==100){

                lista.addAll(BdConnectionSql.getSinglentonInstance()
                        .obtenerReporteVendedorVenta(idVendedor,fechaInit,fechaFin,idTienda))

            }

            if(lista!=null){
                if(lista.size>0){
                    texto=GenerarReporteVendedor(lista)
                }
            }
            launch(Dispatchers.Main){
                if(lista!=null) {
                    if (lista.size > 0) {
                        generarPdfGenerator.GenerarPdf(texto,"ReportePorVendedor")
                    }else{
                        AlertDialog.Builder(context).setTitle("Advertencia")
                                .setMessage("No existen resultados para generar el reporte").
                                         setPositiveButton("Salir",null).create().show()
                    }
                }

            }
        }

    }

}