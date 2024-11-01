package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCaja
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_ventas_cierre.*
import java.text.SimpleDateFormat
import java.util.*

class ReporteCierreCaja : ActivityParent() {

    internal val c = Calendar.getInstance()
    internal var dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm aa")
    var tipoReporte = 0;
    var permitir = true
    internal var fechaInicio: String=""
    internal var fechaFinal:String=""
    internal val CODE_RESULT_ID_CAJA = 1
    val asyncCaja= AsyncCaja(this@ReporteCierreCaja)
    val controladorProcesoCargar = ControladorProcesoCargar(this)
    var idVendedor = 0
    var idCierre=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_cierre_caja)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.elevation = 4f
        supportActionBar?.setTitle("Ventas por caja" )
        cv_select_cierre.setOnClickListener {
            val intent = Intent(this@ReporteCierreCaja, HistorialCierresCaja::class.java)
            startActivityForResult(intent, CODE_RESULT_ID_CAJA)
        }
        asyncCaja.ObtenerIdCierreCaja()
        asyncCaja.setListenerAperturaCaja(object:AsyncCaja.ListenerAperturaCaja {

            override fun ConfirmacionAperturaCaja() {
                Toast.makeText(this@ReporteCierreCaja,"Error al conseguir la informacion de la caja."
                        , Toast.LENGTH_LONG).show()
            }
            override fun AperturarCaja() {/*
                txtMensaje.visibility=View.INVISIBLE
                pb.visibility= View.GONE
                content.visibility=View.INVISIBLE*/
            }
            override fun ConfirmarCierreCaja() {
                Toast.makeText(this@ReporteCierreCaja,"Error al conseguir la informacion de la caja."
                        , Toast.LENGTH_LONG).show()

            }
            override fun ExisteCajaAbiertaDisponible() {
                Toast.makeText(this@ReporteCierreCaja,"Error al conseguir la informacion de la caja."
                        , Toast.LENGTH_LONG).show()

            }
            override fun CajaCerrada() {
                Toast.makeText(this@ReporteCierreCaja,"Error al conseguir la informacion de la caja."
                        , Toast.LENGTH_LONG).show()

            }
            override fun ExisteCierre(Cierre: mCierre?) {
                OrdenarVentasPorCierre(Cierre!!.idCierre)
                idCierre=Cierre!!.idCierre
            }
            override fun ErrorEncontrarCaja() {
                Toast.makeText(this@ReporteCierreCaja,"Error al conseguir la informacion de la caja."
                        , Toast.LENGTH_LONG).show()

            }
        })
    }
    fun OrdenarVentasPorCierre(cierre:Int){
        /*
        asyncReporte.ObtenerCabeceraCierre(cierre!!)
        asyncReporte.setListenerVentasPorCierre(this)*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_RESULT_ID_CAJA) {
            if (resultCode == RESULT_CANCELED) {
            } else {
                idCierre=data!!.getIntExtra("idCierre", 0)
                OrdenarVentasPorCierre(idCierre)
               // OcultarInterfaz()
            }
        }
    }


    fun ModificarDatosCabecera(cierre: mCierre) {

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
    }

}
