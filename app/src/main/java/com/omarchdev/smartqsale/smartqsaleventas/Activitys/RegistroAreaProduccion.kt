
package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAreasProduccion
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_registro_area_produccion.*

class RegistroAreaProduccion : ActivityParent(),AsyncAreasProduccion.ListenerObtenerAreaProduccion, AsyncAreasProduccion.ListenerGuardarAreaProduccion {


    val area=mAreaProduccion()
    var Estado=0
    val asyncAreasProduccion=AsyncAreasProduccion()
    var itemSave:MenuItem?=null
    var itemEliminar:MenuItem?=null
    var itemEditar:MenuItem?=null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_editar_guardar,menu)

        itemSave=menu?.findItem(R.id.action_guardar)
        itemEditar=menu?.findItem(R.id.action_editar)
        itemEliminar=menu?.findItem(R.id.action_eliminar)

        if(Estado==Constantes.EstadoConfiguracion.Nuevo){
            itemSave?.isVisible=true
            itemEditar?.isVisible=false
            itemEliminar?.isVisible=false
        }else if(Estado==Constantes.EstadoConfiguracion.Editar){
            itemSave?.isVisible=true
            itemEditar?.isVisible=false
            itemEliminar?.isVisible=true
        }
        /*
        if(esAdmistrador){
            itemSave?.isVisible=false
        }
        */
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            R.id.action_guardar->{
                if(Estado==Constantes.EstadoConfiguracion.Nuevo) {
                    area.cDescripcionArea = edtDescripcion.editText?.text.toString().trim()
                    asyncAreasProduccion.GuardarAreaProduccion(area)
                }else if(Estado==Constantes.EstadoConfiguracion.Editar) {
                    area.cDescripcionArea = edtDescripcion.editText?.text.toString().trim()
                    asyncAreasProduccion.EditarAreaProduccion(area)
                }
            }
            R.id.action_eliminar->{
                asyncAreasProduccion.EliminarAreaProduccion(area)
            }
            R.id.action_editar->{
               Estado=Constantes.EstadoConfiguracion.Editar
                itemEliminar?.isVisible=true
                itemEditar?.isVisible=false
                itemSave?.isVisible=true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_area_produccion)
        Estado=intent.getIntExtra("accion",Constantes.EstadoConfiguracion.Visualizar)
        asyncAreasProduccion.listenerObtenerAreaProduccion=this
        asyncAreasProduccion.listenerGuardarAreaProduccion=this
        if(Estado==Constantes.EstadoConfiguracion.Nuevo){
            Nuevo()
        }else if(Estado==Constantes.EstadoConfiguracion.Visualizar){
            Existente()
            asyncAreasProduccion.getAreaPorId(intent.getIntExtra("idArea",0))
            Estado=Constantes.EstadoConfiguracion.Editar
        }
        supportActionBar?.elevation=2f
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun Nuevo() {
        edtDescripcion.editText?.isEnabled=true
        supportActionBar?.setTitle("Registro de Area")
          }

    fun Existente(){
        edtDescripcion?.isEnabled=true
        supportActionBar?.setTitle("Editar Area")
    }

    override fun ResultadoObtenerArea(area: mAreaProduccion) {
        this.area.cDescripcionArea=area.cDescripcionArea
        this.area.idArea=area.idArea
        edtDescripcion.editText!!.setText(area.cDescripcionArea)
    }

    override fun ErrorConsultaArea() {

    }

    fun Guardar(){

    }

    override fun GuardarExito() {
        itemEliminar?.isVisible=false
        itemEditar?.isVisible=false
        itemSave?.isVisible=false
        if(area.idArea==0){
            Mensaje("El área de producción/Despacho ${edtDescripcion.editText?.text.toString()} se creó con éxito","Confirmacion")
        }else{
            Mensaje("El área de producción/Despacho ${edtDescripcion.editText?.text.toString()} se Módifico con éxito","Confirmación")
        }
    }
    override fun ErrorGuardar() {
        if(area.idArea==0){
            Mensaje("Error al guardar el área de producción/despacho","Advertencia")
        }else{
            Mensaje("Error al editar el área de producción/despacho","Advertencia")
        }
        itemEliminar?.isVisible=false
        itemEditar?.isVisible=false
        itemSave?.isVisible=false
    }

    fun Mensaje(Mensaje:String,Titulo:String) {
        AlertDialog.Builder(this).setMessage(Mensaje).setOnDismissListener { finish() }.setTitle(Titulo).setPositiveButton("Aceptar",null).create().show()
    }

}
