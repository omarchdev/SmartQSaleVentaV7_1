package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterTiendasIngreso

import kotlinx.android.synthetic.main.activity_listado_tiendas.*
import kotlinx.android.synthetic.main.content_listado_tiendas.*

class ListadoTiendas : ActivityParent(), RvAdapterTiendasIngreso.PositionClick1 {

    val rvAdapterTiendas=RvAdapterTiendasIngreso()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_tiendas)
        setSupportActionBar(toolbar )
        rvAdapterTiendas.tipoVista=2
        rvAdapterTiendas.positionClick=this
        rvListTienda.adapter=rvAdapterTiendas
        rvListTienda.layoutManager=LinearLayoutManager(this)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle( "Tiendas")

        fab.setOnClickListener { view ->
            AbrirRegistroTienda(0)
        }
    }

    override fun onResume() {
        super.onResume()

            rvAdapterTiendas.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun ObtenerTienda(id: Int) {

        AbrirRegistroTienda(id)
    }

    fun AbrirRegistroTienda(id:Int){

        var intent= Intent(this,RegistroTienda::class.java)
        intent.putExtra("Id",id)
        startActivity(intent)

    }


}
