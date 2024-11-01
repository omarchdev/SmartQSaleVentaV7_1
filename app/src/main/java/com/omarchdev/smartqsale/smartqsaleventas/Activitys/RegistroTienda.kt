package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncTiendas
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTienda
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_registro_tienda.*

class RegistroTienda : ActivityParent(), AsyncTiendas.ListenerGuardarTienda {

    var idTienda=0
    val asyncTiendas=AsyncTiendas()
    var context=this@RegistroTienda
    var permitir=true
    internal var itemEliminar: MenuItem?=null
    internal var itemGuardar: MenuItem?=null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_registro_tienda, menu)
        itemEliminar = menu.findItem(R.id.action_eliminar)
        itemGuardar = menu.findItem(R.id.action_guardar)
        if(idTienda==0){
            itemGuardar?.isVisible=true
        }
        itemEliminar?.isVisible=false
        return super.onCreateOptionsMenu(menu)
    }

    fun GuardarTienda(){
        permitir=true
        var tienda=mTienda()
        tienda.idTienda=idTienda
        tienda.nombreTienda=edtNombreTienda.editText?.text.toString()
        if(tienda.nombreTienda.length<2){
            permitir=false
            edtNombreTienda.error="El nombre es muy corto"
        }


        if(permitir){
            if(idTienda==0){
              asyncTiendas.AgregarTienda(tienda)
            }else{
                asyncTiendas.ActualizarTienda(tienda)
            }
            asyncTiendas.listenerGuardarTienda=this
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_guardar) {
                GuardarTienda()

        } else if (id == R.id.action_eliminar) {

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_tienda)
        asyncTiendas.controladorProcesoCargar= ControladorProcesoCargar(this)
        idTienda=intent.getIntExtra("Id",0)
        supportActionBar?.elevation=1f
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        if(idTienda==0) {
            supportActionBar?.setTitle("Nueva Tienda" )
        }
        else{
            supportActionBar?.setTitle("Editar Tienda")
            asyncTiendas.ObtenerTiendaId(idTienda)
            asyncTiendas.listenerTiendaResult=object:AsyncTiendas.ListenerTiendaResult{
                override fun ResultadoTienda(tienda: mTienda) {
                    if(tienda.cEstado.equals("E")){
                        var a=0
                        a=Constantes.Tiendas.tiendaList.filter{  it ->it.idTienda==tienda.idTienda  }.lastIndex
                        Constantes.Tiendas.tiendaList.removeAt(a)
                        AlertDialog.Builder(context).setMessage("La tienda no se encuentra disponible")
                                .setPositiveButton("Salir",null).setTitle("Advertencia").create().show()
                    }else{
                        edtNombreTienda.editText?.setText("${tienda.nombreTienda}")
                    }
                }

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun ActualizarExito() {


        itemGuardar?.isVisible=false

    }

    override fun ErrorGuardar() {
        AlertDialog.Builder(this).setTitle("Advertencia")
                .setMessage("No se logró crear la tienda con éxito")
                .setPositiveButton("Salir",null).create().show()
        itemGuardar?.isVisible=false
    }

    override fun ExitoGuardar() {
        AlertDialog.Builder(this).setTitle("Confirmación")
                .setMessage("Se creo la tienda con éxito")
                .setPositiveButton("Aceptar",null).create().show()
        itemGuardar?.isVisible=false
    }

}
