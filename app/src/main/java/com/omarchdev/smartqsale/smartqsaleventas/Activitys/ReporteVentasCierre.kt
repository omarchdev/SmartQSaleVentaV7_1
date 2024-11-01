package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCaja
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporte
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVendedores
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedorProducto
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasVendedor
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_ventas_cierre.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


class ReporteVentasCierre : ActivityParent(), AsyncReporte.ListenerVentasPorCierre {


    internal val c = Calendar.getInstance()
    internal var dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm aa")
    var tipoReporte = 0;
    var permitir = true
    internal var fechaInicio: String=""
    internal var fechaFinal:String=""
    internal val CODE_RESULT_ID_CAJA = 1
    val asyncCaja= AsyncCaja(this@ReporteVentasCierre)

    val controladorProcesoCargar = ControladorProcesoCargar(this)
    var idVendedor = 0
    var idCierre=0
    val asyncReporte=AsyncReporte()
    val asyncVendedores = AsyncVendedores()
    val listVendedor = ArrayList<mVendedor>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_ventas_cierre)
        Listener()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

        supportActionBar?.setTitle( "Ventas por caja" )
        rgVendedor.check(R.id.rb1)
        rgTipoReporte.check(R.id.rbDetalle)

        cv_select_cierre.setOnClickListener {
            val intent = Intent(this@ReporteVentasCierre, HistorialCierresCaja::class.java)
            startActivityForResult(intent, CODE_RESULT_ID_CAJA)
        }
        try {
            var listaVendedores=ArrayList<String>()
            val arrayAdapter = ArrayAdapter<String>(this@ReporteVentasCierre, android.R.layout.simple_expandable_list_item_1, listaVendedores)
            spnVendedores.adapter = arrayAdapter
            arrayAdapter.add("Cargando vendedores")
            asyncVendedores.ObtenerVendedores();
            asyncVendedores.setListenerVendedores(object : AsyncVendedores.ListenerVendedores {
                override fun ObtenerVendedores(vendedors: MutableList<mVendedor>?) {
                    listVendedor.clear()
                    listVendedor.addAll( ArrayList(vendedors!!))
                    arrayAdapter.clear()
                    arrayAdapter.add("Seleccione un vendedor")
                    ArrayList(vendedors).forEach {
                        arrayAdapter.add(it.primerNombre + " " + it.apellidoPaterno + " " + it.apellidoMaterno)
                    }
                }
                override fun ErrorObtener() {
                    finish()
                    Toast.makeText(this@ReporteVentasCierre, "Error al descargar información.Verifique su conexión.", Toast.LENGTH_LONG).show()
                }
            })
        }catch (e:Exception){
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show()
        }
        OcultarInterfaz()
        asyncCaja.ObtenerIdCierreCaja()
        asyncCaja.setListenerAperturaCaja(object:AsyncCaja.ListenerAperturaCaja {

            override fun ConfirmacionAperturaCaja() {
                Toast.makeText(this@ReporteVentasCierre,"Error al conseguir la informacion de la caja."
                        ,Toast.LENGTH_LONG).show()
            }

            override fun AperturarCaja() {/*
                txtMensaje.visibility=View.INVISIBLE
                pb.visibility= View.GONE
                content.visibility=View.INVISIBLE*/
            }
            override fun ConfirmarCierreCaja() {
                Toast.makeText(this@ReporteVentasCierre,"Error al conseguir la informacion de la caja."
                        ,Toast.LENGTH_LONG).show()

            }
            override fun ExisteCajaAbiertaDisponible() {
                Toast.makeText(this@ReporteVentasCierre,"Error al conseguir la informacion de la caja."
                        ,Toast.LENGTH_LONG).show()

            }
            override fun CajaCerrada() {
                Toast.makeText(this@ReporteVentasCierre,"Error al conseguir la informacion de la caja."
                        ,Toast.LENGTH_LONG).show()

            }
            override fun ExisteCierre(Cierre: mCierre?) {
                OrdenarVentasPorCierre(Cierre!!.idCierre)
                idCierre=Cierre!!.idCierre
            }
            override fun ErrorEncontrarCaja() {
                Toast.makeText(this@ReporteVentasCierre,"Error al conseguir la informacion de la caja."
                        ,Toast.LENGTH_LONG).show()

            }
        })
    }
    override fun ErrorConsulta() {
        Toast.makeText(this@ReporteVentasCierre,"Error al consultar los datos",Toast.LENGTH_LONG).show()
        finish()
    }

    override fun ResultadoVentasPorCierre(listaResultado: MutableList<mVentasVendedor>?, cierre: mCierre?) {
        ModificarDatosCabecera(cierre!!)
        HabilitarInterfaz()
    }

    fun OrdenarVentasPorCierre(cierre:Int){
        asyncReporte.ObtenerCabeceraCierre(cierre!!)
        asyncReporte.setListenerVentasPorCierre(this)
    }

    fun ModificarDatosCabecera(cierre: mCierre) {

        fechaInicio = dateFormat.format(cierre.fechaApertura)
        if (cierre.estadoCierre == "A") {
            txtEstadoCierre.setText("Caja abierta:${cierre.idCierre}")
            fechaFinal = " hasta ahora"

        } else if (cierre.estadoCierre == "C") {
            txtEstadoCierre.setText("Caja cerrada:${cierre.idCierre}")
            if (cierre.fechaCierre != null) {
                fechaFinal = " a " + dateFormat.format(cierre.fechaCierre)
            } else {
                fechaFinal = "-"
            }
        }
        txtPeriodoCaja.setText(fechaInicio + fechaFinal)
    }

    fun Listener(){
        btnGenerarReporte.setOnClickListener{
            when(tipoReporte){
                Constantes.ReporteVendedores.reportePorVendedor->{
                    permitir=true

                    if(spnVendedores.selectedItemPosition==0 ){
                        permitir=false
                        AlertDialog.Builder(this).setTitle("Advertencia")
                                .setMessage("Debe elegir un vendedor para generar un reporte")
                                .setPositiveButton("Salir",null).create().show()
                    }else{
                        permitir=true
                    }
                    if(permitir) {
                        idVendedor = listVendedor.get(spnVendedores.selectedItemPosition - 1).idVendedor
                        if(idVendedor!=0) {
                            controladorProcesoCargar.IniciarDialogCarga("Cargando datos")
                            if(rbDetalle.isChecked) {

                                asyncReporte.ObtenerDetalleVentasCierre(idCierre,idVendedor)
                                asyncReporte.setListenerReporteDetalleVentaCierre(object:AsyncReporte.ListenerReporteDetalleVentaCierre{
                                    override fun ResultadoReporteCierre(listaReporte: MutableList<mVendedorProducto>?) {
                                        controladorProcesoCargar.FinalizarDialogCarga()
                                        try {
                                            if(listaReporte!!.size>0) {
                                                ArmarReporteDetalle(listaReporte)
                                            }
                                            else{
                                                AlertDialog.Builder(this@ReporteVentasCierre).setTitle("Advertencia").
                                                        setMessage("No existen ventas para el periodo seleccionado")
                                                        .setPositiveButton("Salir",null).create().show()
                                            }
                                        }catch (e:Exception){
                                            Toast.makeText(this@ReporteVentasCierre, e.toString(), Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun ErrorConsultaReporte() {
                                        Toast.makeText(this@ReporteVentasCierre, "Error al descargar información.Verifique su conexión.", Toast.LENGTH_LONG).show()
                                        controladorProcesoCargar.FinalizarDialogCarga()
                                    }

                                })

                            }
                            else{
                                asyncReporte.ObtenerAcumuladoVentasCierre(idCierre,idVendedor)
                                asyncReporte.setListenerReporteDetalleVentaCierre(object:AsyncReporte.ListenerReporteDetalleVentaCierre{
                                    override fun ResultadoReporteCierre(listaReporte: MutableList<mVendedorProducto>?) {
                                        controladorProcesoCargar.FinalizarDialogCarga()
                                        try {
                                            if(listaReporte!!.size>0) {
                                                ArmarReporteDetalle(listaReporte)
                                            }
                                            else{
                                                AlertDialog.Builder(this@ReporteVentasCierre).setTitle("Advertencia").
                                                        setMessage("No existen ventas para el periodo seleccionado")
                                                        .setPositiveButton("Salir",null).create().show()
                                            }
                                        }catch (e:Exception){
                                            Toast.makeText(this@ReporteVentasCierre, e.toString(), Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun ErrorConsultaReporte() {
                                        Toast.makeText(this@ReporteVentasCierre, "Error al descargar información.Verifique su conexión.", Toast.LENGTH_LONG).show()
                                        controladorProcesoCargar.FinalizarDialogCarga()
                                    }

                                })

                            }
                        }
                    }
                }
                Constantes.ReporteVendedores.reporteTodosVendedores->{
                    idVendedor=0
                    controladorProcesoCargar.IniciarDialogCarga("Cargando datos")
                    if(rbDetalle.isChecked) {
                        idVendedor=0
                        asyncReporte.ObtenerDetalleVentasCierre(idCierre,idVendedor)
                        asyncReporte.setListenerReporteDetalleVentaCierre(object:AsyncReporte.ListenerReporteDetalleVentaCierre{
                            override fun ResultadoReporteCierre(listaReporte: MutableList<mVendedorProducto>?) {
                                controladorProcesoCargar.FinalizarDialogCarga()
                                try {
                                    if(listaReporte!!.size>0) {
                                        ArmarReporteDetalle(listaReporte)
                                    }
                                    else{
                                        AlertDialog.Builder(this@ReporteVentasCierre).setTitle("Advertencia").
                                                setMessage("No existen ventas para el periodo seleccionado")
                                                .setPositiveButton("Salir",null).create().show()
                                    }
                                }catch (e:Exception){
                                    Toast.makeText(this@ReporteVentasCierre, e.toString(), Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun ErrorConsultaReporte() {
                                Toast.makeText(this@ReporteVentasCierre, "Error al descargar información.Verifique su conexión.", Toast.LENGTH_LONG).show()
                                controladorProcesoCargar.FinalizarDialogCarga()
                            }

                        })

                    }
                    else{
                        idVendedor=0
                        asyncReporte.ObtenerAcumuladoVentasCierre(idCierre,idVendedor)
                        asyncReporte.setListenerReporteDetalleVentaCierre(object:AsyncReporte.ListenerReporteDetalleVentaCierre{
                            override fun ResultadoReporteCierre(listaReporte: MutableList<mVendedorProducto>?) {
                                controladorProcesoCargar.FinalizarDialogCarga()
                                try {
                                    if(listaReporte!!.size>0) {
                                        ArmarReporteDetalle(listaReporte)
                                    }
                                    else{
                                        AlertDialog.Builder(this@ReporteVentasCierre).setTitle("Advertencia").
                                                setMessage("No existen ventas para el periodo seleccionado")
                                                .setPositiveButton("Salir",null).create().show()
                                    }
                                }catch (e:Exception){
                                    Toast.makeText(this@ReporteVentasCierre, e.toString(), Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun ErrorConsultaReporte() {
                                Toast.makeText(this@ReporteVentasCierre, "Error al descargar información.Verifique su conexión.", Toast.LENGTH_LONG).show()
                                controladorProcesoCargar.FinalizarDialogCarga()
                            }

                        })

                    }
               }
            }
        }
        rgVendedor.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb1->{
                    spnVendedores.visibility= View.GONE
                    spnVendedores.setSelection(0)
                    idVendedor=0

                    tipoReporte=Constantes.ReporteVendedores.reporteTodosVendedores
                }
                R.id.rb2->{
                    idVendedor=0
                    spnVendedores.visibility=View.VISIBLE
                    tipoReporte=Constantes.ReporteVendedores.reportePorVendedor

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_RESULT_ID_CAJA) {
            if (resultCode == RESULT_CANCELED) {
            } else {
                idCierre=data!!.getIntExtra("idCierre", 0)
                OrdenarVentasPorCierre(idCierre)
                OcultarInterfaz()
            }
        }
    }
    fun OcultarInterfaz(){
        rgVendedor.visibility=View.INVISIBLE
        rgTipoReporte.visibility=View.INVISIBLE
        spnVendedores.visibility=View.INVISIBLE
        txtTitulo.visibility=View.INVISIBLE
        btnGenerarReporte.visibility=View.INVISIBLE
        pb.visibility=View.VISIBLE
        cv_select_cierre.visibility=View.INVISIBLE
    }
    fun HabilitarInterfaz(){
        rgVendedor.visibility=View.VISIBLE
        rgTipoReporte.visibility=View.VISIBLE
        txtTitulo.visibility=View.VISIBLE
        btnGenerarReporte.visibility=View.VISIBLE
        pb.visibility=View.INVISIBLE
        cv_select_cierre.visibility=View.VISIBLE


        spnVendedores.visibility=View.GONE

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
    fun ArmarReporteDetalle(mVendedorProductoList: MutableList<mVendedorProducto>?){
        val pdfGenerator=PdfGenerator(this)

        var tabla:ArrayList<ArrayList<String>>
        tabla= ArrayList()
        var html= Html()
        var cabeceras=ArrayList<String>()
        var columnas:ArrayList<String>
        columnas= ArrayList()

        var tempFecha:String?=""
        var tempVendedor:Int?=0
        var contador=0
        var total= BigDecimal(0)
        var totalfecha= BigDecimal(0)
        if(rbDetalle.isChecked) {
            cabeceras.add("Num.Venta")
        }
        cabeceras.add("Cod.Producto")
        cabeceras.add("Nombre del producto")
        cabeceras.add("Unidades")
        cabeceras.add("Valor Subtotal")
        html.AgregarTituloCentral(Constantes.Empresa.nombreTienda)
        html.AgregarSubtitulo("Reporte de ventas por cierre de caja")
        tempFecha=mVendedorProductoList!!.get(0).fechaProceso
        tempVendedor=mVendedorProductoList!!.get(0).vendedor.idVendedor
        html.AgregarSubtitulo(mVendedorProductoList!!.get(0).fechaProceso)
        html.AgregarTituloIntermedio("${mVendedorProductoList.get(contador).vendedor.primerNombre}" +
                " ${mVendedorProductoList.get(contador).vendedor.apellidoPaterno} ${mVendedorProductoList.get(contador).vendedor.apellidoMaterno}")

        while(mVendedorProductoList!!.size>contador){
            if(tempFecha==mVendedorProductoList.get(contador).fechaProceso){
                if(tempVendedor==mVendedorProductoList.get(contador).vendedor.idVendedor){
                    columnas= ArrayList()
                    if(rbDetalle.isChecked) {
                        columnas.add("${mVendedorProductoList.get(contador).idCabeceraVenta}")
                    }

                   columnas.add("${mVendedorProductoList.get(contador).product.idProduct}")
                    columnas.add("${mVendedorProductoList.get(contador).product.getcProductName()} " +
                            "${mVendedorProductoList.get(contador).product.descripcionVariante} ${mVendedorProductoList.get(contador).product.modificadores}")
                    columnas.add("${String.format("%.2f",mVendedorProductoList.get(contador).product.getdQuantity())}")
                    columnas.add("${Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",mVendedorProductoList.get(contador).product.precioVenta)}")
                    tabla.add(columnas)
                    total=total.add(mVendedorProductoList.get(contador).product.precioVenta)
                    totalfecha=totalfecha.add(mVendedorProductoList.get(contador).product.precioVenta)
                    contador+=1
                }else{
                    columnas=ArrayList()
                    columnas.add("Total de vendedor")
                    if(rbDetalle.isChecked) {
                        columnas.add("")
                    }
                    columnas.add("")
                    columnas.add("")
                    columnas.add(Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total))
                    tabla.add(columnas)
                    html.AgregarTabla(cabeceras,tabla)
                    tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor
                    total= BigDecimal(0)
                     html.AgregarTituloIntermedio("${mVendedorProductoList.get(contador).vendedor.primerNombre}" +
                             " ${mVendedorProductoList.get(contador).vendedor.apellidoPaterno} ${mVendedorProductoList.get(contador).vendedor.apellidoMaterno}")
                    tabla=ArrayList()
                }
            }else{

                columnas=ArrayList()
                columnas.add("Total de vendedor")
                if(rbDetalle.isChecked) {
                    columnas.add("")
                }
                columnas.add("")
                columnas.add("")
                columnas.add(Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total))
                tabla.add(columnas)
                html.AgregarTabla(cabeceras,tabla)
                html.AgregarSubtitulo("Total por fecha ${String.format("%.2f",totalfecha)}")
                tabla=ArrayList()
                total= BigDecimal(0)
                totalfecha= BigDecimal(0)

                html.SaltoPagina()
                tempFecha=mVendedorProductoList!!.get(contador).fechaProceso
                tempVendedor=mVendedorProductoList!!.get(contador).vendedor.idVendedor
                html.AgregarSubtitulo(mVendedorProductoList.get(contador).fechaProceso)
                html.AgregarTituloIntermedio("${mVendedorProductoList.get(contador).vendedor.primerNombre}" +
                        " ${mVendedorProductoList.get(contador).vendedor.apellidoPaterno} ${mVendedorProductoList.get(contador).vendedor.apellidoMaterno}")

            }

        }

        columnas=ArrayList()
        columnas.add("Total de vendedor")
        if(rbDetalle.isChecked) {
            columnas.add("")
        }
        columnas.add("")
        columnas.add("")
        columnas.add(Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total))
        tabla.add(columnas)
        html.AgregarTabla(cabeceras,tabla)
        html.AgregarSubtitulo("Total por fecha ${String.format("%.2f",totalfecha)}")

        pdfGenerator.GenerarPdf(html.ObtenerHtml(),"Reporte_Ventas_Cierre")

    }

}
