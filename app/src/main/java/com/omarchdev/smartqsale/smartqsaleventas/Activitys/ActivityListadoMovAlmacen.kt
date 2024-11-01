package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_listado_mov_almacen.*

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTransaccionAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterMovAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterTiposTransaccion
import com.sothree.slidinguppanel.SlidingUpPanelLayout

import java.util.*
import kotlin.collections.ArrayList


class ActivityListadoMovAlmacen : ActivityParent(), AsyncAlmacenes.ListenerMovAlmacen {


    val adapterTipoTransaccion= RvAdapterTiposTransaccion()
    val adapterMovAlmacen= RvAdapterMovAlmacen()
    val asyncMovAlmacen=AsyncAlmacenes()
    var fechaInicio=""
    var fechaFinal=""
    var dia:Int=0
    var mes:Int=0
    var anio:Int=0
    var origen:Byte=0
    val calendar:Calendar= Calendar.getInstance()
    var fInicioText=""
    var fFinalText=""
    val selectFecha: DialogDatePickerSelect = DialogDatePickerSelect()
    val listMovAlmacen= mutableListOf<mMovAlmacen>()
    val context: ActivityListadoMovAlmacen =this
    val controladorProcesoCargar=ControladorProcesoCargar(this@ActivityListadoMovAlmacen)
    var pos=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_mov_almacen)
        try {
            adapterMovAlmacen.setContexto(this)
            rvListadoMovAlm.layoutManager = LinearLayoutManager(this)
            rvListadoMovAlm.adapter = adapterMovAlmacen
            listenerButtonFiltro()
            GenerarFechaOrigen()
            GetFecha()
            txtMensajeAlerta.visibility=View.GONE
            pb.isIndeterminate=true
            pb.visibility=View.VISIBLE
            rvTransaccion.layoutManager=LinearLayoutManager(this)
            rvTransaccion.adapter=adapterTipoTransaccion
            txtTitulo.visibility=View.GONE
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            supportActionBar?.setTitle("Movimientos almacén")
            adapterMovAlmacen.setListenerClickItem(listenerPositionMov)
            asyncMovAlmacen.ObtenerTipoTransaccionesAlmacen()
            asyncMovAlmacen.setListenerTipoTransacciones(object:AsyncAlmacenes.ListenerTipoTransacciones{
                override fun TransaccionResultado(transaccionAlmacenList: MutableList<mTransaccionAlmacen>?) {
                    Toast.makeText(context,transaccionAlmacenList!!.size.toString(),Toast.LENGTH_SHORT).show()
                    adapterTipoTransaccion.AgregarTransacciones(ArrayList(transaccionAlmacenList))
                    txtTitulo.visibility=View.VISIBLE
                    pb.visibility=View.GONE
                }
               override fun ErrorConsulta() {
                    pb.visibility=View.GONE
                   txtTitulo.visibility=View.GONE
                   txtMensajeAlerta.visibility=View.VISIBLE

               }
                override fun ErrorConexion() {
                    pb.visibility=View.GONE
                    txtTitulo.visibility=View.GONE
                    txtMensajeAlerta.text="Error al conseguir los tipos de transacciones.Verifique su conexion a internet"
                    txtMensajeAlerta.visibility=View.VISIBLE
                }

            })
            fbtn.setOnClickListener {
                fbtn.show()
                slidingPanel.panelState=SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
        listenerPanelSelect()
        listenerPanelState()
     }

        fun listenerPanelSelect(){
        adapterTipoTransaccion.setListenerTransaccion(object:RvAdapterTiposTransaccion.ListenerTransaccion{
            override fun ObtenerCodigoTransaccion(codigo: String) {
                slidingPanel.panelState=SlidingUpPanelLayout.PanelState.COLLAPSED
                activityTransaccionAlmacen(codigo)
            }

        })
    }

    override fun onBackPressed() {
        if(slidingPanel.panelState==SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingPanel.panelState=SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun visible(){
        fbtn.show()
    }
    fun ocultar(){
        fbtn.hide()
    }
    fun listenerPanelState(){
        slidingPanel.addPanelSlideListener(object:SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {

            }
             override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {

                if(newState==SlidingUpPanelLayout.PanelState.COLLAPSED){
                    visible()
                }else if(newState==SlidingUpPanelLayout.PanelState.EXPANDED){

                    ocultar()
                }

            }

        })

    }

    val listenerPositionMov=object:RvAdapterMovAlmacen.ListenerClickItem{
        override fun getClickPosition(position: Int, metodo: Byte) {

            if(metodo==100.toByte()) {
                when (listMovAlmacen.get(position).getcEstadoRegistro()) {

                    "P" -> {
                        MensajeAlerta("Advertencia","No se puede modificar el movimiento seleccionado.Este ya fue procesado.")
                    }
                    "A" -> {
                        editarMov(listMovAlmacen.get(position).idMovAlmacen)
                    }

                }
            }else if(metodo==101.toByte()){
                pos=position
                when (listMovAlmacen.get(position).getcEstadoRegistro()) {
                    "P" -> {
                        MensajeAlerta("Advertencia","No se puede anular el movimiento seleccionado.Este ya fue procesado.")
                    }
                    "A" -> {
                        Toast.makeText(this@ActivityListadoMovAlmacen,listMovAlmacen.get(position).idMovAlmacen.toString(),Toast.LENGTH_LONG).show()
                        controladorProcesoCargar.IniciarDialogCarga("Anulando movimiento de almacén")
                          asyncMovAlmacen.AnularMovimientoAlmacen(listMovAlmacen.get(position).idMovAlmacen)
                       asyncMovAlmacen.setIAnularMovAlmacen(object:AsyncAlmacenes.IAnularMovAlmacen{
                            override fun MovEliminado() {
                                controladorProcesoCargar.FinalizarDialogCarga()
                                listMovAlmacen.removeAt(pos)
                                adapterMovAlmacen.EliminarMovAlmacen(pos)
                            }

                            override fun EliminarMovError() {

                                controladorProcesoCargar.FinalizarDialogCarga()
                            }

                        })
                    }

                }
            }else if(metodo==102.toByte()){
                visualizarMov("",listMovAlmacen.get(position).idMovAlmacen)
            }
         }

    }
    private fun editarMov(idMov:Int){
        var intent=Intent(this,IngresoCompraKt::class.java)
        intent.putExtra("idMov",idMov)
        intent.putExtra("tipoAccion",resources.getInteger(R.integer.EditarMovAlmacen))
        intent.putExtra("tipoMov","")
        intent.putExtra("estadoEdt",Constantes.EstadoEditar.Editable)
        startActivity(intent)
    }

    private fun TextoFecha(titulo:String, dia:Int, mes:Int, anio:Int):String= "$titulo \n $dia/$mes/$anio"

    private fun GenerarFechaTexto(dia:Int, mes:Int, anio:Int):String="$anio-$mes-$dia"


    private fun GenerarFechaOrigen(){

        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaFinal.setText(TextoFecha(resources.getString(R.string.FechaFinal),dia,mes,anio))
        fFinalText=GenerarFechaTexto(dia,mes,anio)

        calendar.add(Calendar.DAY_OF_MONTH,-1)
        dia=calendar.get(Calendar.DAY_OF_MONTH)
        mes=calendar.get(Calendar.MONTH)+1
        anio=calendar.get(Calendar.YEAR)
        btnFechaInicio.setText(TextoFecha(resources.getString(R.string.FechaInicial),dia,mes,anio))
        fInicioText=GenerarFechaTexto(dia,mes,anio)

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
             asyncMovAlmacen.ObtenerMovimientosAlmacen(fInicioText,fFinalText)
        }
    }

    private fun MostrarSeleccionarFecha(origen:Byte){
        selectFecha.setOrigen(origen,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
        selectFecha.show(fragmentManager,"Fecha")
    }
    private fun listenerButtonFiltro() {

        btnFechaInicio.setOnClickListener{
            MostrarSeleccionarFecha(1)
        }
        btnFechaFinal.setOnClickListener{
            MostrarSeleccionarFecha(2)
        }

    }

    override fun onResume() {
        super.onResume()
        listMovAlmacen.clear()
        adapterMovAlmacen.AgregarMovimientos(ArrayList(listMovAlmacen))

        asyncMovAlmacen.ObtenerMovimientosAlmacen(fInicioText,fFinalText)
        asyncMovAlmacen.setListenerMovAlmacen(this)
        pbMov.visibility=View.VISIBLE
    }

    private fun activityTransaccionAlmacen(tipoMov:String){
        var intent= Intent(this,IngresoCompraKt::class.java)
        intent.putExtra("idMov",0)
        intent.putExtra("tipoAccion",resources.getInteger(R.integer.NuevoMovAlmacen))
        intent.putExtra("tipoMov",tipoMov)
        intent.putExtra("estadoEdt",Constantes.EstadoEditar.Editable)
        startActivity(intent)
    }



    private fun visualizarMov(tipoMov: String,idMov:Int){

        var intent=Intent(this,IngresoCompraKt::class.java)
        intent.putExtra("idMov",idMov)
        intent.putExtra("tipoAccion",resources.getInteger(R.integer.EditarMovAlmacen))
        intent.putExtra("tipoMov","")
        intent.putExtra("estadoEdt",Constantes.EstadoEditar.noEditable)
        startActivity(intent)
  }

    override fun MovimientosRealizados(movAlmacenList: MutableList<mMovAlmacen>?) {
            pbMov.visibility=View.GONE
            listMovAlmacen.clear()
            listMovAlmacen.addAll(movAlmacenList!!)
            adapterMovAlmacen.AgregarMovimientos(ArrayList(listMovAlmacen))

    }

    override fun ErrorObtenerMovimientos() {
    }



    private fun MensajeAlerta(titulo:String,mensaje:String){
        val alertDialog= AlertDialog.Builder(context)
        alertDialog.setTitle(titulo).
                setMessage(mensaje).
                setPositiveButton("Salir",null).
                create().show()

    }
}
