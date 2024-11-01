package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCaja
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporte
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVendedores
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasVendedor
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVentasVendedor
import kotlinx.android.synthetic.main.activity_reporte_ventas_caja.*
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class ReporteVentasCaja : ActivityParent(), AsyncReporte.ListenerVentasPorCierre {

    internal var dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm aa")
    internal var fechaInicio: String=""
    internal var fechaFinal:String=""
    internal val CODE_RESULT_ID_CAJA = 1
    var idCierre=0
    internal val c = Calendar.getInstance()
    val asyncReporte=AsyncReporte()
    val asyncCaja=AsyncCaja(this@ReporteVentasCaja)
    val adapter= RvAdapterVentasVendedor()
    val asyncVendedores = AsyncVendedores()
    override fun ErrorConsulta() {
        Toast.makeText(this,"Error al conseguir las ventas de la caja ",Toast.LENGTH_LONG).show()

        content.visibility=View.INVISIBLE
        txtMensaje.visibility=View.VISIBLE
        pb.visibility= View.GONE
    }


    fun ModificarDatosCabecera(cierre: mCierre) {

        try {
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
        }catch (e:Exception){
            Toast.makeText(this@ReporteVentasCaja,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun ResultadoVentasPorCierre(listaResultado: MutableList<mVentasVendedor>?, cierre: mCierre?) {
        try {
            content.visibility = View.VISIBLE
            txtMensaje.visibility = View.GONE
            pb.visibility = View.GONE
            adapter.AgregarLista(ArrayList(listaResultado!!))
            ObtenerDatosVentas(ArrayList(listaResultado))
            ModificarDatosCabecera(cierre!!)
        }catch (e:Exception){
            Toast.makeText(this@ReporteVentasCaja,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_ventas_caja)
        txtMensaje.visibility=View.INVISIBLE
        pb.visibility= View.VISIBLE
        content.visibility=View.INVISIBLE
        rvVentasVendedores.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rvVentasVendedores.adapter=adapter
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

        supportActionBar?.setTitle("Ventas por Caja")
        asyncCaja.ObtenerIdCierreCaja()
        asyncCaja.setListenerAperturaCaja(object:AsyncCaja.ListenerAperturaCaja {


            override fun ConfirmacionAperturaCaja() {

            }

            override fun AperturarCaja() {
                txtMensaje.visibility=View.INVISIBLE
                pb.visibility= View.GONE
                content.visibility=View.INVISIBLE
            }
            override fun ConfirmarCierreCaja() {
            }
            override fun ExisteCajaAbiertaDisponible() {
            }
            override fun CajaCerrada() {
            }
            override fun ExisteCierre(Cierre: mCierre?) {

                OrdenarVentasPorCierre(Cierre!!.idCierre)
                idCierre=Cierre!!.idCierre
             }
            override fun ErrorEncontrarCaja() {
                Toast.makeText(this@ReporteVentasCaja,"Error al conseguir la informacion de la caja."
                        ,Toast.LENGTH_LONG).show()

            }
        })
        cv_select_cierre.setOnClickListener {
            val intent = Intent(this@ReporteVentasCaja, HistorialCierresCaja::class.java)
            startActivityForResult(intent, CODE_RESULT_ID_CAJA)
        }
     }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_RESULT_ID_CAJA) {
            if (resultCode == RESULT_CANCELED) {
            } else {
                idCierre=data!!.getIntExtra("idCierre", 0)
                if(idCierre!=0) {
                    OrdenarVentasPorCierre(idCierre)
                }
            }
        }
    }

    fun OrdenarVentasPorCierre(cierre:Int){

        try {
            pb.visibility = View.VISIBLE
            content.visibility = View.INVISIBLE
            txtMensaje.visibility = View.INVISIBLE
            asyncReporte.ObtenerVentasPorCierre(cierre!!)
            asyncReporte.setListenerVentasPorCierre(this)
        }catch (e:Exception){
            Toast.makeText(this@ReporteVentasCaja,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun ObtenerDatosVentas(lista:ArrayList<mVentasVendedor>) {
        val count: Int
    //    val dataSets: ArrayList<BarDataSet>? = null
        val yVals = ArrayList<BarEntry>()
        val xVals = ArrayList<String>()

        if (lista != null) {
            count = lista.size
            if (lista.size == 0) {
                txtMensaje!!.setVisibility(View.VISIBLE)
                barChartResumen!!.setVisibility(View.GONE)
            } else if (lista.size > 0) {
                barChartResumen!!.setVisibility(View.VISIBLE)
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
                barChartResumen!!.getAxisLeft().valueFormatter = IAxisValueFormatter { value, axis -> Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", value) }
                barChartResumen!!.getDescription().text = "Valor de ventas por vendedor"
                barChartResumen!!.getAxisRight().isEnabled = false
                barChartResumen!!.animateY(1000)
                val xAxis = barChartResumen!!.getXAxis()
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
                barChartResumen!!.setData(data)
            }
        } else {
            barChartResumen!!.setVisibility(View.GONE)
        }

    }



}
