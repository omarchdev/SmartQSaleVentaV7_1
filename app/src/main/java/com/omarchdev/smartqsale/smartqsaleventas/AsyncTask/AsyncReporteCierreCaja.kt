package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import android.graphics.Bitmap
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICierreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal


class AsyncReporteCierreCaja(context:Context){

    val context=context
    private var bmpMediosPago:Bitmap?=null
    private var bmpProd:Bitmap?=null
    private var bmpCierres:Bitmap?=null
    private var bmpVentasHora:Bitmap?=null
    val pdfGenerator=PdfGenerator(context)
    var listenerRecuperarReporteCierre:ListenerRecuperarReporteCierre?=null
    private val controladorProcesoCargar= ControladorProcesoCargar(context)
    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iCierreRepository = retro.create(ICierreRepository::class.java)
    interface ListenerRecuperarReporteCierre {

        fun ReporteGraficos(productList: ArrayList<ProductoEnVenta>?,ultimosCierres:ArrayList<mCierre>?)
    }


    private fun ArmarReporte(monto:BigDecimal, productList: List<ProductoEnVenta>?, resumenMedioPago:List<mResumenFlujoCaja>?, listaVentasDocumento:List<VentaDocumento>?, listaRetirosCaja:List<mDetalleMovCaja>?,totalDescuento:BigDecimal,bmpMediosPago: Bitmap,bmpProd: Bitmap,bmpCierres: Bitmap,cierre:mCierre,bmpVentasHora: Bitmap):String{
        var lista1=ArrayList<ArrayList<String>>()
        var lista2:ArrayList<String>
        var listaA=ArrayList<ArrayList<String>>()
        var listaB:ArrayList<String>
        var listaTextoApertura=ArrayList<String>()
        var titulo1=ArrayList<String>()
        var titulo2=ArrayList<String>()
        val html= Html()
        html.AgregarTituloCentral("${Constantes.Empresa.nombreTienda}")
        html.AgregarSubtitulo("Informe de Cierre")

        listaTextoApertura.add("${cierre.estadoCierre}")
        listaTextoApertura.add("${cierre.getcFechaApertura()} - ${cierre.getcFechaCierre()}")

        html.AgregarDiv(listaTextoApertura)
        listaTextoApertura.clear()

        listaTextoApertura.add("Monto de Apertura")
        listaTextoApertura.add("${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",monto)}")
         html.AgregarDiv(listaTextoApertura)


