package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAreasProduccion
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncImpresora
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.cImpresionRed
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.SelectTienda
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.SpnAreasProduccion
import com.omarchdev.smartqsale.smartqsaleventas.Model.mImpresora
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_registro_impresora.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class RegistroImpresora : ActivityParent(), SelectTienda.TiendaInterface, SpnAreasProduccion.AreasInterface, AsyncImpresora.ListenerImpresora {

    var idTienda=0
     var idArea=0
    var idImpresora=0
    lateinit var selectTienda: SelectTienda
    lateinit var selectArea: SpnAreasProduccion
   var asyncImpresora=AsyncImpresora(this@RegistroImpresora)
    var asyncAreasProduccion=AsyncAreasProduccion()
    val cImpresora=cImpresionRed()
    var controladorProcesoCargar=ControladorProcesoCargar(this@RegistroImpresora)
    var mensaje=""
    var permitir=true
    internal var itemEliminar: MenuItem?=null
    internal var itemGuardar: MenuItem?=null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_impresora, menu)
        itemEliminar = menu.findItem(R.id.action_eliminar)
        itemGuardar = menu.findItem(R.id.save)
        if(idImpresora==0){
            itemEliminar?.setVisible(false)
        }else{
            itemEliminar?.setVisible(true)

        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.save) {
           GuardarImpresora()
        } else if (id == R.id.action_eliminar) {
            EliminarImpresora()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_impresora)
        selectTienda= SelectTienda().newInstance(this,"Seleccione una tienda ")
        selectArea=SpnAreasProduccion().newInstance(this,"Seleccione un área")
        asyncImpresora.listenerImpresora=this
        idImpresora=intent.getIntExtra("idImpresora",0)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation = 6f

        if(idImpresora==0) {
            supportActionBar!!.title ="Registrar Impresora"
        }else{
            supportActionBar!!.title = "Editar"
        }

        val fml = supportFragmentManager
        val ftl = fml.beginTransaction()
        ftl.replace(R.id.content_SpnTiendas,selectTienda)
        ftl.commit()


        val fm2 = supportFragmentManager
        val ft2 = fm2.beginTransaction()
        ft2.replace(R.id.content_SpnAreas,selectArea)
        ft2.commit()

        btnProbarImpresora.setOnClickListener {

            if(!edtIp.editText?.text.toString().equals("") &&
                    !edtPuerto.editText?.text.toString().equals("")){
                controladorProcesoCargar.IniciarDialogCarga("Buscado impresora")

                 GlobalScope.launch  {
                   var v=cImpresora.ProbarImpresora(edtIp.editText?.text.toString()
                            ,edtPuerto.editText?.text.toString().toInt())
                    when(v){
                         true->mensaje="Se encontró la impresora"
                        false->mensaje="No se encontró la impresora"

                    }
                   launch(Dispatchers.Main){
                        controladorProcesoCargar.FinalizarDialogCarga()
                        AlertDialog.Builder(this@RegistroImpresora).
                                setMessage(mensaje).setTitle("Confirmación")
                                .setPositiveButton("Salir",null).create().show()
                    }

                }
            }

        }

    }

    override fun TiendaPorDefecto() {

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun TiendaSeleccionada(id: Int) {

        idTienda=id
    }

    override fun SeCargoTiendas() {
    }

    fun GuardarImpresora(){
        permitir=true
        if(idTienda==0){
            permitir=false
        }
        if(idArea==0){
            permitir=false
        }
        if(edtIp.editText?.text.toString().equals("")){
            permitir=false
            edtIp.error="Debe ingresar un número de IP"
        }
        if(edtPuerto.editText?.text.toString().equals("")){
            permitir=false
            edtPuerto.error="Debe ingresar el número de Puerto"
        }


        if(permitir){
            var impresora=mImpresora()
            impresora.idImpresora=idImpresora
            impresora.nombreImpresora=edtDescripcion.editText?.text.toString()
            impresora.IP=edtIp.editText?.text.toString()
            impresora.puerto=edtPuerto.editText?.text.toString().toInt()
            impresora.idArea=idArea
            impresora.idTienda=idTienda
            if(idImpresora==0){
                asyncImpresora.GuardarNuevaImpresora(impresora)
            }
            else{
                asyncImpresora.EditarImpresora(impresora)
            }
        }

    }



    override fun AreasPorDefecto() {
    }

    override fun AreaSeleccionada(id: Int) {
     //   Toast.makeText(this,id.toString(),Toast.LENGTH_SHORT).show()

        idArea=id
    }

    override fun SeCargoAreas() {
        if(idImpresora!=0) {
            asyncImpresora.ObtenerImpresora(idImpresora)
        }
    }

    override fun ExitoGuardar() {
        AlertDialog.Builder(this).setTitle("Confirmación").
                setMessage("Se guardó la impresora con éxito").
                setPositiveButton("Salir",null).create().show()
        itemGuardar?.isVisible=false
        itemEliminar?.isVisible=false

    }

    override fun ErrorGuardar() {
        AlertDialog.Builder(this)
                .setMessage("Existe un problema al registrar la impresora.Verifique su conexión a internet")
                .setTitle("Advertencia").setPositiveButton("Salir",null).create().show()
        itemGuardar?.setVisible(false)
        itemEliminar?.setVisible(false)

    }

    override fun ExitoEliminar() {
        AlertDialog.Builder(this)
                .setMessage("Se el elimino la impresora con exito")
                .setTitle("Confirmacion").setPositiveButton("Salir", DialogInterface.OnClickListener { dialog, which ->
                    this@RegistroImpresora.finish()
                }).create().show()
        itemGuardar?.setVisible(false)
        itemEliminar?.setVisible(false)

    }

    override fun ErrorEliminar() {
        AlertDialog.Builder(this)
                .setMessage("Existe un problema al eliminar la impresora.Verifique su conexión a internet")
                .setTitle("Advertencia").setPositiveButton("Salir",null).create().show()

    }

    override fun ObtenerImpresora(impresora: mImpresora) {

        idImpresora=impresora.idImpresora
        edtDescripcion.editText?.setText(impresora.nombreImpresora)
        edtIp.editText?.setText(impresora.IP)
        edtPuerto.editText?.setText(impresora.puerto.toString().trim())
        selectTienda.seleccionarTienda(impresora.idTienda)
        selectArea.seleccionarArea(impresora.idArea)
    }

    override fun ErrorObtenerImpresora() {
        finish()
        Toast.makeText(this,
                "Existen problemas al conseguir la información " +
                        "de la impresora.Verifique su conexión",Toast.LENGTH_LONG).show()
    }



    fun EliminarImpresora(){
        AlertDialog.Builder(this)
                .setMessage("¿Está seguro de eliminar la impresora?")
                .setTitle("Advertencia").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
                    asyncImpresora.EliminarImpresora(idImpresora)

                }).setNegativeButton("No",null).create().show()

        var file= File("")
        var pdfPage = file
    }
}
