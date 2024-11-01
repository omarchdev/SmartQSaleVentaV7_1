package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporteIngresoRetiro
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_ingresos_retiros_periodo.*
import java.util.*

class ReporteIngresosRetirosPeriodo : ActivityParent() {

    var dia: Int = 0
    var mes: Int = 0
    var anio: Int = 0
    var origen: Byte = 0
    val calendar: Calendar = Calendar.getInstance()
    var fInicioText = ""
    var fFinalText = ""
    val selectFecha = DialogDatePickerSelect()
    internal val c = Calendar.getInstance()
    val asyncReporteIngresosRetirosPeriodo=AsyncReporteIngresoRetiro(this)

    private fun TextoFecha(titulo: String, dia: Int, mes: Int, anio: Int): String = "$titulo \n $dia/$mes/$anio"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_ingresos_retiros_periodo)
        GenerarFechaOrigen()
        GetFecha()
        listenerButtonFiltro()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.elevation = 4f
        supportActionBar?.setTitle("Reporte de Ingresos y Salidas Caja")

        btnGenerarReporte.setOnClickListener {

            asyncReporteIngresosRetirosPeriodo.ObtenerReporteIngresosRetirosPeriodoFecha( fInicioText,fFinalText)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun listenerButtonFiltro() {

        btnFechaInicio.setOnClickListener{
            MostrarSeleccionarFecha(1)
        }
        btnFechaFinal.setOnClickListener{
            MostrarSeleccionarFecha(2)
        }
    }

    private fun MostrarSeleccionarFecha(origen:Byte){
        selectFecha.setOrigen(origen,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
        selectFecha.show(this.fragmentManager,"Fecha")
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

    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"

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
}
