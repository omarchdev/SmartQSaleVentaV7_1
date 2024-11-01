package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncRoles
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRol
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterRoles
import kotlinx.android.synthetic.main.activity_listado_roles.*

class ListadoRoles : ActivityParent(), RvAdapterRoles.InterfaceListaRoles {




    override fun ObtenerPosicion(position: Int) {
        val intent= Intent(this, ConfiguracionRol::class.java)
        intent.putExtra("idRol",listadoRoles.get(position).idRol)
        intent.putExtra("nombreRol",listadoRoles.get(position).getcDescripcion())
        intent.putExtra("esAdmistrador",listadoRoles.get(position).isbEsAdmistrador())
        startActivity(intent)
    }

    var listadoRoles=ArrayList<mRol>()
    val asyncRoles=AsyncRoles()
    val adapterRoles=RvAdapterRoles()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_roles)
        rvRoles.adapter=adapterRoles
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle( "Roles del sistema")
        adapterRoles.setInterfaceListaRoles(this)
        pbRoles.visibility= View.VISIBLE
        txtMensaje.visibility=View.INVISIBLE
        asyncRoles.ObtenerRoles()
        asyncRoles.setListenerRoles(object:AsyncRoles.ListenerRoles{
            override fun RolesObtenidos(mRolList: MutableList<mRol>?) {
                pbRoles.visibility= View.INVISIBLE
                listadoRoles=ArrayList(mRolList!!)
                adapterRoles.agregarElementos(listadoRoles)
            }
            override fun ErrorObtenerRoles() {
                pbRoles.visibility= View.INVISIBLE
                txtMensaje.visibility=View.VISIBLE
                txtMensaje.text="Error al conseguir los roles del sistema.Verifique su conexión a internet"
            }
        })

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onResume() {
        super.onResume()

    }
}
