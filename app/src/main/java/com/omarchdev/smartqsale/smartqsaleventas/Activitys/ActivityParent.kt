package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.javiersantos.bottomdialogs.BottomDialog
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ConnectionValid
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.MessageBreakConnection
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.MessageSlowConnect
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.conexion_perdida
import com.omarchdev.smartqsale.smartqsaleventas.Model.MetricWindow
import com.omarchdev.smartqsale.smartqsaleventas.Network.NetworkUtil
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


open class ActivityParent(): AppCompatActivity(), ConnectionValid.ConnectionStatus {

    private lateinit var metricWindow: MetricWindow
    val connectionChange="android.net.conn.CONNECTIVITY_CHANGE"
    val wifiChange="android.net.wifi.WIFI_STATE_CHANGED"
    var mostrarConexion=false
    var mostrarRecuperaCon=false
    var conexionActiva=false
    var tempNamerConnection=""
    var orientationScreen=0
    var dialogConexionPedida:conexion_perdida?=null
    val activityReferences = 0
    val isActivityChangingConfigurations = false
    private val connectionValid=ConnectionValid()
    var verificaConexionBd=true
    var showNotificationOrder=true
    var messageSlowConnect: MessageSlowConnect?=null
    var messageBreakConnection:MessageBreakConnection?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        this.metricWindow = MetricWindow(this)

        this.window.setSoftInputMode(WindowManager.LayoutParams.WRAP_CONTENT)
        val orientation=resources.configuration.orientation
        var d=metricWindow.height()
        when(orientation){
            ORIENTATION_PORTRAIT->
                if (metricWindow.width()<= 650.0f) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    orientationScreen=0
                }else{
                    //HABILITAR LUEGO

                   requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    //    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    orientationScreen=0
                }
            ORIENTATION_LANDSCAPE->
                if (metricWindow.height()<= 650.0f) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    orientationScreen=0
                }else{
                    //    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                    //HABILITAR LUEGO
                   requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    orientationScreen=0
                }
        }


        if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.P){

            val broadcast=object: BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    if(p0!=null){
                        verificarConexion(p0)
                    }
                }
            }
            val networkUtil= NetworkUtil(this)
            conexionActiva=networkUtil.statusConnection()
            BdConnectionSql.getSinglentonInstance().nombreRed=networkUtil.nameConnection()

            val filter = IntentFilter(connectionChange)
            this.registerReceiver(broadcast,filter)
        }

        connectionValid.connectionStatus=this
        messageSlowConnect=MessageSlowConnect()
        messageBreakConnection=MessageBreakConnection()

        super.onCreate(savedInstanceState)



    }
    private fun VerificaConexion(){
        if(verificaConexionBd){
           connectionValid.ValidConnectionTaks()
        }
    }



    override fun onRestart() {
        super.onRestart()
        Log.e("MARCA->","REINICIO")
        VerificaConexion()
    }
    fun DialogFallaConexion(){
        dialogConexionPedida= conexion_perdida()
        dialogConexionPedida?.show(this.supportFragmentManager,"")

    }


    fun verificarConexion(p0:Context?){
         GlobalScope.launch  {
            val networkUtil= NetworkUtil(p0)
            networkUtil.VerificarConexion()
            conexionActiva=networkUtil.statusConnection()
             tempNamerConnection=networkUtil.nameConnection()
            if(BdConnectionSql.getSinglentonInstance().nombreRed!=tempNamerConnection){
                BdConnectionSql.getSinglentonInstance().setConnectionVentasFin()
            }
            if(networkUtil.statusConnection()){

               launch(Dispatchers.Main){
                    recuperarConexion()
                }
            }else{

                var c= BdConnectionSql.getSinglentonInstance().connectionVentas
                tempNamerConnection=""
                try{
                    // BdConnectionSql.getSinglentonInstance().setConnectionVentasFin()
                    if(c==null &&  !BdConnectionSql.getSinglentonInstance().nombreRed.isEmpty() &&  tempNamerConnection.isEmpty()){
                       launch(Dispatchers.Main){
                            try{
                                if(p0!=null && mostrarConexion){
                                    DialogFallaConexion()
                                    BdConnectionSql.getSinglentonInstance().nombreRed=""
                                    Log.e("Mostrar-> ","  SI ")
                                }else{

                                    Log.e("Mostrar-> ","  NO ")
                                }
                            }catch (f:Exception){
                                Log.e("Error",f.toString())
                            }
                        }
                    }else{
                        Log.e("Conexion -> ","  NO CERRADA  ")
                    }
                }catch (f:Exception){
                    Log.e("Excepcion conn",f.toString())
                }
            }
        }
    }

    fun ShowNotificationNewOrder(){

        BottomDialog.Builder(this)
                .setTitle("Advertencia")
                .setContent("Mensaje de advertencia")
                .setPositiveText("Ok")
                .setPositiveBackgroundColor(R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                .setCancelable(false)
                .show()

    }

    override fun onResume() {

        super.onResume()

    }


    fun recuperarConexion(){


        //    var progress:Dialog?=null
        //  if(mostrarConexion){
        //     progress=dialogCargaAsync?.getDialogCarga("Recuperando conexión")
        //       progress?.show()
        //       }
        Log.e("Verifica ",BdConnectionSql.getSinglentonInstance().isSeConectoVentas.toString() )
        if(BdConnectionSql.getSinglentonInstance().connectionVentas==null
                && BdConnectionSql.getSinglentonInstance().isSeConectoVentas &&
                tempNamerConnection!=BdConnectionSql.getSinglentonInstance().nombreRed) {
            var dialogCargaAsync:DialogCargaAsync?=DialogCargaAsync(this)
            var progress:Dialog?=null
            if(mostrarConexion){
                dialogConexionPedida?.dismiss()

                progress=dialogCargaAsync?.getDialogCarga("Recuperando conexión")
                progress?.show()

            }
             GlobalScope.launch  {
                try{
                    Log.e(" VERIFICADO  ", "Conectado")
                    BdConnectionSql.getSinglentonInstance().nombreRed = tempNamerConnection
                    BdConnectionSql.getSinglentonInstance().ConectarInstanciaPrincipal()

                }catch (f:Exception){
                    Log.e("ERROR " ,f.toString())
1
                }
               launch(Dispatchers.Main){
                    progress?.dismiss()
                }
            }

        }



    }

    override fun onStart() {

        if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.P) {
            mostrarConexion = true
            if (!conexionActiva) {
                DialogFallaConexion()
            }
        }
        super.onStart()

    }


    override fun onStop() {
        mostrarConexion=false
        super.onStop()
    }

    override fun ConnectionSlow() {

        messageSlowConnect?.show(supportFragmentManager,"")

    }

    override fun ConnectionBreak() {
        messageBreakConnection?.show(supportFragmentManager,"")

    }

}