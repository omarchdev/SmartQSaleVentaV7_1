package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncRoles
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.ConfiguracionBase
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProcesoRol
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_configuracion_rol.*

class ConfiguracionRol : ActivityParent() {

    val demoFragment= ConfiguracionBase()
    val asyncRoles=AsyncRoles()
    var idRol=0
    var context: Context =this
    var esAdmistrador=false
    private var itemSave:MenuItem?=null


    fun CargarInformacion(listaProceso:ArrayList<mProcesoRol>){
        try {
            demoFragment.useSwitch(true)
            demoFragment.setMargins(top = 24, left = 16, right = 16, bottom = 16)
            listaProceso.forEach {
                demoFragment.AgregarSwitch(it.idProceso,it.nombreProceso,it.isbEstadoAcceso())
            }
            txtMensaje.visibility = View.INVISIBLE
            pb.visibility = View.INVISIBLE
            content_frame.visibility=View.VISIBLE
            demoFragment.GenerarPantalla(this)
           /* if(esAdmistrador){
                demoFragment.HabilitarSwitch(false)
            }*/
        }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ingreso_compra,menu)

        itemSave=menu?.findItem(R.id.actionCheck)

        /*
        if(esAdmistrador){
            itemSave?.isVisible=false
        }
        */
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!!.itemId== R.id.actionCheck){
            MensajeConfirmacionGuardar()/*
            if(!esAdmistrador) {

            }else{
                AlertDialog.Builder(this).setTitle("Advertencia").
                        setMessage("No se encuentra disponible la edición para el rol ADMINISTRADOR")
                        .setPositiveButton("Salir",null).create().show()
            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    fun procesarResultadosSwitch(lista:ArrayList<ConfiguracionBase.EstadoSwitch>){
        val listaProceso=ArrayList<mProcesoRol>()
         lista.forEach {
            var procesoRol=mProcesoRol()
            procesoRol.idProceso=it.idSwitch
            procesoRol.setbEstadoAcceso(it.estado)
            listaProceso.add(procesoRol)
        }
        val controladorProcesoCargar=ControladorProcesoCargar(this)
        asyncRoles.GuardarEstadosProcesoRol(idRol,listaProceso)
        controladorProcesoCargar.IniciarDialogCarga("Guardando cambios")
        asyncRoles.setListenerGuardarProcesos(object:AsyncRoles.ListenerGuardarProcesos{
            override fun FinGuardar() {
                controladorProcesoCargar.FinalizarDialogCarga()
            }

            override fun ExitoGuardar() {
                AlertDialog.Builder(context).setTitle("Confirmacion")
                        .setMessage("Los cambios se guardaron con éxito")
                        .setPositiveButton("Salir",null).create().show()
            }

            override fun ErrorConnection() {

                AlertDialog.Builder(context).setTitle("Error")
                        .setMessage("Error al guardar los cambios.Verifique su conexión")
                        .setPositiveButton("Salir",null).create().show()
            }

            override fun ErrorGuardar() {

                AlertDialog.Builder(context).setTitle("Error")
                        .setMessage("Error al guardar los cambios.Verifique su conexión.")
                        .setPositiveButton("Salir",null).create().show()
            }

        })
    }
    fun MensajeConfirmacionGuardar(){
        AlertDialog.Builder(this)
                .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                   procesarResultadosSwitch(demoFragment.EstadosSwitch())
                })
                .setNegativeButton("Cancelar",null)
                .setMessage("¿Desea guardar los cambios realizados?")
                .setTitle("Confirmación")
                .create().show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_rol)
        val fm1 = supportFragmentManager
        val ft1 = fm1.beginTransaction()
        ft1.replace(R.id.content_frame, demoFragment)
        ft1.commit()
        content_frame.visibility=View.INVISIBLE
        txtMensaje.visibility= View.VISIBLE
        pb.visibility=View.VISIBLE
        idRol=intent.getIntExtra("idRol",0)
        esAdmistrador=intent.getBooleanExtra("esAdmistrador",true)
        supportActionBar?.elevation=2f
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        supportActionBar?.setTitle(Html.fromHtml(
                "<font color=\"#757575\">" + intent.getStringExtra("nombreRol")+
                        "</font>"))
        supportActionBar?.setSubtitle(Html.fromHtml(
                "<font color=\"#757575\">" + "Configuración Rol"+
                        "</font>"))
        verificarIdRol()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    fun verificarIdRol() {

        if(idRol>0) {
            txtMensaje.text="Cargando datos"
            asyncRoles.ObtenerProcesosRol(idRol)
            asyncRoles.setListenerConfigProcesoRol(object:AsyncRoles.ListenerConfigProcesoRol{
                override fun ProcesosRolObtenidos(procesoRols: MutableList<mProcesoRol>?) {
                    CargarInformacion(ArrayList(procesoRols!!))
                }
                override fun ErrorObtenerProcesos() {
                    finish()
                    Toast.makeText(context,"Error al conseguir la información.Verifique su conexión.",Toast.LENGTH_LONG).show()
                }
            })
        }else{
            finish()
        }
    }
}
