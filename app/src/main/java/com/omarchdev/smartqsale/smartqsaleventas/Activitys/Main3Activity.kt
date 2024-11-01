package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncLogUser
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVersion
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.InfoSesionTemp
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.SesionUsuario
import com.omarchdev.smartqsale.smartqsaleventas.Model.HorarioTerminal
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : ActivityParent(), AsyncLogUser.IStarConnection, AsyncLogUser.ResultLogPrincipal, AsyncLogUser.ResultLogPin {

    val SplashTimer:Long=0
    val asyncLogUser=AsyncLogUser()
    lateinit var dbHelper: DbHelper
    var s: SesionUsuario?=null
    var p: InfoSesionTemp?=null
    var asyncVersion:AsyncVersion?=null
    var usuario: mUsuario? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        verificaConexionBd=false
         supportActionBar?.hide()
        pb1.visibility=View.GONE
        asyncVersion= AsyncVersion(this)
        txtM.visibility=View.GONE
        dbHelper= DbHelper(this)
        Constantes.HorariosTerminal.list.clear()
        Constantes.HorariosTerminal.list.add(HorarioTerminal("NUNCA",0))
        Constantes.HorariosTerminal.list.add(HorarioTerminal("1 HORA",1))
        Constantes.HorariosTerminal.list.add(HorarioTerminal("12 HORAS",12))
        Constantes.HorariosTerminal.list.add(HorarioTerminal("24 HORAS",24))
        Constantes.PATH.DIRFILE=getFilesDir().toString()

        asyncLogUser.iStarConnection=this
        asyncLogUser.StartSERVICEAPP(packageManager.getPackageInfo(packageName,0).versionCode
                ,packageManager.getPackageInfo(packageName,0).packageName)

        /*
        val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "10409103842-03-B001-8.pdf")
        if(f.exists()){
            Toast.makeText(this,"Existe",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"No Existe",Toast.LENGTH_SHORT).show()

        }*/
    }



    override fun IngresarApp() {
        usuario = dbHelper.SelectUsuario()
        if (usuario!!.email != "" && usuario!!.contrasena != "") {
            pb1.visibility=View.VISIBLE
            txtM.visibility=View.VISIBLE
            asyncLogUser.resultLogPrincipal = this
            asyncLogUser.LogEmpresa(usuario!!.email, usuario!!.contrasena)
        }else{
            iniciarApp()
        }
    }

    override fun Actualizar(link: String, name: String, extend: String,mensaje:String) {

        AlertDialog.Builder(this).setTitle("Advertencia").setMessage(mensaje)
                .setPositiveButton("Actualizar",DialogInterface.OnClickListener { dialogInterface, i ->
                    asyncVersion?.BuscarActualizacion(packageManager.getPackageInfo(packageName,0).packageName)
        }).setNegativeButton("Cancelar",null).create().show()

    }


    fun iniciarApp(){

        Handler().postDelayed({
            startActivity(Intent(this,LoginPrincipal12::class.java))
            finish()
        },SplashTimer)
    }

    override fun ErrorConexion(mensaje:String) {
  //      AlertaMensajeErrorApp("Error al ingresar.Verifique su conexión a internet","Error")
        AlertDialog.Builder(this).setTitle("Mensaje").setMessage(mensaje).
                setPositiveButton("Salir",null).create().show()

    }

    override fun LogExito() {
       try {
            s = SesionUsuario()
            p = s!!.ObtenerInfoSesionTemp()
            if (p!!.permitirAcceso) {
                asyncLogUser.LogPin("0000",Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
                        , Build.BRAND, Build.MODEL, Build.VERSION.RELEASE, p!!.user, p!!.permitirAcceso)
                asyncLogUser.resultLogPin = this

            } else {

                val intent = Intent(this, PinLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    override fun LogError(mensaje: String) {

        AlertDialog.Builder(this).setMessage(mensaje).setPositiveButton("Salir",null).create().show()

    }
    override fun PinLogExito() {
        s!!.ReiniciarConteoUsuario()
        if (Constantes.Usuario.esAdministrador) {
            val intent = Intent(this, SelectTienda::class.java)
            startActivity(intent)
            finish()
        } else {
            //  permitirEscribir=true;
            val intent = Intent(this, PantallaPrincipal::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun PinLogExitoAccesoDirecto() {
        val intent = Intent(this, PantallaPrincipal::class.java)
        startActivity(intent)
        finish()
    }

    override fun PinLogError(mensaje: String) {
    }



}
