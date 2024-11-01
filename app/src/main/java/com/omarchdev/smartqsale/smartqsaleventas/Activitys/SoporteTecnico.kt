package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVersion
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultas
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ExternalApps
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_soporte_tecnico.*

class SoporteTecnico : ActivityParent(), HttpConsultas.ListenerResultadoConsultaNum {

    val httpConsultas=HttpConsultas()
    val externalApps=ExternalApps(this@SoporteTecnico)
    val asyncVersion=AsyncVersion(this@SoporteTecnico)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soporte_tecnico)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
         supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

        supportActionBar?.setTitle("Soporte técnico")
        btnReiniciarApp.visibility= View.VISIBLE
        btnCall.visibility=View.INVISIBLE
        pb.visibility=View.VISIBLE
        btnCallNumber.visibility=View.INVISIBLE
        btnReiniciarApp.setOnClickListener {
            activityPin()
        }
        btnCallNumber.setOnClickListener{
            externalApps.LlamarServicioTecnico()
        }
        btnCall.setOnClickListener {
            externalApps.AperturaWhatsapp()
        }

        btnActualizar.setOnClickListener {

            asyncVersion.BuscarActualizacion(packageManager.getPackageInfo(packageName,0).packageName)
        }
        txtVersion.setText("Version "+packageManager.getPackageInfo(packageName,0).versionName)
        if(Constantes.Contacto.numContacto==null) {
            httpConsultas.
                    ConsultaServicio()
            httpConsultas.listenerResultadoConsultaNum = this
        }else{
            btnCallNumber.visibility=View.VISIBLE
            pb.visibility=View.INVISIBLE
            btnCall.visibility=View.VISIBLE
            btnReiniciarApp.visibility=View.VISIBLE
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
    fun activityPin(){
        val ia=Intent(this,PinResetApp::class.java)
        startActivity(ia)
    }

    override fun ResultadoNumero(num: String) {
        txtMensaje.setText("Contactar al número $num")
        Constantes.Contacto.numContacto=num
        btnCallNumber.visibility=View.VISIBLE
        pb.visibility=View.INVISIBLE
        btnCall.visibility=View.VISIBLE
        btnReiniciarApp.visibility=View.VISIBLE
    }

    override fun ErrorConsulta(mensaje: String) {
        btnCallNumber.visibility=View.INVISIBLE
        pb.visibility=View.INVISIBLE
        btnCall.visibility=View.INVISIBLE
        btnReiniciarApp.visibility=View.VISIBLE
    }

}
