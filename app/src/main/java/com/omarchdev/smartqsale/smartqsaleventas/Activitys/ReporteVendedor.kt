package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporte
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporteVendedor
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVendedor
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVendedores
import com.omarchdev.smartqsale.smartqsaleventas.Bluetooth.BluetoothConnection
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.SelectTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedorProducto
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrintOptions
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_vendedor.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class ReporteVendedor : ActivityParent(), AsyncReporte.ListenerReportePVendedor, SelectTienda.TiendaInterface {


    lateinit var printOptions:PrintOptions
        var deviceAddressBt = ""
        val btConnection = BluetoothConnection.getSinglentonInstance(this@ReporteVendedor)
        var dbHelper = DbHelper(this@ReporteVendedor)
        val listadoResultado = ArrayList<mVendedorProducto>()
        internal val c = Calendar.getInstance()
        val selectFecha = DialogDatePickerSelect()
        val controladorProcesoCargar = ControladorProcesoCargar(this)
        var tipoReporte = 0;
        var dia: Int = 0
        var mes: Int = 0
        var anio: Int = 0
        var origen: Byte = 0
        val calendar: Calendar = Calendar.getInstance()
        var fInicioText = ""
        var fFinalText = ""
        var permitir = true
        val fm = this@ReporteVendedor.fragmentManager
        private fun TextoFecha(titulo: String, dia: Int, mes: Int, anio: Int): String = "$titulo \n $dia/$mes/$anio"
        var idVendedor = 0
        val listVendedor = ArrayList<mVendedor>()
        val asyncReporte = AsyncReporte();
        val asyncVendedores = AsyncVendedores()
        var idTienda=0
        var permitirGenerar=false
        val asyncVendedor=AsyncVendedor()
        val asyncReporteVendedor=AsyncReporteVendedor(this@ReporteVendedor)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_vendedor)
        Listener()
        rgVendedor.check(R.id.rb1)
        rgTipoReporte.check(R.id.rbDetalle)
        GenerarFechaOrigen()
        listenerButtonFiltro()
        GetFecha()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.elevation  = 4f
        supportActionBar?.setTitle("Reporte de Vendedores" )
        try {
            var listaVendedores=ArrayList<String>()
            val arrayAdapter = ArrayAdapter<String>(this@ReporteVendedor, android.R.layout.simple_expandable_list_item_1, listaVendedores)
            spnVendedores.adapter = arrayAdapter
            arrayAdapter.add("Cargando vendedores")
            asyncVendedor.listenerVendedores=object : AsyncVendedores.ListenerVendedores {
                override fun ObtenerVendedores(vendedors: MutableList<mVendedor>?) {
                    spnVendedores.isEnabled=true
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
                    Toast.makeText(this@ReporteVendedor, "Error al descargar información.Verifique su conexión.", Toast.LENGTH_LONG).show()
                }
            }
        }catch (e:Exception) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }

        val selectTienda=SelectTienda().newInstance(this,"Todas las tiendas ")
        val fml = supportFragmentManager
        val ftl = fml.beginTransaction()
        ftl.replace(R.id.content_SpnTiendas,selectTienda)
        ftl.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"


    private fun GenerarReporte(){

        if(idTienda==0){
            if(rbDetalle.isChecked) {
                asyncReporteVendedor.ReporteTodasTiendas(fInicioText, fFinalText, 100)
            }else if(rbAcumulado.isChecked){
                asyncReporteVendedor.ReporteTodasTiendas(fInicioText, fFinalText, 200)

            }

        }else{

            try{
              if(rb1.isChecked){

                idVendedor=0
                spnVendedores.setSelection(0)

                spnVendedores.setSelection(0)

                spnVendedores.setSelection(0)

                }else if(rb2.isChecked){
                  if(spnVendedores.selectedItemPosition==0){
                      idVendedor=0
                  }
                  else{
                      idVendedor = listVendedor.get(spnVendedores.selectedItemPosition-1).idVendedor
                  }
               }


            if(rbDetalle.isChecked){
                asyncReporteVendedor.ReporteVendedorPorTienda(fInicioText,fFinalText,idTienda,idVendedor,100)

            }else if(rbAcumulado.isChecked){

                asyncReporteVendedor.ReporteVendedorPorTienda(fInicioText,fFinalText,idTienda,idVendedor,200)


            }
            }catch (e:Exception){
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun Listener(){
        btnGenerarReporte.setOnClickListener{
            GenerarReporte()
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
    override fun ErrorResultado() {
        MensajeAlerta(titulo="Advertencia",mensaje="Error al consultar la informacion.Verifique su conexión.")
        controladorProcesoCargar.FinalizarDialogCarga()
    }

    override fun ResultadoReporte(mVendedorProductoList: MutableList<mVendedorProducto>?) {
        controladorProcesoCargar.FinalizarDialogCarga()
        listadoResultado.clear()
       if(mVendedorProductoList!!.size>0) {
            listadoResultado.addAll(ArrayList(mVendedorProductoList))
           try {
               if (rbDetalle.isChecked) {

                   ArmarReporteDetalle(ArrayList(mVendedorProductoList))
               } else {
                   ArmarReporteAcumulado(ArrayList(mVendedorProductoList))
               }// SeleccionarMetodo()
           }catch (e:Exception){
               Toast.makeText(this@ReporteVendedor,e.toString(),Toast.LENGTH_LONG).show()
           }
       }else{
            when(tipoReporte) {
                Constantes.ReporteVendedores.reportePorVendedor->
                    MensajeAlerta(titulo = "Advertencia", mensaje = "El vendedor no posee ventas en el periodo seleccionado")
               Constantes.ReporteVendedores.reporteTodosVendedores->
                   MensajeAlerta(titulo = "Advertencia", mensaje = "Los vendedores no poseen ventas en el periodo seleccionado")

           }
        }
    }


    private fun MensajeAlerta(mensaje:String="",titulo:String=""){
       AlertDialog.Builder(this).setMessage(mensaje).setTitle(titulo).setPositiveButton("Aceptar",null).create().show()
    }
    private fun MostrarSeleccionarFecha(origen:Byte){
        selectFecha.setOrigen(origen,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
        selectFecha.show(fm,"Fecha")
    }
    private fun listenerButtonFiltro() {

        btnFechaInicio.setOnClickListener{
            MostrarSeleccionarFecha(1)
        }
        btnFechaFinal.setOnClickListener{
            MostrarSeleccionarFecha(2)
        }
    }

    fun ArmarTextoImpresion(mVendedorProductoList: MutableList<mVendedorProducto>?){
        var texto:String
        var tempFecha:String?
        var tempVendedor:Int?
        var total=BigDecimal(0)
        var totalfecha=BigDecimal(0)
        tempFecha=mVendedorProductoList!!.get(0).fechaProceso
        tempVendedor=mVendedorProductoList!!.get(0).vendedor.idVendedor
        var contador=0
        texto=""
        texto=texto+"\n     $tempFecha     "
        texto=texto+"\n $tempVendedor   "
        texto=texto+"\n "
        while(mVendedorProductoList!!.size>contador){
            if(tempFecha==mVendedorProductoList.get(contador).fechaProceso){
                if(tempVendedor==mVendedorProductoList.get(contador).vendedor.idVendedor){
                    total=total.add(mVendedorProductoList.get(contador).product.precioVenta)
                    totalfecha=totalfecha.add(mVendedorProductoList.get(contador).product.precioVenta)
                    texto=texto+"\n"
                    contador+=1

                }else{
                    tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor
                    total=BigDecimal(0)
                }
            }
            else{
                total=BigDecimal(0)
                totalfecha= BigDecimal(0)
                tempFecha=mVendedorProductoList.get(contador).fechaProceso
                tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor


            }
        }
    }

    fun SeleccionarMetodo() {
        var option: String
        val c = dbHelper.SelectOptionPrint()
        if (c.count > 0) {
            while (c.moveToNext()) {
                option = c.getString(0)
                when (option) {
                    "PDF o impresora en red" -> ArmarReporteDetalle(listadoResultado)
                    "Bluetooth" -> if (btConnection.VerifiedBt1()) {
                        val cc = dbHelper.SelectDevice()
                        if (cc.count > 0) {
                            while (cc.moveToNext()) {
                                deviceAddressBt = cc.getString(0)
                            }
                            btConnection.selectDevice(deviceAddressBt)
                            btConnection.openBT()

                            printOptions = PrintOptions(this@ReporteVendedor, btConnection.outputStream, btConnection.mmInputStream)

                            listadoResultado.forEach {
                                printOptions.PrintReportVendedor(it.product.getcProductName(),String.format("%.2f",it.product.getdQuantity()),String.format("%.2f",it.product.precioVenta))
                            }
                            //printOptions.imprimirTicketPago(list);
                          //  printOptions.ImpresionTicketNotaVenta(list)
                        } else if (cc.count == 0) {
                            Toast.makeText(this@ReporteVendedor, "No selecciono una impresora", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@ReporteVendedor, "El bluetooth no está conectado", Toast.LENGTH_LONG).show()
                    }
                    "Ninguno" -> Toast.makeText(this@ReporteVendedor, "No selecciono una impresora.Elija una en la configuración de Impresora", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(this@ReporteVendedor, "No selecciono una impresora", Toast.LENGTH_LONG).show()
                }//printOptions=new PrintOptions(getActivity());
                //printOptions.PrintPdf();
            }

            } else {
            Toast.makeText(this@ReporteVendedor, "No se selecciono una impresora", Toast.LENGTH_LONG).show()

        }
    }


    fun ImprimirReporte(mVendedorProductoList: MutableList<mVendedorProducto>?){

    }

    override fun TiendaSeleccionada(id:Int)  {


        idTienda=id


        if(id==0){

            EstadoVendedores(false)

        }else{
            asyncVendedor.ObtenerVendedoresTienda(id)
            EstadoVendedores(true)
            spnVendedores.isEnabled=false

        }
    }
    private fun EstadoVendedores(b:Boolean){
        rgVendedor.isEnabled=b
        rb1.isEnabled=b
        rb2.isEnabled=b
        spnVendedores.isEnabled=b
    }
    private fun GenerarFechaOrigen(){
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaFinal.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))
        fFinalText=GenerarFechaTexto(dia,mes,anio)
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
        fInicioText=GenerarFechaTexto(dia,mes,anio)
    }

    @Deprecated("Se reemplazo por mentodos en clase AsyncReporteVendedor")
    fun ArmarReporteAcumulado(mVendedorProductoList: MutableList<mVendedorProducto>?){
        var tempFecha:String?
        var tempVendedor:Int?
        var contador=0
        var mWebView: WebView? = null

        tempFecha=mVendedorProductoList!!.get(0).fechaProceso
        tempVendedor=mVendedorProductoList!!.get(0).vendedor.idVendedor
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                mWebView = null
            }
        }

        val inicioHtml="<html><body>"
        val titulo="<h1  ALIGN=center>${Constantes.Empresa.nombreTienda}</h1><h1  ALIGN=center>${this.resources.getString(R.string.app_name)}</h1>"
        val finalHtml="</body></html>"
        val saltoLinea="<br/>"
        val finalTabla = "</table>"
        var htmlTituloFecha = "<h2  ALIGN=left>${mVendedorProductoList.get(0).fechaProceso}</h2>"
        var htmlTituloVendedor="<h3 Align=left >  ${mVendedorProductoList.get(0).vendedor.primerNombre}</h3>"
        var InicioTabla = "<table style='width:100%'  ALIGN=center>"
        var CabeceraTabla = "<tr style='background-color:#ef5350;'>" +
                "<th align=left style='color:#ffffff;'>Num. Producto</th><th align=left style='color:#ffffff;' >Nombre del producto</th><th align=center style='color:#ffffff;'>Unidades</th><th align=right style='color:#ffffff;'>Valor Subtotal</th></tr>"
        var cuerpoTabla=""
        var total=BigDecimal(0)
        var totalfecha=BigDecimal(0)
        var html=inicioHtml+titulo+saltoLinea+htmlTituloFecha+htmlTituloVendedor+InicioTabla+CabeceraTabla
        while(mVendedorProductoList.size>contador){
            if(tempFecha==mVendedorProductoList.get(contador).fechaProceso){
                if(tempVendedor==mVendedorProductoList.get(contador).vendedor.idVendedor){
                    total=total.add(mVendedorProductoList.get(contador).product.precioVenta)
                    totalfecha=totalfecha.add(mVendedorProductoList.get(contador).product.precioVenta)
                    cuerpoTabla="<tr><td>${mVendedorProductoList.get(contador).product.idProduct}</td><td>${mVendedorProductoList.get(contador).product.getcProductName()}" +
                            " ${mVendedorProductoList.get(contador).product.descripcionVariante} ${mVendedorProductoList.get(contador).product.modificadores} </td><td align=center>" +
                            String.format("%.2f",mVendedorProductoList.get(contador).product.getdQuantity())+
                            "</td><td align=right>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",mVendedorProductoList.get(contador).product.precioVenta)+"</td></tr>"
                    contador+=1
                    html=html+cuerpoTabla
                }
                else{
                    html= html+"<tr style='background-color:#ef5350;'><td style='color:#ffffff;font-family:verdana;'>" +
                            "Total de vendedor</td><td></td><td></td><td align=right style='color:#ffffff;font-family:verdana;'>"+
                            Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total)+"</td></tr>"
                    html=html+saltoLinea
                    html=html+finalTabla
                    tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor
                    total=BigDecimal(0)

                    html=html+saltoLinea
                    html=html+"<h3 ALIGN=left> ${mVendedorProductoList.get(contador).vendedor.primerNombre} </h3>"
                    html=html+InicioTabla+CabeceraTabla
                    cuerpoTabla=""
                }
            }else{

                //html=html+cuerpoTabla
                html= html+"<tr style='background-color:#ef5350;'><td style='color:#ffffff;font-family:verdana;'>Total de vendedor</td><td></td><td></td><td align=right style='color:#ffffff;font-family:verdana;'>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total)+"</td></tr>"
                html=html+finalTabla
                html=html+"<h3 align=center ><a align=left>Total </a><a align=rigth>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",totalfecha)+" </a></h3>"

                total=BigDecimal(0)
                totalfecha= BigDecimal(0)
                tempFecha=mVendedorProductoList.get(contador).fechaProceso
                tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor
                html=html+"<div style='page-break-after:always'></div>"
                html=html+saltoLinea
                html=html+"<h2  ALIGN=left>${mVendedorProductoList.get(contador).fechaProceso}</h2>"
                html=html+"<h3 ALIGN=left> ${mVendedorProductoList.get(contador).vendedor.primerNombre} </h3>"
                html=html+InicioTabla+CabeceraTabla
                cuerpoTabla=""
            }
        }
        html= html+"<tr style='background-color:#ef5350;'><td style='color:#ffffff;font-family:verdana;'>Total de vendedor</td><td></td><td></td><td align=right style='color:#ffffff;font-family:verdana;'>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total)+"</td></tr>"
        html=html+finalTabla+"<h3 align=center style='width:100%'><a align=left>Total </a>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",totalfecha)+"</h3>"+finalHtml
        webView.loadDataWithBaseURL("Texto", html, "text/HTML", "UTF-8", null)
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createWebPrintJob(webView)
        }
    }

    @Deprecated("Se reemplazo por mentodos en clase AsyncReporteVendedor")
    fun ArmarReporteDetalle(mVendedorProductoList: MutableList<mVendedorProducto>?){
        var tempFecha:String?
        var tempVendedor:Int?
        var contador=0
        var mWebView: WebView? = null
        tempFecha=mVendedorProductoList!!.get(0).fechaProceso
        tempVendedor=mVendedorProductoList!!.get(0).vendedor.idVendedor
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
            override fun onPageFinished(view: WebView, url: String) {
               mWebView = null
            }
        }
        val inicioHtml="<html><body>"
        val titulo="<h1  ALIGN=center>${Constantes.Empresa.nombreTienda}</h1><h1  ALIGN=center>${this.resources.getString(R.string.app_name)}</h1>"
        val finalHtml="</body></html>"
        val saltoLinea="<br/>"
        val finalTabla = "</table>"
        var htmlTituloFecha = "<h2  ALIGN=left>${mVendedorProductoList.get(0).fechaProceso}</h2>"
        var htmlTituloVendedor="<h3 Align=left >  ${mVendedorProductoList.get(0).vendedor.primerNombre}</h3>"
        var InicioTabla = "<table style='width:100%'  ALIGN=center>"
        var CabeceraTabla = "<tr style='background-color:#ef5350;'>" +
                "<th align=left style='color:#ffffff;'>Num.Venta</th><th align=left style='color:#ffffff;' >" +
                "Nombre del producto</th><th align=center style='color:#ffffff;'>Unidades</th>" +
                "<th align=right style='color:#ffffff;'>Valor Subtotal</th></tr>"
        var cuerpoTabla=""
        var total=BigDecimal(0)
        var totalfecha=BigDecimal(0)
        var html=inicioHtml+titulo+saltoLinea+htmlTituloFecha+htmlTituloVendedor+InicioTabla+CabeceraTabla
        while(mVendedorProductoList.size>contador){
           if(tempFecha==mVendedorProductoList.get(contador).fechaProceso){
               if(tempVendedor==mVendedorProductoList.get(contador).vendedor.idVendedor){
                   total=total.add(mVendedorProductoList.get(contador).product.precioVenta)
                   totalfecha=totalfecha.add(mVendedorProductoList.get(contador).product.precioVenta)
                   cuerpoTabla="<tr><td>${mVendedorProductoList.get(contador).idCabeceraVenta}</td>" +
                           "<td>${mVendedorProductoList.get(contador).product.getcProductName()}" +
                        " ${mVendedorProductoList.get(contador).product.descripcionVariante}" +
                           " ${mVendedorProductoList.get(contador).product.modificadores} </td><td align=center>" +
                        String.format("%.2f",mVendedorProductoList.get(contador).product.getdQuantity())+
                        "</td><td align=right>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",mVendedorProductoList.get(contador).product.precioVenta)+"</td></tr>"
                   contador+=1
                   html=html+cuerpoTabla
               }
               else{
                    html= html+"<tr style='background-color:#ef5350;'><td style='color:#ffffff;font-family:verdana;'>" +
                           "Total de vendedor</td><td></td><td></td><td align=right style='color:#ffffff;font-family:verdana;'>"+
                           Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total)+"</td></tr>"
                   html=html+saltoLinea
                   html=html+finalTabla
                   tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor
                   total=BigDecimal(0)

                   html=html+saltoLinea
                   html=html+"<h3 ALIGN=left> ${mVendedorProductoList.get(contador).vendedor.primerNombre} </h3>"
                   html=html+InicioTabla+CabeceraTabla
                   cuerpoTabla=""
               }
           }else{

               //html=html+cuerpoTabla
               html= html+"<tr style='background-color:#ef5350;'><td style='color:#ffffff;font-family:verdana;'>Total de vendedor</td><td></td><td></td><td align=right style='color:#ffffff;font-family:verdana;'>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total)+"</td></tr>"
               html=html+finalTabla
               html=html+"<h3 align=center ><a align=left>Total </a><a align=rigth>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",totalfecha)+" </a></h3>"

               total=BigDecimal(0)
               totalfecha= BigDecimal(0)
               tempFecha=mVendedorProductoList.get(contador).fechaProceso
               tempVendedor=mVendedorProductoList.get(contador).vendedor.idVendedor
               html=html+"<div style='page-break-after:always'></div>"
               html=html+saltoLinea
               html=html+"<h2  ALIGN=left>${mVendedorProductoList.get(contador).fechaProceso}</h2>"
               html=html+"<h3 ALIGN=left> ${mVendedorProductoList.get(contador).vendedor.primerNombre} </h3>"
               html=html+InicioTabla+CabeceraTabla
               cuerpoTabla=""

           }
        }
           html= html+"<tr style='background-color:#ef5350;'><td style='color:#ffffff;font-family:verdana;'>Total de vendedor</td><td></td><td></td><td align=right style='color:#ffffff;font-family:verdana;'>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f", total)+"</td></tr>"


        html=html+finalTabla+"<h3 align=center style='width:100%'><a align=left>Total </a>"+Constantes.DivisaPorDefecto.SimboloDivisa+String.format("%.2f",totalfecha)+"</h3>"+finalHtml
        webView.loadDataWithBaseURL("Texto", html, "text/HTML", "UTF-8", null)
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createWebPrintJob(webView)
        }
    }
    private fun GetFecha(){
        selectFecha.setFechaListener {
            day: Int, month: Int, year: Int, origen: Byte ->
            dia=day
            mes=month
            anio=year
            this.origen=origen
            when(origen){
                2.toByte()->{
                    fFinalText=GenerarFechaTexto(dia,mes,anio)
                    btnFechaFinal.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))}
                1.toByte()->{
                    fInicioText=GenerarFechaTexto(dia,mes,anio)
                    btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))}
            }
          //  asyncMovAlmacen.ObtenerMovimientosAlmacen(fInicioText,fFinalText)
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun createWebPrintJob(webView: WebView) {

        // Get a PrintManager instance
        val printManager = this
                .getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAttributes=PrintAttributes.Builder()
        printAttributes.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
        var printAdapter: PrintDocumentAdapter? = null
        // Get a print adapter instance

        printAdapter = webView.createPrintDocumentAdapter(this.getString(R.string.app_name) + " " + c.get(Calendar.YEAR).toString() + "_" + (c.get(Calendar.MONTH) + 1).toString()
                + "_" + c.get(Calendar.DAY_OF_MONTH).toString()
        )

        // Create a print job with name and adapter instance
        val jobName = this.getString(R.string.app_name) + " Document"
        val printJob = printManager.print(jobName, printAdapter!!,
                printAttributes.build())

        // Save the job object for later status checking

    }
    override fun SeCargoTiendas() {
        permitirGenerar=true
    }

    override fun TiendaPorDefecto() {
        EstadoVendedores(false)

    }


}
