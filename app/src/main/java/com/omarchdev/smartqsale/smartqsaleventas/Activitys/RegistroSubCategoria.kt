    package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncSubCategorias
import com.omarchdev.smartqsale.smartqsaleventas.ClickListener
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAgregarSubCategoria
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSubCategoria
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterSubCategorias
import kotlinx.android.synthetic.main.activity_registro_sub_categoria.*


class RegistroSubCategoria : ActivityParent(),DialogAgregarSubCategoria.ListenerSubCategoriaAgregar,AsyncSubCategorias.ResultadoSubCategorias,AsyncSubCategorias.ListenerConfigSubCategoria,ClickListener {
    override fun ResultadoEliminarExito(mSubCategoria: mSubCategoria) {

    }

    override fun ErrorEliminar() {
    }


    var listaSubCat=ArrayList<mSubCategoria>()
    val adapter= RvAdapterSubCategorias(listaSubCat)
    val asyncSubCategorias=AsyncSubCategorias()
    val ft=this@RegistroSubCategoria.supportFragmentManager
    var idCategoria=0
    var descripcionCategoria=""
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sub_categoria,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun DialogAddSubCategoria() {

        val dialogFragment=DialogAgregarSubCategoria().newInstance(0,"")
        dialogFragment.show(ft,"")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.item_add_subCategoria->{
                DialogAddSubCategoria()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_sub_categoria)
        txtMensajeAlerta.visibility= View.GONE
        idCategoria=intent.getIntExtra("idCategoria",0)
        descripcionCategoria= intent.getStringExtra("nombre")!!
        asyncSubCategorias.listenerConfigSubCategoria=this
        asyncSubCategorias.resultadoSubCategorias=this
        asyncSubCategorias.ObtenerSubCategorias(idCategoria)
        adapter.clickListener=this
        rvSubCategoria.adapter=adapter
        rvSubCategoria.layoutManager=LinearLayoutManager(this@RegistroSubCategoria)
        supportActionBar?.elevation=4f
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle("$descripcionCategoria")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun ObtenerDescripcionSubCategoria(descripcion: String) {
        asyncSubCategorias.AgregarSubCategoria(idCategoria,descripcion)
    }

    override fun ErrorBusqueda() {

        txtMensajeAlerta.visibility= View.VISIBLE
        txtMensajeAlerta.text="Error al consultar las subcategorias.Verifique su conexión a internet o reinicie su aplicación"

    }

    override fun ResultadoBusqueda(listaSubCategoria: MutableList<mSubCategoria>?) {
        txtMensajeAlerta.visibility= View.GONE
        this.listaSubCat.addAll(ArrayList(listaSubCategoria!!))
        adapter.notifyDataSetChanged()
        if(listaSubCategoria?.size==0){
            txtMensajeAlerta.visibility=View.VISIBLE
            txtMensajeAlerta.text="No tiene agregada subcategorias.Para agregar presione '+' en la parte superior."
        }

    }

    override fun clickPositionOption(position: Int, accion: Byte) {

        when(accion){

            100.toByte()->Toast.makeText(this@RegistroSubCategoria,"Eliminar",Toast.LENGTH_SHORT).show()

            101.toByte()-> {
                val dialogFragment = DialogAgregarSubCategoria()
                        .newInstance(listaSubCat.get(position).idSubCategoria, listaSubCat.get(position).descripcionSubCategoria)
                dialogFragment.show(ft, "")
            }



        }

    }

    override fun subCategoriaAgregada(subCategoria: mSubCategoria) {
        this.listaSubCat.add(subCategoria)
        adapter.notifyItemInserted(listaSubCat.size-1)
        txtMensajeAlerta.visibility=View.GONE
        MensajeAdvertencia("Confirmación","La subcategoria ${subCategoria.descripcionSubCategoria} se agrego con éxito.")

    }

    fun MensajeAdvertencia(titulo:String,mensaje:String){

        AlertDialog.Builder(this@RegistroSubCategoria).setTitle(titulo).setMessage(mensaje).setPositiveButton("Salir",null).create().show()

    }

    override fun errorAgregarSubCategoria() {
        MensajeAdvertencia("Advertencia","Error al ingresar una nueva subcategoria.Verifique su conexión a internet y reinicie la aplicación.")


    }

    override fun EditarSubCategoria(descripcion: String, id: Int) {

        var subCategoria=mSubCategoria(id,descripcion)
        subCategoria.idCategoria=idCategoria
        asyncSubCategorias.EditarSubCategoria(subCategoria)

    }
    override fun errorEditar() {
        MensajeAdvertencia("Advertencia","Error al editar subcategoria.Verifique su conexión a internet y reinicie la aplicación.")


    }

    override fun editarSubCategoria(subCategoria: mSubCategoria) {
            listaSubCat.filter { it->it.idSubCategoria==subCategoria.idSubCategoria }.first().descripcionSubCategoria=subCategoria.descripcionSubCategoria
        adapter.notifyDataSetChanged()
        MensajeAdvertencia("Confirmación","La subcategoria ${subCategoria.descripcionSubCategoria} se editó con éxito.")

    }

}

