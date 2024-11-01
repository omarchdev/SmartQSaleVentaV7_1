package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporte
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacenProducto
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_almacen.*
import java.math.BigDecimal

class ReporteAlmacen : ActivityParent(), AsyncReporte.ReporteAlmacen {



   val asyncAlmacen=AsyncAlmacenes()
    val listaAlmacen=ArrayList<mAlmacen>()
    val asyncReporteAlmacen=AsyncReporte()
    val controladorProcesoCargar=ControladorProcesoCargar(this@ReporteAlmacen)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_almacen)
        try {
            var lista=ArrayList<String>()
            val adapterSpinner=ArrayAdapter<String>(this@ReporteAlmacen,android.R.layout.simple_expandable_list_item_1,lista)
            spnAlmacenes.adapter = adapterSpinner
            rgAlmacen.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                   R.id.rbTodos->{
                       rspn.visibility= View.GONE
                    }
                    R.id.rbUno->{
                        rspn.visibility= View.VISIBLE
                    }
                }
            }
            rgAlmacen.check(R.id.rbTodos)

            try{
                adapterSpinner.add("Obteniendo almacenes...")
            }catch (e:Exception){
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
            }
            asyncAlmacen.ObtenerAlmacenes()
            asyncAlmacen.setListenerAlmacenes(object : AsyncAlmacenes.ListenerAlmacenes {
                override fun ObtenerBusquedaAlmacenes(almacenList: MutableList<mAlmacen>?) {
                    listaAlmacen.clear()
                    listaAlmacen.addAll(ArrayList(almacenList!!))
                    adapterSpinner.clear()
                    if (listaAlmacen.size == 0) {
                        adapterSpinner.add("No tiene un almacén")
                    }else{
                        adapterSpinner.add("Seleccione un almacén")
                        listaAlmacen.forEach {
                            adapterSpinner.add(it.descripcionAlmacen)
                        }
                    }

                }

                override fun ErrorConsulta() {
                    finish()
                    Toast.makeText(this@ReporteAlmacen, "Error al conseguir los almacénes." +
                            "Verifique su conexión a internet", Toast.LENGTH_LONG).show()
                }

            })
        }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
        btnGReporte.setOnClickListener {
            if(rbUno.isChecked){
                if(listaAlmacen.size==0){
                    AlertDialog.Builder(this).setTitle("Advertencia")
                            .setMessage("No tiene almacenes registrado para generar un reporte").
                                    setPositiveButton("Salir",null).create().show()
                }else{
                    ObtenerReporteAlmacenEspecifico()
                }
            }else if(rbTodos.isChecked){
                ObtenerReporteAlmacenEspecifico()
            }

        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle("Reporte de almacén" )
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
    fun ObtenerReporteAlmacenEspecifico(){
        if(rbTodos.isChecked){
            controladorProcesoCargar.IniciarDialogCarga("Cargando información")
            asyncReporteAlmacen.ObtenerReporteProductosAlmacen(0)
            asyncReporteAlmacen.setReporteAlmacen(this)
        }else if(rbUno.isChecked){
            if(spnAlmacenes.selectedItemPosition==0){
                AlertDialog.Builder(this).setTitle("Advertencia")
                        .setMessage("Debe seleccionar un almacén ").
                                setPositiveButton("Salir",null).create().show()
            }else{

                controladorProcesoCargar.IniciarDialogCarga("Cargando información")
                asyncReporteAlmacen.ObtenerReporteProductosAlmacen(listaAlmacen.get(spnAlmacenes.selectedItemPosition-1).idAlmacen)
                asyncReporteAlmacen.setReporteAlmacen(this)

            }
        }
    }


    inner class ProcesarDatos():AsyncTask<MutableList<mAlmacenProducto>?,Void,String>(){
        private lateinit var tabla:ArrayList<ArrayList<String>>
        private lateinit var columnas:ArrayList<String>
        private lateinit var html:Html

        override fun onPreExecute() {
            controladorProcesoCargar.IniciarDialogCarga("Generando Reporte")
             tabla= ArrayList()
            columnas= ArrayList()
             html=Html()

        }

        override fun doInBackground(vararg params: MutableList<mAlmacenProducto>?): String {
            var totalCompra=BigDecimal(0)
            var tempIdAlmacen=0
            var contador=0
            tempIdAlmacen=params[0]!!.get(0).idAlmacen
            var cabecera=ArrayList<String>()
            cabecera.add("Nombre Producto")
            cabecera.add("Cantidad Disponible")
            cabecera.add("Precio Compra ${Constantes.DivisaPorDefecto.SimboloDivisa}")
            cabecera.add("Total compra ${Constantes.DivisaPorDefecto.SimboloDivisa}")
            html.AgregarTituloCentral(Constantes.Empresa.nombreTienda)
            html.AgregarSubtitulo("Reporte de Almacenes")
            html.AgregarTituloIntermedio(params[0]!!.get(0).descripcionAlmacen)
            while(params[0]!!.size>contador){
                if(tempIdAlmacen==params[0]!!.get(contador).idAlmacen){
                    columnas= ArrayList()
                    columnas.add("${params[0]!!.get(contador).nombreProducto} " +
                            "${params[0]!!.get(contador).descripcionVariante}${Constantes.EHTML.Left}")
                    columnas.add("${String.format("%.2f",params[0]!!.get(contador).cantidadDisponible)}")
                    columnas.add("${String.format("%.2f",params[0]!!.get(contador).precioCompra)}")
                    columnas.add("${String.format("%.2f",params[0]!!.get(contador).getTotalCompra())}")
                    totalCompra=totalCompra.add(params[0]!!.get(contador).getTotalCompra())
                    tabla.add(columnas)
                    contador+=1
                }else{
                    columnas=ArrayList()
                    columnas.add("")
                    columnas.add("")
                    columnas.add("")
                    columnas.add("${String.format("%.2f",totalCompra)} ")
                    tabla.add(columnas)
                    html.AgregarTabla(cabecera,tabla)
                    html.SaltoPagina()
                    tempIdAlmacen=params[0]!!.get(contador).idAlmacen
                    html.AgregarTituloIntermedio(params[0]!!.get(contador).descripcionAlmacen)
                    totalCompra= BigDecimal(0)
                    tabla=ArrayList()
                }
            }
            columnas=ArrayList()
            columnas.add("")
            columnas.add("")
            columnas.add("")
            columnas.add("${String.format("%.2f",totalCompra)} ")
            tabla.add(columnas)
            html.AgregarTabla(cabecera,tabla)

            return html.ObtenerHtml()
        }

        override fun onPostExecute(result: String?) {
            val pfdGenerator=PdfGenerator(this@ReporteAlmacen)

            controladorProcesoCargar.FinalizarDialogCarga()
            pfdGenerator.GenerarPdf(html=result!!,nombreDoc = "Reporte_Almacen")

        }
    }
    override fun ObtenerReporteAlmacen(listaReporte: MutableList<mAlmacenProducto>?) {
        controladorProcesoCargar.FinalizarDialogCarga()
        if(listaReporte!!.size>0){
            ProcesarDatos().execute(listaReporte)
        }else{
            AlertDialog.Builder(this)
                    .setTitle("Advertencia")
                    .setMessage("No tiene productos en su almacén")
                    .setPositiveButton("Salir",null)
                    .create().show()
        }

    }

    override fun ErrorObtenerAlmacen() {
        controladorProcesoCargar.FinalizarDialogCarga()
        Toast.makeText(this,"Error al conseguir el reporte de el almacén.Verifique su conexión a internet",Toast.LENGTH_LONG).show()
    }
}











