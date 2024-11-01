package com.omarchdev.smartqsale.smartqsaleventas.Fragment
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporte
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasVendedor
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVentasVendedor
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class ReporteVentasVendedor : Fragment(), DatePickerDialog.OnDateSetListener, AsyncReporte.ResultadoVentasVendedor, OnChartValueSelectedListener {
    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun ErrorConsulta() {
        pb!!.visibility=View.GONE
        txtMensaje!!.visibility=View.VISIBLE
        contentData!!.visibility=View.INVISIBLE
        txtMensaje!!.text="Error al consultar la información.Verifique su conexión a internet."

    }

    override fun ResultadosConsulta(ventasVendedors: MutableList<mVentasVendedor>?) {
        pb!!.visibility = View.GONE
        adapter.AgregarLista(ArrayList(ventasVendedors!!))
        if(ventasVendedors!!.size>0) {
             txtMensaje!!.visibility = View.GONE
            contentData!!.visibility = View.VISIBLE
            ObtenerDatosVentas(ArrayList(ventasVendedors))
            mChart!!.visibility=View.VISIBLE
            rvVendedores!!.visibility=View.VISIBLE
            txt1?.visibility=View.VISIBLE
            txt2?.visibility=View.VISIBLE


        }else{
            txt1?.visibility=View.INVISIBLE
            txt2?.visibility=View.INVISIBLE
            contentData!!.visibility = View.VISIBLE
            txtMensaje!!.visibility=View.VISIBLE

            txtMensaje!!.text="No existe resultados disponibles para el periodo seleccionado"
            mChart!!.visibility=View.INVISIBLE
            rvVendedores!!.visibility=View.INVISIBLE
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        when(origen){
            1.toByte()->{
                fInicioText=GenerarFechaTexto(dayOfMonth,month+1,year)
                btnFechaInicio!!.setText(TextoFecha(resources.getString(R.string.FechaInicial),dayOfMonth,month+1,year))
            }

            2.toByte()->{
                fFinalText=GenerarFechaTexto(dayOfMonth,month+1,year)
                btnFechaFinal!!.setText(TextoFecha(resources.getString(R.string.FechaFinal),dayOfMonth,month+1,year))
            }
        }
        asyncReporte.ObtenerVentasVendedor(0, fInicioText,fFinalText)
        pb!!.visibility = View.VISIBLE
        txtMensaje!!.visibility = View.INVISIBLE
        contentData!!.visibility = View.INVISIBLE
    }

    private fun TextoFecha(titulo: String, dia: Int, mes: Int, anio: Int): String = "$titulo \n $dia/$mes/$anio"
    val asyncReporte= AsyncReporte()
    var fechaInicio=""
    var fechaFinal=""
    var origen: Byte=0
    var idVendedor=0
    internal val c = Calendar.getInstance()
    val selectFecha = DialogDatePickerSelect()
    val calendar: Calendar = Calendar.getInstance()
    var dia: Int = 0
    var mes: Int = 0
    var anio: Int = 0
    var fInicioText = ""
    var fFinalText = ""
    var permitir = true
    val fm = fragmentManager
    var btnFechaInicio:Button?=null
    var btnFechaFinal:Button?=null
    var pb:ProgressBar?=null
    var txtMensaje:TextView?=null
    var contentData:RelativeLayout?=null
    var rvVendedores: RecyclerView?=null
    var txt1:TextView?=null
    var txt2:TextView?=null
    val adapter= RvAdapterVentasVendedor()

    private var mChart: BarChart? = null
    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_reporte_ventas_vendedor, container, false)
      try {
          txt1=v.findViewById(R.id.txt1)
          txt2=v.findViewById(R.id.txt2)
          btnFechaInicio=v.findViewById(R.id.btnFechaInicio)
          btnFechaFinal=v.findViewById(R.id.btnFechaFinal)
          pb=v.findViewById(R.id.pb)
          txtMensaje=v.findViewById(R.id.txtMensaje)
          contentData=v.findViewById(R.id.contentData)
          rvVendedores=v.findViewById(R.id.rvVendedores)
          mChart = v.findViewById<View>(R.id.barChartResumen) as BarChart
          rvVendedores!!.adapter=adapter
          listenerButtonFiltro()
          GenerarFechaOrigen()
          listenerButtonFiltro()

          asyncReporte.ObtenerVentasVendedor(0, fInicioText,fFinalText)
          pb!!.visibility = View.VISIBLE
          txtMensaje!!.visibility = View.INVISIBLE
          contentData!!.visibility = View.INVISIBLE
         asyncReporte.setResultadoVentasVendedor(this)
          mChart?.setOnChartValueSelectedListener(this)
          mChart?.setDrawBarShadow(false)
          mChart?.setDrawValueAboveBar(false)
          mChart!!.getLegend().isEnabled = false
          mChart!!.setPinchZoom(false)
          mChart!!.setDrawGridBackground(false)
          mChart!!.setDrawBorders(false)
          rvVendedores!!.setNestedScrollingEnabled(false)
          rvVendedores!!.setHasFixedSize(true)

      }catch (e:Exception){
          Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show()
      }
        return v
    }

    private fun GenerarFechaOrigen(){
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaFinal!!.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))
        fFinalText=GenerarFechaTexto(dia,mes,anio)
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaInicio!!.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
        fInicioText=GenerarFechaTexto(dia,mes,anio)
    }
    private fun MostrarSeleccionarFecha(origen:Byte){

        selectFecha.setOrigen(origen,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
    }
    private fun listenerButtonFiltro() {

        btnFechaInicio!!.setOnClickListener{
            MostrarSeleccionarFecha(1)
            DatePickerDialog(requireActivity(), this, anio, mes-1 , dia).show()
            origen=1
        }
        btnFechaFinal!!.setOnClickListener{
            MostrarSeleccionarFecha(1)
            DatePickerDialog(requireActivity(), this, anio, mes -1, dia).show()
            origen=2
        }
    }

    fun ObtenerDatosVentas(lista:ArrayList<mVentasVendedor>) {
        val count: Int
        val dataSets: ArrayList<BarDataSet>? = null
        val yVals = ArrayList<BarEntry>()
        val xVals = ArrayList<String>()

        if (lista != null) {
            count = lista.size
            if (lista.size == 0) {
                 txtMensaje!!.setVisibility(View.VISIBLE)
                mChart!!.setVisibility(View.GONE)
            } else if (lista.size > 0) {
                mChart!!.setVisibility(View.VISIBLE)
                for (i in 0 until count) {
                    try {
                        val rm = RoundingMode.DOWN
                        yVals.add(BarEntry(i.toFloat(), lista[i].montoVentas.toInt().toFloat()))
                        xVals.add("${lista.get(i).vendedor.primerNombre}")
                      //  xVals.add(String(list[i].horaInicio.toString()) + "-" + String((list[i].horaInicio + 1).toString()) + "h   ")
                    } catch (e: Exception) {
                        e.toString()
                    }

                }

                val barDataSet = BarDataSet(yVals, "")
                val data = BarData(barDataSet)
                barDataSet.valueFormatter = IValueFormatter { value, entry, dataSetIndex, viewPortHandler -> " " }

                barDataSet.color = Color.parseColor("#00BDFF")
                mChart!!.getAxisLeft().valueFormatter = IAxisValueFormatter { value, axis -> Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", value) }
                mChart!!.getDescription().text = "Valor de ventas por vendedor"
                mChart!!.getAxisRight().isEnabled = false
                mChart!!.animateY(1000)
                val xAxis = mChart!!.getXAxis()
                xAxis.isEnabled = true
                xAxis.setDrawGridLines(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM

                xAxis.granularity = 1f
                xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                    if (xVals.size > value.toInt()) {
                        xVals[value.toInt()]
                    } else {

                        " "
                    }
                }
                mChart!!.setData(data)


            }
        } else {
            mChart!!.setVisibility(View.GONE)
        }


    }


}















