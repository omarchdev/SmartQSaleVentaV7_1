package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReportePeriodo
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.SelectTienda
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_periodo_tienda.*
import java.util.*

class ReportePeriodoTienda : ActivityParent(), SelectTienda.TiendaInterface {

    var dia: Int = 0
    var mes: Int = 0
    var anio: Int = 0
    var origen: Byte = 0
    var idTienda=0
    val calendar: Calendar = Calendar.getInstance()
    val aReporte=AsyncReportePeriodo(this@ReportePeriodoTienda)
    var fInicioText = ""
    var fFinalText = ""
    var fechaInicio=""
    var fechaFinal=""
    val selectFecha = DialogDatePickerSelect()
    internal val c = Calendar.getInstance()
    private fun TextoFecha(titulo: String, dia: Int, mes: Int, anio: Int): String = "$titulo \n $dia/$mes/$anio"
    lateinit var selectTienda: SelectTienda

    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"

    private fun Fecha(dia:Int, mes:Int, anio:Int):String="$dia/$mes/$anio"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_periodo_tienda)
        GenerarFechaOrigen()
        GetFecha()
        listenerButtonFiltro()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.elevation = 4f
        supportActionBar?.setTitle("Reporte por periodo")
        selectTienda= SelectTienda().newInstance(this,"Todas las tiendas ")

        val fml = supportFragmentManager
        val ftl = fml.beginTransaction()
        ftl.replace(R.id.content_tienda,selectTienda)
        ftl.commit()
        btnGenerarReporte.setOnClickListener {

            aReporte.ObtenerReportePeriodo(idTienda,fInicioText,fFinalText,fechaInicio,fechaFinal)

        }
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
        selectFecha.setOrigen(origen,c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH))
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
                    fechaFinal= Fecha(dia,mes,anio)
                    btnFechaFinal.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))}
                1.toByte()->{
                    fInicioText=GenerarFechaTexto(dia,mes,anio)
                    btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
                    fechaInicio=Fecha(dia,mes,anio)
                }

            }
         }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }


    private fun GenerarFechaOrigen(){
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaFinal.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))
        fechaFinal= Fecha(dia,mes,anio)

        fFinalText=GenerarFechaTexto(dia,mes,anio)
        calendar.add(Calendar.DAY_OF_MONTH, -10)
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
        fInicioText=GenerarFechaTexto(dia,mes,anio)
        fechaInicio= Fecha(dia,mes,anio)

    }

    override fun TiendaPorDefecto() {
    }

    override fun TiendaSeleccionada(id: Int) {
        idTienda=id
    }

    override fun SeCargoTiendas() {
    }

}