        resumenMedioPago?.forEach {
            lista2 = ArrayList()
            if(it.codtitulo.equals("1")) {

                lista2.add(it.descripcionTitulo)
                lista2.add("${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f", it.monto)}")
            }
            lista1.add(lista2)
        }
        listaVentasDocumento?.forEach {
            listaB= ArrayList()
            listaB.add(it.descripcionDocumento)
            listaB.add(it.cantidad.toString())
            listaB.add("${String.format("%.2f",it.monto)}")
            listaA.add(listaB)
        }

        titulo1.add("Descripcion")
        titulo1.add("Monto ${Constantes.DivisaPorDefecto.SimboloDivisa}")

        titulo2.add("Descripcion")
        titulo2.add("Cantidad")
        titulo2.add("Monto ${Constantes.DivisaPorDefecto.SimboloDivisa}")

        html.AgregarDiv2ColumnasTablas("Ventas por Forma de Pago",titulo1,lista1,"Documentos por Venta",titulo2,listaA)

        html.SaltoLinea()
        html.SaltoLinea()

        listaA.clear()
        lista1.clear()
        titulo1.clear()
        titulo2.clear()

        listaRetirosCaja?.forEach {
            lista2= ArrayList()
            lista2.add(it.descripcion)
            lista2.add(it.cantidadMov.toString())
            lista2.add("${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",it.monto)}")
            lista1.add(lista2)
        }
        titulo1.add("Descripcion")
        titulo1.add("Cantidad")
        titulo1.add("Monto")

        listaB= ArrayList()
        listaB.add("${String.format("%.2f",totalDescuento)}")
        listaA.add(listaB)

        titulo2.add("Total Descuento ${Constantes.DivisaPorDefecto.SimboloDivisa}")

        html.AgregarDiv2ColumnasTablas("Retiros de Caja",titulo1,lista1,"Descuentos en Ventas",titulo2,listaA)

        listaA.clear()
        lista1.clear()
        titulo1.clear()
        titulo2.clear()
        var totalDescuento=BigDecimal(0)
        var totalIgv=BigDecimal(0)
        var totalVentas=BigDecimal(0)
        productList?.forEach {

            lista2 = ArrayList()
            lista2.add(it.descripcionCategoria+"/"+it.descripcionSubCategoria)

            totalDescuento=totalDescuento.add(it.montoDescuento)
            totalIgv=totalIgv.add(it.montoIgv)
            totalVentas=totalVentas.add(it.precioVentaFinal)
            lista2.add("${it.descripcionCombo}/ ${it.productName}/ ${it.descripcionVariante}")
            lista2.add("${String.format("%.2f", it.cantidad)}")
            lista2.add("${String.format("%.2f", it.montoIgv)}")
            lista2.add("${String.format("%.2f", it.montoDescuento)}")
            lista2.add("${String.format("%.2f", it.precioVentaFinal)}")
            lista1.add(lista2)

        }
        lista2 = ArrayList()
        lista2.add("Total")
        lista2.add("")
        lista2.add("")
        lista2.add("${String.format("%.2f",totalIgv)}")
        lista2.add("${String.format("%.2f",totalDescuento)}")
        lista2.add("${String.format("%.2f",totalVentas)}")
        lista1.add(lista2)
        titulo1.add("Categoria/ SubCategoria ")

        titulo1.add("Nombre Producto")

        titulo1.add("Cantidad")
        titulo1.add("IGV ${Constantes.DivisaPorDefecto.SimboloDivisa}")
        titulo1.add("Descuento ${Constantes.DivisaPorDefecto.SimboloDivisa}")
        titulo1.add("Total Venta ${Constantes.DivisaPorDefecto.SimboloDivisa}")

        html.SaltoPagina()


        html.AgregarDiv2ColumnasImagenes(bmpMediosPago,"Monto recaudado por medio de Pago",bmpProd,"Top 10 Productos mas Vendidos")

        html.AgregarDiv2ColumnasImagenes(bmpVentasHora,"Acumulado de ventas por hora",bmpCierres,"Comparativo de 10 últimos cierres de caja")

        html.SaltoPagina()
        html.AgregarSubtitulo("Productos Vendidos")

        html.AgregarTabla(titulo1,lista1)


        return html.ObtenerHtml()
    }

    fun ObtenerReporteCierreId(idCierre:Int,bmpMediosPago:Bitmap,bmpProd: Bitmap,bmpCierres:Bitmap,bmpVentasHora:Bitmap){
        controladorProcesoCargar.IniciarDialogCarga("Generando Reporte")
        this.bmpVentasHora=bmpVentasHora;
        this.bmpMediosPago=bmpMediosPago
        this.bmpProd=bmpProd
        this.bmpCierres=bmpCierres

        GlobalScope.launch(Dispatchers.Default)  {
            var texto=""
            val montoApertura=BdConnectionSql.getSinglentonInstance().ObtenerMontoAperturaCierre(idCierre)
            val productList: List<ProductoEnVenta>? = BdConnectionSql.getSinglentonInstance(). ObtenerAcumuladoVentasPorCierreMonto(idCierre)
            var resumenMedioPago=BdConnectionSql.getSinglentonInstance(). getResumenFlujoCaja(idCierre)
            var listaVentasDocumento:List<VentaDocumento>?=BdConnectionSql.getSinglentonInstance().VentasPorDocumentoCierre(idCierre)
            var listaRetirosCaja:List<mDetalleMovCaja>?=BdConnectionSql.getSinglentonInstance().RetirosCajaPorCierre(idCierre)
            var totalDescuento=BdConnectionSql.getSinglentonInstance().TotalDescuentoCierre(idCierre)
            var cierre=BdConnectionSql.getSinglentonInstance().CabeceraCierre(idCierre)
            texto=ArmarReporte(montoApertura,productList,
                    resumenMedioPago,listaVentasDocumento,listaRetirosCaja,
                    totalDescuento,bmpMediosPago,bmpProd,bmpCierres,cierre,bmpVentasHora)

            launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()

                pdfGenerator.GenerarPdf(texto,"ReporteCierreCaja")
            }

        }


    }


    fun ObtenerDatosResumenCaja(idCierre:Int){
        var productList: List<ProductoEnVenta>?=ArrayList()
        var listUltimosCierres:List<mCierre>?=ArrayList()
         GlobalScope.launch  {
            try {
                productList= iCierreRepository.ObtenerAcumuladoVentasPorCierreMontoTop10(idCierre, BASECONN.TIPO_CONSULTA,codeCia).execute().body()
                listUltimosCierres= iCierreRepository.ComparativoCierresUltimos10(idCierre, BASECONN.TIPO_CONSULTA,codeCia).execute().body()
            }catch (ex:Exception){

            }
         launch(Dispatchers.Main){
                if(productList!=null) {
                    listenerRecuperarReporteCierre?.ReporteGraficos(ArrayList(productList),ArrayList(listUltimosCierres))
                }else{
                    listenerRecuperarReporteCierre?.ReporteGraficos(ArrayList(),ArrayList())
                }

            }

        }
    }

}