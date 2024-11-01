package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncImpresora
import com.omarchdev.smartqsale.smartqsaleventas.Model.mImpresora
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterImpresora

import kotlinx.android.synthetic.main.activity_lista_impresoras_areas.*
import kotlinx.android.synthetic.main.content_lista_impresoras_areas.*

class ListaImpresorasAreas : ActivityParent(), AsyncImpresora.ListenerResultadosImpresoras, RvAdapterImpresora.ClickPosition {

    override fun positionClick(position: Int) {

        var intent=Intent(this,RegistroImpresora::class.java)
        intent.putExtra("idImpresora",listaImpresoras.get(position).idImpresora)
        startActivity(intent)
    }


    val listaImpresoras=ArrayList<mImpresora>()
    var rvAdapterImpresora: RvAdapterImpresora?=null
    val asyncImpresora=AsyncImpresora(this@ListaImpresorasAreas)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_lista_impresoras_areas)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title ="Listado de impresoras"
        txtMensaje.visibility= View.VISIBLE

        rvAdapterImpresora= RvAdapterImpresora(listaImpresoras)
        rvAdapterImpresora?.clickPosition=this
        asyncImpresora.listenerResultadosImpresoras=this
        rv_impresoras.adapter=rvAdapterImpresora
        rv_impresoras.layoutManager=LinearLayoutManager(this)
        fab.setOnClickListener { view ->
            var intent= Intent(this,RegistroImpresora::class.java)
            startActivity(intent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun onResume() {
        super.onResume()
        asyncImpresora.ObtenerImpresoras()


    }
    override fun ErrorEncotrarImpresoras() {

    }

    override fun ImpresorasObtenidas(impresoras: ArrayList<mImpresora>) {

        txtMensaje.visibility= View.GONE
        listaImpresoras.clear()
        listaImpresoras.addAll(impresoras)
        rvAdapterImpresora?.notifyDataSetChanged()
    }
}
