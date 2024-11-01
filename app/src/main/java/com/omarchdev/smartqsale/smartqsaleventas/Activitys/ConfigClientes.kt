package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DFmRegistroCliente
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigClientes : ActivityParent(), DFmRegistroCliente.ListenerNombreCliente {



    lateinit var dialogConfigCliente:DFmRegistroCliente
    internal var itemEliminar: MenuItem?=null
    internal var itemGuardar: MenuItem?=null
    var accion=0
    var idCliente=0
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_congif_cliente, menu)
        itemEliminar = menu.findItem(R.id.action_eliminar)
        itemGuardar = menu.findItem(R.id.action_guardar)
        itemEliminar?.isVisible=false
        itemGuardar?.isVisible=false


        itemGuardar?.isVisible = accion==Constantes.EstadoConfiguracion.Nuevo  || accion==Constantes.EstadoConfiguracion.Editar
       Log.e("accion",accion.toString())
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (    id == R.id.action_guardar) {
            if(accion==Constantes.EstadoConfiguracion.Nuevo  || accion==Constantes.EstadoConfiguracion.Editar) {
                dialogConfigCliente?.GuardarCliente()
            }

        } else if (id == R.id.action_eliminar) {

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_clientes)
        idCliente=intent.getIntExtra("IdCliente",0)
        accion=intent.getIntExtra("accionConfig", Constantes.EstadoConfiguracion.Nuevo)
        dialogConfigCliente=DFmRegistroCliente().newInstance(idCliente)
        try {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            val fml = supportFragmentManager
            val ftl = fml.beginTransaction()
            dialogConfigCliente?.listenerNombreCliente=this
            ftl.replace(R.id.content, dialogConfigCliente)
            ftl.commit()


        }catch (e:Exception){
            e.toString()
        }

    }
    override fun ObtenerNombreCliente(nombre: String) {

        supportActionBar!!.title = Html.fromHtml("$nombre")


        itemGuardar?.isVisible=true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
}
