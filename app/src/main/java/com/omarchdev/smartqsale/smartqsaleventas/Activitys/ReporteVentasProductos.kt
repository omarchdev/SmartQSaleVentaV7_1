package com.omarchdev.smartqsale.smartqsaleventas.Activitys


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporteProductos
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfChartProductoVenta
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfResumenVentasProducto
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct

import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_reporte_ventas_productos.*
import kotlinx.android.synthetic.main.fragment_reporte_ventas_productos.view.*

class ReporteVentasProductos : ActivityParent(), DfChartProductoVenta.ListenerChangedSelection {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */



    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    val asyncReporteProducto=AsyncReporteProductos()
    var fragment:Fragment?=null
    var fragmentChar=DfChartProductoVenta()
    var fragmentResumen=DfResumenVentasProducto()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_ventas_productos)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fragmentChar.listenerChangedSelection=this


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_reporte_ventas_productos, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            when(position){
               0->{
                  return fragmentChar
                }
                1->{
                    return fragmentResumen
                }

            }
            return fragment!!
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }
    }

    override fun VentasPorMonto(fechaInicio:String,fechaFinal:String) {

        asyncReporteProducto.ObtenerReporteVentasProductoMonto(fechaInicio,fechaFinal,object:AsyncReporteProductos.ListenerReporteVentasProducto{
            override fun ResultadoVentasProductos(listadoProductos: List<mProduct>) {
                if(listadoProductos.size<=10){
                    fragmentChar.AgregarProductosLista( listadoProductos)
                }
                else{
                    fragmentChar.AgregarProductosLista( listadoProductos.subList(0,10))
                }
                fragmentResumen.AgregarProductosLista(listadoProductos)
            }

            override fun ErrorConsultaProductosVentas() {
            }
        })

    }
    override fun VentasPorUnidades(fechaInicio:String,fechaFinal:String) {
        asyncReporteProducto.ObtenerReporteVentasProductoUnidades(fechaInicio,fechaFinal,object:AsyncReporteProductos.ListenerReporteVentasProducto{
            override fun ResultadoVentasProductos(listadoProductos: List<mProduct>) {
                if(listadoProductos.size<=10){
                    fragmentChar.AgregarProductosLista( listadoProductos)
                }
                else{
                    fragmentChar.AgregarProductosLista( listadoProductos.subList(0,10))
                }
                fragmentResumen.AgregarProductosLista(listadoProductos)
            }
            override fun ErrorConsultaProductosVentas() {

            }
        })

    }

}
