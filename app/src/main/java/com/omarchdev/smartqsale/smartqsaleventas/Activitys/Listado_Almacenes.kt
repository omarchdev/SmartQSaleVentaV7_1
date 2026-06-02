package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterAlmacenes

class Listado_Almacenes : ActivityParent(), RvAdapterAlmacenes.ListenerClickItem, View.OnClickListener {

    lateinit var rvListadoAlm: RecyclerView
    lateinit var fbAlmacen: FloatingActionButton
    lateinit var pbAlmacen: ProgressBar
    lateinit var txtMensaje: TextView

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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        rvListadoAlm = findViewById(R.id.rvListadoAlm)
        fbAlmacen = findViewById(R.id.fbAlmacen)
        pbAlmacen = findViewById(R.id.pbAlmacen)
        txtMensaje = findViewById(R.id.txtMensaje)

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
