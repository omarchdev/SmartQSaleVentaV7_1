package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedidos
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectCustomer
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCabeceraPedido
import kotlinx.android.synthetic.main.activity_pedidos_despacho.*
import java.util.*
import kotlin.collections.ArrayList

class PedidosDespacho : AppCompatActivity(),ClickListener, View.OnClickListener, DialogDatePickerSelect.interfaceFecha, dialogSelectCustomer.DatosCliente, AsyncPedidos.ListenerObtenerPedidos, AsyncPedidos.ResultPedidosTask {

    override fun ResultadosConsultas(result: List<mCabeceraPedido>?) {

        Toast.makeText(this@PedidosDespacho,result!!.size.toString(),Toast.LENGTH_SHORT).show()

    }

    override fun ErrorConsulta() {

    }

    override fun ErrorPedidos() {

        Toast.makeText(this@PedidosDespacho,"Error",Toast.LENGTH_LONG).show()
    }

    override fun obtenerDato(customer: mCustomer?) {
        idCliente=customer!!.getiId()
        txtSeleccionarCliente.text=customer.getcName()
    }


    internal var fechaInicio: Int = 0
    internal var fechaFinal:Int = 0
    internal var year: Int = 0
    internal var month:Int = 0
    internal var day:Int = 0
    internal var idCliente: Int = 0
    internal var origen: Byte = 0
    internal var fIni=""
    internal var fFin=""
    var listaPedidos=ArrayList<mCabeceraPedido>()
    val delete:Byte=50
    val obtener:Byte=60
    val adapterPedidos= RvAdapterCabeceraPedido()
    val lManager=LinearLayoutManager(this@PedidosDespacho,LinearLayoutManager.VERTICAL,false)
    var fecha={d:Int,m:Int,a:Int->"${d}/${m}/${a}"}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_despacho)
        adapterPedidos.clickListener=this
        adapterPedidos.listaPedidos=listaPedidos
        rv.adapter=adapterPedidos
        rv.layoutManager=lManager
        idCliente = 0
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1
        day = c.get(Calendar.DAY_OF_MONTH)
        fechaFinal = year * 10000 + month * 100 + day
        //c.add(Calendar.DAY_OF_MONTH, -1)
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1
        day = c.get(Calendar.DAY_OF_MONTH)
        fechaInicio = year * 10000 + month * 100 + day
        btnFechaInicio.text=fecha(day,month,year)
        btnFechaFinal.text=fecha(day,month,year)
        /*
        *   btnFechaInicio.setText(convertirFormatoFecha(fechaInicio));
        btnFechaFinal.setText(convertirFormatoFecha(fechaFinal));
        * */
        btnFechaInicio.setOnClickListener(this)
        btnFechaFinal.setOnClickListener(this)
        txtSeleccionarCliente.setOnClickListener(this)
        fIni=generarTextoFecha(day,month,year)
        fFin=generarTextoFecha(day,month,year)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()

    //    asyncPedidos.ReiniciarConsultas()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
   }

    fun generarTextoFecha(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"

    override fun onClick(v: View?) {
        var a: Int
        var m: Int
        var d: Int
        when(v!!.id){

            R.id.btnFechaInicio->{
                origen = 1
                a = fechaInicio / 10000
                m = fechaInicio % 10000 / 100
                d = fechaInicio % 100
                MostrarDialogDatePicker(origen, a, m, d)

            }
            R.id.btnFechaFinal->{
                origen = 2
                a = fechaFinal / 10000
                m = fechaFinal % 10000 / 100
                d = fechaFinal % 100
                MostrarDialogDatePicker(origen, a, m, d)
            }
            R.id.txtSeleccionarCliente->{
                MostrarDialogSeleccionCliente()
            }
        }
    }

    private fun MostrarDialogDatePicker(origen: Byte, year: Int, month: Int, day: Int) {
        val dialogDatePickerSelect = DialogDatePickerSelect().newInstance(origen, year, month, day)
        dialogDatePickerSelect.setFechaListener(this)
        dialogDatePickerSelect.show(this.fragmentManager, "Dialog Fecha")
    }


    private fun convertirFormatoFecha(fecha: Int): String {
        val anio = fecha / 10000
        val mes = fecha % 10000 / 100
        val dia = fecha % 100
        return if (dia < 10) {
            "0" + dia.toString() + "/" + mes.toString() + "/" + anio.toString()
        } else dia.toString() + "/" + mes.toString() + "/" + anio.toString()

    }

    override fun getFechaSelecionada(day: Int, month: Int, year: Int, origen: Byte) {

        when(origen){

            1.bt->{

                this.year = year
                this.month = month
                this.day = day
                fechaInicio = year * 10000 + month * 100 + day
                btnFechaInicio.text=fecha(day,month,year)
                fIni=generarTextoFecha(day,month,year)

            }
            2.bt->{
                this.year = year
                this.month = month
                this.day = day
                btnFechaFinal.text=fecha(day,month,year)
                fFin=generarTextoFecha(day,month,year)
                fechaFinal = year * 10000 + month * 100 + day


            }

        }

    }

    override fun clickPositionOption(position: Int, accion: Byte) {
        when(accion){
            delete->{

            }
            obtener->{

            }
        }
    }

    private fun MostrarDialogSeleccionCliente() {

    }

    override fun PedidosResultado(result: List<mCabeceraPedido>?) {
    }


}
private val Int.bt: Any
    get() {
        return this.toByte()
    }



