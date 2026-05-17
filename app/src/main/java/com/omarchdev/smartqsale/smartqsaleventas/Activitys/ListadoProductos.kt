package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto.ObtenerProductos
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam.ScannerResult
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.SnackBarsPer
import com.omarchdev.smartqsale.smartqsaleventas.InterfaceDetalleCarritoVenta
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapter
import java.util.Locale

class ListadoProductos : ActivityParent(), SearchView.OnQueryTextListener, ScannerResult,
    ObtenerProductos, View.OnClickListener, InterfaceDetalleCarritoVenta {
    val metodoTodos: Byte = 100
    val metodoBusqueda: Byte = 103
    var metodoRealizar: Byte = 0
    var snackBarsPer: SnackBarsPer? = null
    var searchView: SearchView? = null
    var dialogScannerCam: DialogScannerCam? = null
    var searchBox: EditText? = null
    var recyclerView: RecyclerView? = null
    var helper: DbHelper? = null
    var adapterProductos: RvAdapter? = null
    var asyncProducto: AsyncProducto? = null
    var pbProductos: ProgressBar? = null
    var context: Context? = null
    var txtMensaje: TextView? = null
    var descripcion: String? = null
    override fun getMenuInflater(): MenuInflater {
        return super.getMenuInflater()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_listado_productos, menu)

        // Hide the search menu item as we now use an inline SearchView
        val searchItem = menu.findItem(R.id.searchToolbar1)
        if (searchItem != null) {
            searchItem.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.searchToolbar1 -> {}
            R.id.actionScanCode -> SolicitarPermiso()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun SolicitarPermiso() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
            Toast.makeText(this, "Debe activar la camara para usar el scanner", Toast.LENGTH_LONG)
                .show()
        } else {
            Scan()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Si el permiso está concedido prosigue con el flujo normal
                Scan()
            } else {
                //Si el permiso no fue concedido no se puede continuar
                Toast.makeText(this, "Debe permitir a la camara usar la opcion", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descripcion = ""
        //VerificarPedidoEnProceso();
        helper = DbHelper(this)
        setContentView(R.layout.activity_listado_productos)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
                supportActionBar!!.title = "Listado productos"
            }
            toolbar.setNavigationOnClickListener { onBackPressed() }

            // Initialize Inline SearchView
            searchView = findViewById(R.id.searchViewInline)
            searchView?.setOnQueryTextListener(this)

            // Get access to the inner EditText of the SearchView for scanner results
            val searchTextId = searchView?.context?.resources?.getIdentifier("android:id/search_src_text", null, null)
            if (searchTextId != null && searchTextId != 0) {
                searchBox = searchView?.findViewById(searchTextId)
            }
        }

        dialogScannerCam = DialogScannerCam()
        dialogScannerCam!!.setScannerResult(this)
        NUEVO_PRODUCTO = this.resources.getInteger(R.integer.NuevoProducto)
        PRODUCTO_EXISTENTE = resources.getInteger(R.integer.ProductoExistente)
        ESTADO = resources.getString(R.string.EstadoExistenciaProducto)
        CODE = resources.getString(R.string.CodigoProducto)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(this)
        txtMensaje = findViewById(R.id.txtMensaje)
        pbProductos = findViewById(R.id.pbProductos)
        recyclerView = findViewById(R.id.rvProductos)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        adapterProductos = RvAdapter(this, 1)
        asyncProducto = AsyncProducto(this)
        asyncProducto!!.setListenerObtenerProductos(this)
        context = this
        recyclerView!!.setAdapter(adapterProductos)
        snackBarsPer = SnackBarsPer(findViewById(R.id.fab), this)
        snackBarsPer!!.setDuration(Snackbar.LENGTH_LONG)
        snackBarsPer!!.setColor(resources.getColor(R.color.colorAccent))
        pbProductos!!.setVisibility(View.GONE)
        recyclerView!!.setVisibility(View.GONE)
        adapterProductos!!.setInterfaceDetalleVenta(this)
    }

    override fun onResume() {
        super.onResume()
        metodoRealizar = metodoTodos
        asyncProducto!!.ObtenerProductosLista(descripcion)
        pbProductos!!.visibility = View.VISIBLE
        recyclerView!!.visibility = View.GONE
        txtMensaje!!.visibility = View.INVISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        pbProductos!!.visibility = View.VISIBLE
        recyclerView!!.visibility = View.GONE
        txtMensaje!!.visibility = View.INVISIBLE
        descripcion = s
        asyncProducto!!.ObtenerProductosLista(descripcion)
        return false
    }

    override fun onQueryTextChange(s: String): Boolean {
        if (s.length == 0) {
            pbProductos!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
            txtMensaje!!.visibility = View.INVISIBLE
            metodoRealizar = metodoTodos
            descripcion = ""
            asyncProducto!!.ObtenerProductosLista(descripcion)
        }
        return false
    }

    fun Scan() {
        dialogScannerCam?.show(supportFragmentManager, "Scanner")
    }

    override fun ResultadoScanner(resultText: String) {
        searchView?.isIconified = false
        searchView?.setQuery(resultText, true)
    }

    override fun ObtenerListaProductos(mProductList: List<mProduct>) {
        pbProductos!!.visibility = View.GONE
        if (mProductList != null) {
            if (mProductList.size == 0) {
                if (metodoRealizar == metodoBusqueda) {
                    txtMensaje!!.visibility = View.VISIBLE
                    txtMensaje!!.text =
                        "No existen resultados en la busqueda".uppercase(Locale.getDefault())
                } else if (metodoRealizar == metodoTodos) {
                    txtMensaje!!.visibility = View.VISIBLE
                    txtMensaje!!.text =
                        "No tienes productos registrados.Para agregar presione el botón '+'".uppercase(
                            Locale.getDefault()
                        )
                }
            }
            adapterProductos!!.AddProduct(mProductList)
            recyclerView!!.visibility = View.VISIBLE
        } else {
            txtMensaje!!.visibility = View.VISIBLE
            txtMensaje!!.text =
                "Error al descargar la informacion.Verifique su conexion".uppercase(Locale.getDefault())
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fab) {
            if (!helper!!.ObtenerPermiso(Constantes.ProcesosPantalla.AgregarNuevoProducto))
            {
                Toast.makeText(this, "No tiene permiso para realizar esta accion", Toast.LENGTH_SHORT).show()
                return;
            }
            val intentInventario = Intent(this, Registro_Producto::class.java)
            intentInventario.putExtra(ESTADO, NUEVO_PRODUCTO)
            startActivity(intentInventario)
        }
    }

    fun ObtenerProducto(id: Int) {
        val intentInventario = Intent(this, Registro_Producto::class.java)
        intentInventario.putExtra(ESTADO, PRODUCTO_EXISTENTE)
        intentInventario.putExtra(CODE, id)
        startActivity(intentInventario)
    }

    override fun CantidadProductosEnCarrito(cantidad: Int) {}
    override fun InformacionUltimoProducto(nombre: String, precio: String) {}
    override fun PasarInformacionProductoaDetalleVenta(id: Int) {
        ObtenerProducto(id)
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
        private var NUEVO_PRODUCTO = 0
        private var PRODUCTO_EXISTENTE = 0
        private var ESTADO: String? = null
        private var CODE: String? = null
    }
}