package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAreasProduccion
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterAreasProduccion
import kotlinx.android.synthetic.main.activity_areas_produccion_lista.*


class AreasProduccionLista : ActivityParent(),AsyncAreasProduccion.ListenerAreasProduccion,ClickListener{


    var areasProduccion=ArrayList<mAreaProduccion>()

    val asyncAreasProduccion=AsyncAreasProduccion()
    var adapter: RvAdapterAreasProduccion?=null


    override fun clickPositionOption(position: Int, accion: Byte) {

        ActivityRegistroArea(areasProduccion.get(position).idArea,Constantes.EstadoConfiguracion.Visualizar)

    }

    override fun ResultadosBusquedaArea(result: MutableList<mAreaProduccion>) {
        areasProduccion.clear()
        areasProduccion.addAll(result)
        adapter?.notifyDataSetChanged()
    }

    override fun ErrorConsultaAreas() {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areas_produccion_lista)
        adapter= RvAdapterAreasProduccion(areasProduccion)
        asyncAreasProduccion.listenerAreasProduccion=this
        adapter?.clickListener=this
        rvAreasProduccion.adapter=adapter
        rvAreasProduccion.layoutManager= LinearLayoutManager(this@AreasProduccionLista)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle( "Areas de Atención")
        supportActionBar?.setSubtitle("Producción/Despacho")
        fbAreaProduccion.setOnClickListener {
                ActivityRegistroArea(0,Constantes.  EstadoConfiguracion.Nuevo)
        }
    }

    override fun onResume() {
        super.onResume()
        asyncAreasProduccion.ObtenerAreasProduccionListado()
    }

    fun ActivityRegistroArea(id:Int, accion:Int){
        val intent= Intent(this@AreasProduccionLista,RegistroAreaProduccion::class.java)
        intent.putExtra("idArea",id)
        intent.putExtra("accion",accion)
        startActivity(intent)
    }
}
