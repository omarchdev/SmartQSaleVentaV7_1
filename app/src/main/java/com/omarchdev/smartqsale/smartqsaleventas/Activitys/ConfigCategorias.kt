package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_config_categorias.*

class ConfigCategorias : AppCompatActivity(), AsyncCategoria.ListenerCategoria, AsyncCategoria.ListenerResultCambioCategoriaDefecto {

    val asyncCategorias = AsyncCategoria()
    val categorias = ArrayList<mCategoriaProductos>()
    var bUsaCategoria = Constantes.ConfigTienda.bCategoriaDefecto
    var idCategoria = -1

    val SplashTimer:Long=5000
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
         val inflater = menuInflater
         inflater.inflate(R.menu.menu_config_categoria, menu)
         return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ic_guardar) {

            asyncCategorias.ModificaCategoriaDefecto(idCategoria, bUsaCategoria)
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_categorias)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Configuracion de categorías"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar!!.elevation = 4f
        asyncCategorias.setListenerCategoria(this)
        asyncCategorias.getCategorias()

        rgCategorias.orientation = RadioGroup.VERTICAL
        categorias.add(mCategoriaProductos(-1, "Todos los productos"))
        categorias.add(mCategoriaProductos(0, "Favoritos"))
        swCategoriaDefecto.isChecked = bUsaCategoria
        CambioEstado(bUsaCategoria)
        asyncCategorias.setListenerResultCambioCategoriaDefecto(this)
        swCategoriaDefecto.setOnCheckedChangeListener { buttonView, isChecked ->
            rgCategorias.isEnabled = !isChecked
            CambioEstado(isChecked)
            bUsaCategoria = isChecked

        }

        rgCategorias.setOnCheckedChangeListener { group, checkedId ->

            var rb = group.findViewById<RadioButton>(checkedId)
            txtCatDefecto.text = "Categoria seleccionada por defecto: " + rb.text
            idCategoria = rb.id


        }
    }

    fun CambioEstado(estado: Boolean) {
        if (estado) {
            CatActivado()
        } else {
            CatDesactivado()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
    fun CatActivado() {
        rgCategorias.visibility = View.VISIBLE
        txtAvisoCat.visibility = View.GONE
    }

    fun CatDesactivado() {
        rgCategorias.visibility = View.GONE
        txtAvisoCat.visibility = View.VISIBLE
    }

    override fun ObtenerUnidadesMedidad(listaUnidades: MutableList<mUnidadMedida>?) {

    }

    override fun CategoriasObtenidas(categoriaProductosList: MutableList<mCategoriaProductos>?) {
        try {

            categorias.addAll(categoriaProductosList!!)
            categorias?.forEachIndexed { i, t ->
                val rb = RadioButton(this)
                rb.setText("${t.descripcionCategoria}")
                rb.id = t.idCategoria
                rb.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT + 140)

                rgCategorias.addView(rb)
                if (Constantes.ConfigTienda.bCategoriaDefecto) {
                    if (Constantes.ConfigTienda.idCategoriaDefecto == t.idCategoria) {
                        rb.isChecked = true
                        txtCatDefecto.text = "Categoria seleccionada por defecto: ${t.descripcionCategoria}"

                    }
                }
            }

        } catch (e: Exception) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()

        }

    }

    override fun ErrorActualizar(mensajeResultado: String?) {
        AlertDialog.Builder(this).setTitle("Advertencia").setMessage(mensajeResultado).setPositiveButton("Salir", null).create().show()
    }

    override fun ActualizoCategoriaDefecto(mensajeResultado: String?) {
        AlertDialog.Builder(this).setTitle("Confirmación")
                .setMessage(mensajeResultado).setPositiveButton("Salir", null).create().show()

        Handler().postDelayed({
            val intent = Intent(this, Main3Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            startActivity(intent)
        },SplashTimer)
    }
}