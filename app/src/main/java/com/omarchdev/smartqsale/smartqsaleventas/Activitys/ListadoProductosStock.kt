package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.app.FragmentManager
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncStockProductos
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfStockAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterStockProductos
import kotlinx.android.synthetic.main.activity_listado_productos_stock.*

class ListadoProductosStock : ActivityParent(), AsyncStockProductos.ListenerListadoProductoStock, SearchView.OnQueryTextListener, ClickListener {


    val fm: FragmentManager = this@ListadoProductosStock.fragmentManager
    val dialogScan: DialogScannerCam = DialogScannerCam()
    var a: DfStockAlmacenes? = null
    val asyncStockProductos = AsyncStockProductos()
    val productos = ArrayList<mProduct>()
    var rvAdapterStockProductos: RvAdapterStockProductos? = null
    private lateinit var menuSearch: MenuItem
    private lateinit var menuScan: MenuItem
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_listado_productos, menu)
        
        // Hide the old search item
        val searchItem = menu.findItem(R.id.searchToolbar1)
        if (searchItem != null) {
            searchItem.setVisible(false)
        }
        
        menuScan = menu!!.findItem(R.id.actionScanCode)
        
        dialogScan.setScannerResult {
            val searchViewInline = findViewById<SearchView>(R.id.searchViewInline)
            searchViewInline?.setQuery(it, true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun Scannear() {
        dialogScan.show(supportFragmentManager, "ScanBarCode")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item!!.itemId == R.id.actionCheck) {

        } else if (item!!.itemId == R.id.actionScanCode) {
            Scannear()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_productos_stock)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
                supportActionBar!!.title = "Stock de Productos"
            }
            toolbar.setNavigationOnClickListener { onBackPressed() }

            // Inicializar SearchView Inline
            val searchViewInline = findViewById<SearchView>(R.id.searchViewInline)
            searchViewInline?.setOnQueryTextListener(this)
        }

        asyncStockProductos.listenerListadoProductoStock = this
        rvAdapterStockProductos = RvAdapterStockProductos(productos)
        rvAdapterStockProductos?.clickListener = this
        rvProductosStock.adapter = rvAdapterStockProductos
        rvProductosStock.layoutManager = LinearLayoutManager(this)

    }

    override fun ListadoResultado(lista: ArrayList<mProduct>) {
        productos.clear()
        productos.addAll(lista)
        rvAdapterStockProductos?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        asyncStockProductos.ObtenerProductosStock()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.length > 0) {
            asyncStockProductos.ObtenerProductosConTexto(newText)
        } else {
            asyncStockProductos.ObtenerProductosStock()
        }
        return false
    }

    override fun clickPositionOption(position: Int, accion: Byte) {
        DfStockAlmacenes().newInstance(productos.get(position)
                .idProduct, productos.get(position)
                .descripcionCategoria + " " + productos.get(position).getcProductName() + " " + productos.get(position).descripcionVariante).show(supportFragmentManager, "")

    }
}
