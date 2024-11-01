package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedidos
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPedidoskt

import kotlinx.android.synthetic.main.activity_pedidos_tienda.*
import java.util.*
import kotlin.collections.ArrayList

class PedidosTienda : ActivityParent(), ClickListener,
        AsyncPedidos.ListenerObtenerPedidos, DialogDatePickerSelect.interfaceFecha, View.OnClickListener,
        SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    val c = Calendar.getInstance()


    var identificador=""
    internal var origen: Byte = 0
    val asyncPedidos=AsyncPedidos(this@PedidosTienda)
    var listaPedido=ArrayList<mCabeceraPedido>()
    val rvAdapterPedidos= RvAdapterPedidoskt(listaPedido)
    internal var fechaInicio: Int = 0
    internal var fechaFinal:Int = 0
    internal var year: Int = 0
    internal var month:Int = 0
    internal var day:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_tienda)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar!!.title ="Gestion de pedidos "
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        rvPedidos.adapter=rvAdapterPedidos
        rvAdapterPedidos.listenerClickListener=this

         year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1
        day = c.get(Calendar.DAY_OF_MONTH)
        fechaFinal = year * 10000 + month * 100 + day
        c.add(Calendar.DAY_OF_MONTH, -5)
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH) + 1
        day = c.get(Calendar.DAY_OF_MONTH)
        fechaInicio = year * 10000 + month * 100 + day
        asyncPedidos.listenerObtenerPedidos=this
        btnSelectDate1.text = convertirFormatoFecha(fechaInicio)
        btnSelectDate2.text = convertirFormatoFecha(fechaFinal)
        btnSelectDate1.setOnClickListener(this)
        btnSelectDate2.setOnClickListener(this)
        sview.queryHint="Busqueda de pedido"
        sview.setOnQueryTextListener(this)
        sview.setIconified(false)


        identificador=""
        swipePedidos.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipePedidos.setOnRefreshListener(this)
        cargarPedidos()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun convertirFormatoFecha(fecha: Int): String {
        val anio = fecha / 10000
        val mes = fecha % 10000 / 100
        val dia = fecha % 100
        return if (dia < 10) {
            "0" + dia.toString() + "/" + mes.toString() + "/" + anio.toString()
        } else dia.toString() + "/" + mes.toString() + "/" + anio.toString()

    }
    override fun clickPositionOption(position: Int, accion: Byte) {

        var intent= Intent(this,DetallePedido::class.java)
        intent.putExtra("idCabeceraPedido",listaPedido.get(position).idCabecera)
        intent.putExtra("EstadoPagado",listaPedido.get(position).isbEstadoPagado())
        startActivity(intent)

    }
    override fun PedidosResultado(result: List<mCabeceraPedido>?) {
      //  Toast.makeText(this,identificador,Toast.LENGTH_SHORT).show()
        swipePedidos.isRefreshing=false
        listaPedido= ArrayList( result!!.filter { it.identificadorPedido.toUpperCase().startsWith(identificador.toUpperCase())
                || it.cliente.getcName().toUpperCase().startsWith(identificador.toUpperCase())})
        rvAdapterPedidos.Agregar(ArrayList(listaPedido))
    }

    override fun ErrorPedidos() {

    }


    private fun MostrarDialogDatePicker(origen: Byte, year: Int, month: Int, day: Int) {
        val dialogDatePickerSelect = DialogDatePickerSelect().newInstance(origen, year, month, day)
        dialogDatePickerSelect.setFechaListener(this)
        dialogDatePickerSelect.show(this.fragmentManager, "Dialog Fecha")
    }

    override fun getFechaSelecionada(day: Int, month: Int, year: Int, origen: Byte) {
        if (origen.toInt() == 1) {

            this.year = year
            this.month = month
            this.day = day
            fechaInicio = year * 10000 + month * 100 + day
            asyncPedidos.ObtenerTotalPedidoTienda(fechaInicio.toString(),
                    fechaFinal.toString(),identificador)

            btnSelectDate1.text = convertirFormatoFecha(fechaInicio)
        } else if (origen.toInt() == 2) {
            this.year = year
            this.month = month
            this.day = day
            fechaFinal = year * 10000 + month * 100 + day
            btnSelectDate2.text = convertirFormatoFecha(fechaFinal)
            cargarPedidos()
        }
    }
    override fun onClick(v: View?) {
        val a: Int
        val m: Int
        val d: Int
        when (v?.id) {
            R.id.btnSelectDate1 -> {
                origen = 1
                a = fechaInicio / 10000
                m = fechaInicio % 10000 / 100
                d = fechaInicio % 100
                MostrarDialogDatePicker(origen, a, m, d)
            }
            R.id.btnSelectDate2 -> {
                origen = 2
                a = fechaFinal / 10000
                m = fechaFinal % 10000 / 100
                d = fechaFinal % 100
                MostrarDialogDatePicker(origen, a, m, d)
            }

        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
  /*      identificador=query!!
        asyncPedidos.ObtenerTotalPedidoTienda(fechaInicio.toString(),fechaFinal.toString(),identificador)
*/
        listaPedido.clear()
        rvAdapterPedidos.notifyDataSetChanged()
        identificador=query!!
        cargarPedidos()
       return false

    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!!.isEmpty()){
            listaPedido.clear()
            rvAdapterPedidos.notifyDataSetChanged()
            identificador=newText!!
            cargarPedidos()
        }
        return false

    }

    fun cargarPedidos(){
        swipePedidos.isRefreshing=true
        asyncPedidos.ObtenerTotalPedidoTienda(fechaInicio.toString(), fechaFinal.toString(), identificador)

    }

    override fun onRefresh() {
        cargarPedidos()
    }

}
