package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncSoporte
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.SesionUsuario
import com.omarchdev.smartqsale.smartqsaleventas.R

import kotlinx.android.synthetic.main.activity_pin_reset_app.*
import kotlinx.android.synthetic.main.content_pin_reset_app.*

class PinResetApp : ActivityParent(), View.OnClickListener, AsyncSoporte.RespuestaPin {

    val SplashTimer:Long=2000
    private val maxLongitud: Int = 6
    internal var longitudPin: Int ?=null
    internal var permitirEscribir: Boolean = false
    internal var pin: String?=null
    internal var cont: Int = 0
    internal var helper= DbHelper(this)
    internal val asyncSoporte=AsyncSoporte()
    var sesionUsuario=SesionUsuario()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_reset_app)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle("Reiniciar Aplicación")


        asyncSoporte.respuestaPin=this
        longitudPin = 0
        permitirEscribir = true
        pin=""
        cont=0
        avi.hide()
        btnNumber0.setOnClickListener(this)
        btnNumber1.setOnClickListener(this)
        btnNumber2.setOnClickListener(this)
        btnNumber3.setOnClickListener(this)
        btnNumber4.setOnClickListener(this)
        btnNumber5.setOnClickListener(this)
        btnNumber6.setOnClickListener(this)
        btnNumber7.setOnClickListener(this)
        btnNumber8.setOnClickListener(this)
        btnNumber9.setOnClickListener(this)
        btnNumberDelete.setOnClickListener(this)
        txtTextoPin.visibility = View.INVISIBLE


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
    override fun onClick(v: View?) {
        if (permitirEscribir) {
            if (v?.getId() == R.id.btnNumberDelete) {
                cont = 0
                pin = ""
                circle1.setImageResource(R.drawable.circle_void)
                circle2.setImageResource(R.drawable.circle_void)
                circle3.setImageResource(R.drawable.circle_void)
                circle4.setImageResource(R.drawable.circle_void)
                circle5.setImageResource(R.drawable.circle_void)
                circle6.setImageResource(R.drawable.circle_void)
                txtMensajeConfirmacion.text = "Ingrese el PIN"
            }
            else {
                if (cont < maxLongitud) {
                    cont++
                    cambiarColorCirculo()
                    when (v?.id) {
                        R.id.btnNumber0 -> {
                            pin = pin + "0"
                            AccederPin()
                        }
                        R.id.btnNumber1 -> {
                            pin = pin + "1"
                            AccederPin()
                        }
                        R.id.btnNumber2 -> {
                            pin = pin + "2"
                            AccederPin()
                        }
                        R.id.btnNumber3 -> {
                            pin = pin + "3"
                            AccederPin()
                        }
                        R.id.btnNumber4 -> {
                            pin = pin + "4"
                            AccederPin()
                        }
                        R.id.btnNumber5 -> {
                            pin = pin + "5"
                            AccederPin()
                        }
                        R.id.btnNumber6 -> {
                            pin = pin + "6"
                            AccederPin()
                        }
                        R.id.btnNumber7 -> {
                            pin = pin + "7"
                            AccederPin()
                        }
                        R.id.btnNumber8 -> {
                            pin = pin + "8"
                            AccederPin()
                        }
                        R.id.btnNumber9 -> {
                            pin = pin + "9"
                            AccederPin()
                        }
                    }

                }

            }
        }
    }
    fun cambiarColorCirculo() {


        when(cont){
            1->circle1.setImageResource(R.drawable.circle_full)
            2->circle2.setImageResource(R.drawable.circle_full)
            3->circle3.setImageResource(R.drawable.circle_full)
            4->circle4.setImageResource(R.drawable.circle_full)
            5->circle5.setImageResource(R.drawable.circle_full)
            6->circle6.setImageResource(R.drawable.circle_full)
        }
    }
    fun AccederPin() {
        if (cont == 6) {
            permitirEscribir = false
            if(pin!!.equals("299041")){
                PermitirReinicio()
            }else{
                NoPermitirReinicio()
            }
          //  asyncSoporte.VerificarPinReinicio(pin!!)
           // avi.show()
           // permitirEscribir = false
        }
    }
    override fun PermitirReinicio() {
        txtMensajeConfirmacion.text="Reiniciando la aplicación"
        sesionUsuario.BorrarSesion()
        Handler().postDelayed({
            helper.DeleteUserRegister()
            helper.DeleteAccesoRolAcceso()
            helper.DeletePrint()
            val intent = Intent(this, Main3Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            startActivity(intent)
        },SplashTimer)

    }

    override fun NoPermitirReinicio() {
        avi.hide()
        txtMensajeConfirmacion.text="El PIN de reinicio es incorrecto"
        circle1.setImageResource(R.drawable.circle_full_error)
        circle2.setImageResource(R.drawable.circle_full_error)
        circle3.setImageResource(R.drawable.circle_full_error)
        circle4.setImageResource(R.drawable.circle_full_error)
        circle5.setImageResource(R.drawable.circle_full_error)
        circle6.setImageResource(R.drawable.circle_full_error)
        permitirEscribir = true
    }

    override fun ErrorProcedimient() {
        avi.hide()
        txtMensajeConfirmacion.text="Error al verificar el PIN de reinicio.Verifique su conexión a internet.Codigo AB0099"
    }

    override fun ErrorConexion() {
        avi.hide()
        txtMensajeConfirmacion.text="Error al verificar el PIN de reinicio.Verifique su conexión a internet.Codigo AB0098"

    }


}
