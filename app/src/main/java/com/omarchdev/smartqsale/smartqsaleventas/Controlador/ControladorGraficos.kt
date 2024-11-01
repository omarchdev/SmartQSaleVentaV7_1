package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenFlujoCaja
import java.util.ArrayList

class ControladorGraficos{




    private fun ColorsDataSet():ArrayList<Int>{
        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)


        for(c in ColorTemplate.MATERIAL_COLORS)
            colors.add(c)
        /*      for (c in ColorTemplate.COLORFUL_COLORS)
               colors.add(c)
   for (c in ColorTemplate.JOYFUL_COLORS)
               colors.add(c)

       for (c in ColorTemplate.LIBERTY_COLORS)
           colors.add(c)
*/
        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)
        return  colors
    }
    private fun ConfigStyleChart (dataSet:PieDataSet){
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 10f
        val colors = ArrayList<Int>()

           for (c in ColorTemplate.VORDIPLOM_COLORS)
               colors.add(c)


        for(c in ColorTemplate.MATERIAL_COLORS)
            colors.add(c)
        /*      for (c in ColorTemplate.COLORFUL_COLORS)
               colors.add(c)
   for (c in ColorTemplate.JOYFUL_COLORS)
               colors.add(c)

       for (c in ColorTemplate.LIBERTY_COLORS)
           colors.add(c)
*/
         for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors=colors
    }
    private fun ConfigPieData(data: PieData){

    }

    fun GraficosTop10Productos(pieChartResumen:PieChart,descripcion: String,list:List<ProductoEnVenta>){

        ConfigPieChart(pieChartResumen,descripcion)
        val entries = ArrayList<PieEntry>()
        list.forEachIndexed { i, it ->  entries.add(PieEntry(it.precioVentaFinal.toFloat(),"${it.descripcionCombo}/${it.productName}/${it.descripcionVariante} ${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",it.precioVentaFinal)}"))}

        val dataSet = PieDataSet(entries,descripcion)
        ConfigStyleChart(dataSet)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        pieChartResumen.setData(data)
        pieChartResumen.highlightValues(null)
        pieChartResumen.invalidate()
    }

    fun ConfigBarChart(barChart: BarChart,descripcion:String,
                       entriesY:List<BarEntry>,valuesX:List<String>){


        var barDataSet=BarDataSet(entriesY,"Cierres")
        var data=BarData(barDataSet)

        barDataSet.valueFormatter= IValueFormatter { value,
                                                     entry,
                                                     dataSetIndex,
                                                     viewPortHandler ->


            "${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",value)}"
        }

        barDataSet.colors=ColorsDataSet()
        barChart.axisLeft.valueFormatter= IAxisValueFormatter { value, axis ->
            "${Constantes.DivisaPorDefecto.SimboloDivisa}${String.format("%.2f",value)}"
        }

        barChart.axisRight.isEnabled=false
        barChart.description.text="$descripcion"
        barChart.legend.isEnabled=true
        barChart.animateX(1000)
        var xAxis=barChart.xAxis
        xAxis.isEnabled=true

        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            if (valuesX.size > value.toInt()) {
                valuesX.get(value.toInt())
            } else {
                " "
            }
        }

        var legend=barChart.legend
        legend.isEnabled=false
        // ConfigLegends(legend)
        var description=barChart.description
        barChart.data=data


    }

    private fun ConfigPieChart(pieChartResumen:PieChart,descripcion:String){

        pieChartResumen.setUsePercentValues(true);
        pieChartResumen.getDescription().setEnabled(true);
        var d=pieChartResumen.description
        d.text="$descripcion"
        pieChartResumen.setDragDecelerationFrictionCoef(0.5f)
        pieChartResumen.setDrawHoleEnabled(true)
        pieChartResumen.setHoleColor(Color.WHITE)
        pieChartResumen.setTransparentCircleColor(Color.WHITE)
        pieChartResumen.setTransparentCircleAlpha(110)
        pieChartResumen.setHoleRadius(20f)
        pieChartResumen.setTransparentCircleRadius(40f)
        pieChartResumen.setDrawCenterText(true)
        pieChartResumen.setEntryLabelColor(Color.TRANSPARENT)
        pieChartResumen.setRotationAngle(0f)
        // enable rotation of the chart by touch
        pieChartResumen.setRotationEnabled(true)
        pieChartResumen.setHighlightPerTapEnabled(true)
        pieChartResumen.getDrawingCache(true)
        var l= pieChartResumen.legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(8f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)

    }

    private fun ConfigLegends(l:Legend){
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(8f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)
    }

    fun GraficoResumenPagos(pieChartResumen:PieChart,descripcion: String,list:List<mResumenFlujoCaja>){


        ConfigPieChart(pieChartResumen,descripcion)
        val entries = ArrayList<PieEntry>()

        list.forEachIndexed { i, it -> if(i!=0) entries.add(PieEntry(it.monto.toFloat(),
                "${it.descripcionTitulo} ${Constantes.DivisaPorDefecto.SimboloDivisa}" +
                        " ${String.format("%.2f",it.monto)}"))}
        val dataSet = PieDataSet(entries,"$descripcion")
        ConfigStyleChart(dataSet)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
           data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)

        pieChartResumen.setData(data)
        pieChartResumen.highlightValues(null)
        pieChartResumen.invalidate()

    }


}











