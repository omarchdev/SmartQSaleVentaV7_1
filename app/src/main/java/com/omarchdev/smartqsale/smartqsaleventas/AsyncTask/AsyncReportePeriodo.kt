package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask
import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class AsyncReportePeriodo(context:Context){

    private var d=ControladorProcesoCargar(context)
    private var textoHtml=""
    private var generarPdfGenerator=PdfGenerator(context)

    private fun ReporteTiendaUnica(productos:ArrayList<ProductoEnVenta>,pagosDocumento:ArrayList<PagosDocPago>
                                   ,pagosPorTipo:ArrayList<PagoPorTipoPago>,descuentosPorTienda:ArrayList< DescuentoPorTienda>,movimientosTienda:ArrayList<MovCajaTienda>,
                                   fechaInicio:String,fechaFinal: String,idTienda: Int,totales:ArrayList<TotalPorTienda>):String{
        var html= Html()
        var cabecerasDescuento=ArrayList<String>()
        var cabecerasProductos=ArrayList<String>()
        var cabecerasTipoPago=ArrayList<String>()
        var cabecerasMovimientosCaja=ArrayList<String>()
        var cabecerasDocumentoPago=ArrayList<String>()
        var tablaIngreso=ArrayList<ArrayList<String>>()
        var tablaRetiro=ArrayList<ArrayList<String>>()
        var tablaDocPago=ArrayList<ArrayList<String>>()
        var tablaTipoPago=ArrayList<ArrayList<String>>()
        var tablaProductos=ArrayList<ArrayList<String>>()
        var tablaDescuento=ArrayList<ArrayList<String>>()
        var r=0
        var titulo=ArrayList<String>()
        var total=BigDecimal(0)

        var totalDescuento=0.bg
        var totalIgv=0.bg
        var totalCosto=0.bg
        var totalUtilidad=0.bg

        titulo.add("$fechaInicio - $fechaFinal")

        cabecerasDescuento.add("Total Descuento")

        cabecerasProductos.add("Fecha")
        cabecerasProductos.add("Codigo")
        cabecerasProductos.add("Descripción Producto")
        cabecerasProductos.add("Unidades")
        cabecerasProductos.add("Descuento")
        cabecerasProductos.add("IGV")
        cabecerasProductos.add("Venta Total")
        cabecerasProductos.add("Inversión Total")
        cabecerasProductos.add("Utilidad")
        cabecerasTipoPago.add("Descripcion")
        cabecerasTipoPago.add("Monto")

        cabecerasMovimientosCaja.add("Descripcion")
        cabecerasMovimientosCaja.add("Monto")

        cabecerasDocumentoPago.add("Descripcion")
        cabecerasDocumentoPago.add("Num Doc")
        cabecerasDocumentoPago.add("Monto")

        html.AgregarTituloCentral(Constantes.Empresa.nombreTienda)
        html.AgregarDiv(titulo)



        html.AgregarSubtitulo(ObtenerNombreTienda(idTienda))
        totales.filter {f->f.idTienda==idTienda  }.forEach {
            var titulo=ArrayList<String>()

            titulo.add("Total en ventas-Total Inversión-TotalIgv=Total Utilidad")
            titulo.add(it.total.fortMoneda+"-"+it.totalCosto.fortMoneda+"-"+it.totalIgv.fortMoneda+"="+it.totalUtilidad.fortMoneda)
            html.AgregarDiv(titulo)

        }


        movimientosTienda.filter { f->f.idTienda==idTienda }.forEach {
            if(it.motivo.tipoMotivo==1){
                var fila=ArrayList<String>()
                fila.add(it.motivo.descripcionMotivo)
                fila.add(it.monto.fortMoneda)
                tablaIngreso.add(fila)

            }
            else if(it.motivo.tipoMotivo==2){
                var fila=ArrayList<String>()
                fila.add(it.motivo.descripcionMotivo)
                fila.add(it.monto.fortMoneda)
                tablaRetiro.add(fila)

            }

        }

        html.AgregarDiv2ColumnasTablas("Ingresos",cabecerasMovimientosCaja,
                tablaIngreso,"Retiros",cabecerasMovimientosCaja,tablaRetiro)

        pagosDocumento.filter { f->f.idTienda==idTienda }.forEach {
           var fila=ArrayList<String>()
            fila.add(it.cDescripcion)
            fila.add(it.numeroDocumentos.toString())
            fila.add(it.monto.fortMoneda)
            tablaDocPago.add(fila)
        }

        pagosPorTipo.filter { f->f.idTienda==idTienda }.forEach {

            var fila=ArrayList<String>()
            fila.add(it.tipoPago.descripcionTipoPago)
            fila.add(it.monto.fortMoneda)
            tablaTipoPago.add(fila)
        }
        html.AgregarDiv2ColumnasTablas("Documentos de pago",cabecerasDocumentoPago,
                tablaDocPago,"Tipo de pago",cabecerasTipoPago,tablaTipoPago)

        descuentosPorTienda.filter { f->f.idTienda==idTienda }.forEach {

            var fila=ArrayList<String>()
            fila.add(it.monto.fortMoneda)
            tablaDescuento.add(fila)
        }
        html.SaltoPagina()
        html.AgregarSubtitulo("Descuentos")
        html.AgregarTabla(cabecerasDescuento,tablaDescuento)
        html.SaltoPagina()

        productos.filter { f->f.idTienda==idTienda }.forEach {
            var fila=ArrayList<String>()
            fila.add(it.fecha)
            fila.add(it.codigoProducto)
            fila.add(it.descripcionCategoria+" "+it.descripcionSubCategoria
                    +" "+it.productName+" "+it.descripcionVariante+Constantes.EHTML.Left)
            fila.add(it.cantidad.fUnidades.toString())
            fila.add(it.montoDescuento.fortMoneda)
            fila.add(it.montoIgv.fortMoneda)
            fila.add(it.precioVentaFinal.fortMoneda)
            fila.add(it.costoTotal.fortMoneda)
            fila.add(it.utilidad.subtract(it.montoIgv).fortMoneda)
            tablaProductos.add(fila)
            r++
            totalUtilidad=totalUtilidad.add(it.utilidad)
            totalCosto=totalCosto.add(it.costoTotal)
            totalDescuento=totalDescuento.add(it.montoDescuento)
            totalIgv=totalIgv.add(it.montoIgv)
            total=total.add(it.precioVentaFinal)
        }
        if(r>0){
            var fila=ArrayList<String>()
            fila.add("Total")
            fila.add("")
            fila.add("")
            fila.add("")
            fila.add(totalDescuento.fortMoneda)
            fila.add(totalIgv.fortMoneda)

            fila.add(total.fortMoneda)
            fila.add(totalCosto.fortMoneda)
            fila.add(totalUtilidad.subtract(totalIgv).fortMoneda)
            tablaProductos.add(fila)
        }

        html.AgregarSubtitulo("Productos Vendidos")
        html.AgregarTabla(cabecerasProductos,tablaProductos)


        return html.ObtenerHtml()
    }

    private fun GenerarReporteTodasTiendas(productos:ArrayList<ProductoEnVenta>,pagosDocumento:ArrayList<PagosDocPago>
    ,pagosPorTipo:ArrayList<PagoPorTipoPago>,descuentosPorTienda:ArrayList< DescuentoPorTienda>,movimientosTienda:ArrayList<MovCajaTienda>,
                                           fechaInicio:String,fechaFinal: String,totales:ArrayList<TotalPorTienda>):String{

            var html= Html()
        var cabecerasDescuento=ArrayList<String>()
        var cabecerasProductos=ArrayList<String>()
        var cabecerasTipoPago=ArrayList<String>()
        var cabecerasMovimientosCaja=ArrayList<String>()
        var cabecerasDocumentoPago=ArrayList<String>()
        var tablaIngreso=ArrayList<ArrayList<String>>()
        var tablaRetiro=ArrayList<ArrayList<String>>()
        var tablaDocPago=ArrayList<ArrayList<String>>()
        var tablaTipoPago=ArrayList<ArrayList<String>>()
        var tablaProductos=ArrayList<ArrayList<String>>()
        var tablaDescuento=ArrayList<ArrayList<String>>()
        var r=0
        var titulo=ArrayList<String>()

        titulo.add("$fechaInicio - $fechaFinal")

        cabecerasDescuento.add("Total Descuento")

        cabecerasProductos.add("Fecha")
        cabecerasProductos.add("Codigo")
        cabecerasProductos.add("Descripción Producto")
        cabecerasProductos.add("Unidades")
        cabecerasProductos.add("Descuento")
        cabecerasProductos.add("IGV")
        cabecerasProductos.add("Venta Total")
        cabecerasProductos.add("Inversión Total")
        cabecerasProductos.add("Utilidad")

        cabecerasTipoPago.add("Descripcion")
        cabecerasTipoPago.add("Monto")

        cabecerasMovimientosCaja.add("Descripcion")
        cabecerasMovimientosCaja.add("Monto")

        cabecerasDocumentoPago.add("Descripcion")
        cabecerasDocumentoPago.add("Num Doc")
        cabecerasDocumentoPago.add("Monto")

        html.AgregarTituloCentral(Constantes.Empresa.nombreTienda)
        html.AgregarDiv(titulo)
        var total=0.bg
        var totalDesc=0.bg
        var totalIgv=0.bg
        var totalCosto=0.bg
        var totalUtilidad=0.bg

        Constantes.Tiendas.tiendaList.forEach {
            r=0
            tablaDescuento.clear()
            tablaDocPago.clear()
            tablaIngreso.clear()
            tablaRetiro.clear()
            tablaProductos.clear()
            tablaTipoPago.clear()
            total=0.bg
            totalDesc=0.bg
            totalIgv=0.bg
            totalCosto=0.bg
            totalUtilidad=0.bg
            if(totales.filter {f->f.idTienda==it.idTienda }.size>0) {
                html.AgregarSubtitulo(it.nombreTienda)
            }
            totales.filter {f->f.idTienda==it.idTienda  }.forEach {

                var titulo=ArrayList<String>()
                titulo.add("Total en ventas - Total Inversión - Total IGV = Total utilidad")
                titulo.add(it.total.fortMoneda+" - " +it.totalCosto.fortMoneda+" - "+it.totalIgv.fortMoneda
                        +" = "+it.totalUtilidad.fortMoneda)

                html.AgregarDiv(titulo)
            }

            movimientosTienda.filter { f->f.idTienda==it.idTienda }.forEach {

                if(it.motivo.tipoMotivo==1){
                    var fila=ArrayList<String>()
                    fila.add(it.motivo.descripcionMotivo)
                    fila.add(it.monto.fortMoneda)
                    tablaIngreso.add(fila)
              }
                else if(it.motivo.tipoMotivo==2){
                    var fila=ArrayList<String>()
                    fila.add(it.motivo.descripcionMotivo)
                    fila.add(it.monto.fortMoneda)
                    tablaRetiro.add(fila)

                }

            }
            if(tablaRetiro.size>0 || tablaIngreso.size>0) {
                html.AgregarDiv2ColumnasTablas("Ingresos", cabecerasMovimientosCaja,
                        tablaIngreso, "Retiros", cabecerasMovimientosCaja, tablaRetiro)
            }
            pagosDocumento.filter { f->f.idTienda==it.idTienda }.forEach {

                var fila=ArrayList<String>()
                fila.add(it.cDescripcion)
                fila.add(it.numeroDocumentos.toString())
                fila.add(it.monto.fortMoneda)
                tablaDocPago.add(fila)
            }

            pagosPorTipo.filter { f->f.idTienda==it.idTienda }.forEach {

                var fila=ArrayList<String>()
                fila.add(it.tipoPago.descripcionTipoPago)
                fila.add(it.monto.fortMoneda)
                tablaTipoPago.add(fila)
            }
            if(tablaDocPago.size>0 || tablaTipoPago.size>0) {
                html.AgregarDiv2ColumnasTablas("Documentos de pago",cabecerasDocumentoPago,
                        tablaDocPago,"Tipo de pago.",cabecerasTipoPago,tablaTipoPago)

            }

            descuentosPorTienda.filter { f->f.idTienda==it.idTienda }.forEach {

                var fila=ArrayList<String>()
                fila.add(it.monto.fortMoneda)
                tablaDescuento.add(fila)
            }

            if( tablaDescuento.size>0) {
                html.SaltoPagina()
                html.AgregarSubtitulo("Descuentos")
                html.AgregarTabla(cabecerasDescuento, tablaDescuento)
                html.SaltoPagina()
            }
            productos.filter { f->f.idTienda==it.idTienda }.forEach {
                var fila=ArrayList<String>()
                fila.add(it.fecha)
                fila.add(it.codigoProducto)
                fila.add(it.descripcionCategoria+" "+it.descripcionSubCategoria+" "+it.productName
                        +" "+it.descripcionVariante+Constantes.EHTML.Left)
                fila.add(it.cantidad.fUnidades.toString())
                fila.add(it.montoDescuento.fortMoneda)
                fila.add(it.montoIgv.fortMoneda)
                fila.add(it.precioVentaFinal.fortMoneda)
                fila.add(it.costoTotal.fortMoneda)
                fila.add(it.utilidad.subtract(it.montoIgv).fortMoneda)
                total=total.add(it.precioVentaFinal)
                totalIgv=totalIgv.add(it.montoIgv)
                totalDesc=totalDesc.add(it.montoDescuento)
                totalUtilidad=totalUtilidad.add(it.utilidad)
                totalCosto=totalCosto.add(it.costoTotal)
                tablaProductos.add(fila)

                r++
            }
            if(r>0){
                var fila=ArrayList<String>()
                fila.add("Total")
                fila.add("")
                fila.add("")
                fila.add("")
                fila.add(totalDesc.fortMoneda)
                fila.add(totalIgv.fortMoneda)

                fila.add(total.fortMoneda)
                fila.add(totalCosto.fortMoneda)
                fila.add(totalUtilidad.subtract(totalIgv).fortMoneda)
                tablaProductos.add(fila)
            }
            if(tablaProductos.size>0) {
                html.AgregarSubtitulo("Productos Vendidos")
                html.AgregarTabla(cabecerasProductos, tablaProductos)
                html.SaltoPagina()
            }
        }


        return html.ObtenerHtml()
    }

        fun ObtenerReportePeriodo(idTienda:Int,fInicio:String,fFinal:String,fechaInicio:String,fechaFinal:String){
        d.IniciarDialogCarga("Generando Reporte")
         GlobalScope.launch  {

            var productos=BdConnectionSql.
                    getSinglentonInstance().ProductosVendidosPorFechaTienda(idTienda,fInicio,fFinal)
            var totalesPorTienda=BdConnectionSql.getSinglentonInstance().TotalesPorTienda(idTienda,fInicio,fFinal)
            var pagosDocumentos=BdConnectionSql.getSinglentonInstance().PagosPorDocumentoPago(idTienda,fInicio,fFinal)

            var pagosPorTipo=BdConnectionSql.getSinglentonInstance().PagosPorTiposPagoTiendas(idTienda,fInicio,fFinal)

            var descuentos=BdConnectionSql.getSinglentonInstance().DescuentosPorTienda(idTienda,fInicio,fFinal)

            var movimientos=BdConnectionSql.getSinglentonInstance().obtenerMovimientosCaja(idTienda,fInicio,fFinal)
            if(idTienda==0) {
                textoHtml = GenerarReporteTodasTiendas(ArrayList(productos), ArrayList(pagosDocumentos)
                        , ArrayList(pagosPorTipo), ArrayList(descuentos), ArrayList(movimientos), fechaInicio, fechaFinal,ArrayList(totalesPorTienda))
            }else{
                textoHtml=ReporteTiendaUnica(ArrayList(productos), ArrayList(pagosDocumentos)
                        , ArrayList(pagosPorTipo), ArrayList(descuentos), ArrayList(movimientos), fechaInicio, fechaFinal,idTienda,ArrayList(totalesPorTienda))
            }

           launch(Dispatchers.Main){
                    generarPdfGenerator.GenerarPdf(html=textoHtml,nombreDoc = "RPeriodo")
                    d.FinalizarDialogCarga()
            }
        }



    }





}

private val Int.bg: BigDecimal
    get() {
         return BigDecimal(0)
    }

val Float.fUnidades: String
    get() {
        return String.format("%.2f",this)
    }

val BigDecimal.fortMoneda: String
    get() {
        return String.format("%.2f",this)
    }
