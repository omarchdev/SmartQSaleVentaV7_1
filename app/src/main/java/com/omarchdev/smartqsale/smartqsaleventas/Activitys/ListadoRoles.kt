package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncRoles
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRol
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterRoles
import kotlinx.android.synthetic.main.activity_listado_roles.*

class ListadoRoles : ActivityParent(), RvAdapterRoles.InterfaceListaRoles {


    var idPosTemp:Int=-1

    override fun ObtenerPosicion(position: Int) {
        idPosTemp=position

        val intent= Intent(this, PinProcesApp::class.java)
        resultLauncher.launch(intent)




    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 3. Aquí recibimos la respuesta
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val respuesta = data?.getStringExtra("claveResult")

            val intent= Intent(this, ConfiguracionRol::class.java)
            intent.putExtra("idRol",listadoRoles.get(idPosTemp).idRol)
            intent.putExtra("nombreRol",listadoRoles.get(idPosTemp).getcDescripcion())
            intent.putExtra("esAdmistrador",listadoRoles.get(idPosTemp).isbEsAdmistrador())
            startActivity(intent)
            // Haz algo con el resultado
            //Toast.makeText(this, "Recibido: $respuesta", Toast.LENGTH_SHORT).show()
        }
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
