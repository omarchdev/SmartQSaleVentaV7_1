package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.ReporteVentasVendedor
import com.omarchdev.smartqsale.smartqsaleventas.R

class VentasPorVendedor : ActivityParent() {


    val reporteVentasVendedor=ReporteVentasVendedor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        setContentView(R.layout.activity_ventas_por_vendedor)
        try {
            val fml = supportFragmentManager
            val ftl = fml.beginTransaction()
            ftl.replace(R.id.content_frame, reporteVentasVendedor)
            ftl.commit()
        }catch (e:Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
