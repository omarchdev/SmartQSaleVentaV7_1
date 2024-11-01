package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.graphics.Color
import android.os.Bundle

import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_char_ventas_producto.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html

import java.util.*


class DfChartProductoVenta():DialogFragment(), OnChartValueSelectedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_char_ventas_producto, container, false)
    }
    var dia: Int = 0
    var mes: Int = 0
    var anio: Int = 0
    var origen: Byte = 0
    val calendar: Calendar = Calendar.getInstance()
    var fechaInicio=""
    var fechaFinal=""
    var b=false
    var html= Html()
    val selectFecha = DialogDatePickerSelect()

    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"
    private fun TextoFecha(titulo: String, dia: Int, mes: Int, anio: Int): String = "$titulo \n $dia/$mes/$anio"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GenerarFechaOrigen()
        listenerButtonFiltro()
        GetFecha()
        rgSeleccion.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbUnidades->{
                    listenerChangedSelection?.VentasPorUnidades(fechaInicio,fechaFinal)
                    pbChart.visibility=View.VISIBLE
                    b=false
                }
                R.id.rbMonto->{
                    listenerChangedSelection?.VentasPorMonto(fechaInicio,fechaFinal)
                    pbChart.visibility=View.VISIBLE
                    b=true
                }
            }
        }
        rgSeleccion.check( R.id.rbUnidades)
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
                    fechaFinal=GenerarFechaTexto(dia,mes,anio)
                    btnFechaFinal.setText(TextoFecha(
                            resources.getString(R.string.FechaFinal),dia,mes,anio))


                    if(b){
                        listenerChangedSelection?.VentasPorMonto(fechaInicio,fechaFinal)
                    }else{
                        listenerChangedSelection?.VentasPorUnidades(fechaInicio,fechaFinal)

                    }
                }

                1.toByte()->{
                    fechaInicio=GenerarFechaTexto(dia,mes,anio)
                    btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
                    if(b){
                        listenerChangedSelection?.VentasPorMonto(fechaInicio,fechaFinal)
                    }else{
                        listenerChangedSelection?.VentasPorUnidades(fechaInicio,fechaFinal)

                    }
                }
            }
            //  asyncMovAlmacen.ObtenerMovimientosAlmacen(fInicioText,fFinalText)
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
        selectFecha.setOrigen(origen,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
        selectFecha.show(requireActivity().fragmentManager,"Fecha")
    }
    private fun GenerarFechaOrigen(){
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaFinal.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))
        fechaInicio=GenerarFechaTexto(dia,mes,anio)
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
        fechaFinal=GenerarFechaTexto(dia,mes,anio)
    }

    fun ErrorConsulta(){
        pbChart.visibility=View.GONE
    }

    var listenerChangedSelection:ListenerChangedSelection?=null

    interface ListenerChangedSelection {
        fun VentasPorMonto(fechaInicio:String,fechaFinal:String)
        fun VentasPorUnidades(fechaInicio:String,fechaFinal:String)

    }
    fun newInstance():DfChartProductoVenta{
        val d=DfChartProductoVenta()
        return d

    }

    fun AgregarProductosLista(productList: List<mProduct>){
        pbChart.visibility=View.GONE
        pieChartResumen.setUsePercentValues(true);
        pieChartResumen.getDescription().setEnabled(true);
        var d=pieChartResumen.description
        d.text="Top 10 Productos más vendidos"
        pieChartResumen.setDragDecelerationFrictionCoef(0.5f)
        pieChartResumen.setDrawHoleEnabled(true)
        pieChartResumen.setHoleColor(Color.WHITE)
        pieChartResumen.setTransparentCircleColor(Color.WHITE)
        pieChartResumen.setTransparentCircleAlpha(110)
        pieChartResumen.setHoleRadius(20f)
        pieChartResumen.setTransparentCircleRadius(40f)
        pieChartResumen.setDrawCenterText(true)
        pieChartResumen.setRotationAngle(0f)
        // enable rotation of the chart by touch
        pieChartResumen.setRotationEnabled(false)
        pieChartResumen.setHighlightPerTapEnabled(true)
        pieChartResumen.getDrawingCache(true)
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);
        // add a selection listener
        pieChartResumen.setOnChartValueSelectedListener(this)
        var l= pieChartResumen.legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(8f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)
        pieChartResumen.setDrawEntryLabels(false)
        pieChartResumen.setEntryLabelColor(Color.TRANSPARENT)
        pieChartResumen.setEntryLabelTextSize(12f)

        setData(10, 100f,productList)


    }



    private fun setData(count: Int, range: Float,list:List<mProduct>) {

        val entries = ArrayList<PieEntry>()


        if(b) {
            list.forEach {
                entries.add(PieEntry(it.precioVenta.toFloat(),
                        "${it.getcProductName()}\n " +
                                "${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f", it.precioVenta)}"))
            }
        }else{
            list.forEach {
                entries.add(PieEntry(it.getdQuantity(),
                        "${it.getcProductName()}\n ${String.format("%.2f", it.getdQuantity())}"))
            }
        }
        val dataSet = PieDataSet(entries, "10 Productos mas vendidos")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 10f
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        pieChartResumen.setData(data)
        // undo all highlights
        pieChartResumen.highlightValues(null)
        pieChartResumen.invalidate()
        WindowManager.LayoutParams.MATCH_PARENT
/*
        pieChartResumen.visibility=View.GONE

        var p=PieChart(context)

          p.layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400)
        pieChartResumen.setUsePercentValues(true);
        pieChartResumen.getDescription().setEnabled(true);
        var d=pieChartResumen.description
        d.text="Top 10 Productos más vendidos"
        p.setDragDecelerationFrictionCoef(0.5f)
        p.setDrawHoleEnabled(true)
        p.setHoleColor(Color.WHITE)
        p.setTransparentCircleColor(Color.WHITE)
        p.setTransparentCircleAlpha(110)
        p.setHoleRadius(20f)
        p.setTransparentCircleRadius(40f)
        p.setDrawCenterText(true)
        p.setRotationAngle(0f)
        //enable rotation of the chart by touch
        p.setRotationEnabled(false)
        p.setHighlightPerTapEnabled(true)
        p.getDrawingCache(true)
        var l= p.legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(8f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);
        // add a selection listener
        p.setOnChartValueSelectedListener(this)
        p.data=data

        //      Generar Pdf de reporte imagen
        try {
            html.AgregarImagen(pieChartResumen.chartBitmap)

        }catch (e:Exception){
            e.toString()
        }
        val pfdGenerator= PdfGenerator(requireContext())
        pfdGenerator.GenerarPdf(html=html.ObtenerHtml(),nombreDoc = "Reporte Caja")
*/

        // html.ObtenerHtml()
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

}