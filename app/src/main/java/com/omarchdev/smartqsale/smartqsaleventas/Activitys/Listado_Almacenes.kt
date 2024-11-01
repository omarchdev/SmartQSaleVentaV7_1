package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterAlmacenes
import kotlinx.android.synthetic.main.activity_listado__almacenes.*

class Listado_Almacenes : ActivityParent(), RvAdapterAlmacenes.ListenerClickItem, View.OnClickListener {

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.fbAlmacen->{
                getClickPosition(0, Constantes.EstadoConfiguracion.Nuevo)
            }
        }
    }

    val rvAdapterAlmacen= RvAdapterAlmacenes(this)
    val asyncAlmacenes=AsyncAlmacenes()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado__almacenes)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle("Almacenes")
        rvListadoAlm.adapter=rvAdapterAlmacen
        rvAdapterAlmacen.setListenerClickItem(this)
        fbAlmacen.setOnClickListener(this)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
    override fun onResume() {
        super.onResume()
        rvAdapterAlmacen.LimpiarLista()
        txtMensaje.visibility= View.INVISIBLE
        pbAlmacen.visibility= View.VISIBLE
        asyncAlmacenes.ObtenerAlmacenes()
        asyncAlmacenes.setListenerAlmacenes(object:AsyncAlmacenes.ListenerAlmacenes{
            override fun ObtenerBusquedaAlmacenes(almacenList: MutableList<mAlmacen>?) {
                rvAdapterAlmacen.AgregarAlmacenes(ArrayList(almacenList!!))
                txtMensaje.visibility= View.INVISIBLE
                pbAlmacen.visibility= View.INVISIBLE
            }
            override fun ErrorConsulta() {
                txtMensaje.visibility= View.VISIBLE
                pbAlmacen.visibility= View.INVISIBLE

            }
        })
    }


    override fun getClickPosition(idAlmacen: Int, metodo: Int) {
        val intent = Intent(this, ConfiguracionAlmacen::class.java)
        intent.putExtra("idAlmacen",idAlmacen)
        intent.putExtra("estadoConfig",metodo)
        startActivity(intent)
    }

}
