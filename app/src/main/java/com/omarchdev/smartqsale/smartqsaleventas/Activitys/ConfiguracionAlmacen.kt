package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.SelectTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.activity_configuracion_almacen.*

class ConfiguracionAlmacen : ActivityParent(), SelectTienda.TiendaInterface {
    override fun TiendaPorDefecto() {

    }

    override fun TiendaSeleccionada(id: Int) {
        idTienda=id
    }

    override fun SeCargoTiendas() {
    }

    var idTienda=0
    lateinit var selectTienda:SelectTienda
    var permitir=false
    val almacen= mAlmacen()
    val asyncAlmacenes=AsyncAlmacenes()
    var estadoConfig=0
    val context=this
    private lateinit var menuSaveItem: MenuItem
    private lateinit var menuEliminar: MenuItem

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_configuracion_almacen,menu)
        menuSaveItem=menu!!.findItem(R.id.action_guardar)
        menuEliminar=menu!!.findItem(R.id.action_eliminar)

        if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
            menuSaveItem.setVisible(false)
            menuEliminar.setVisible(false)
        }

        if(estadoConfig==Constantes.EstadoConfiguracion.Nuevo){
            menuEliminar.isVisible=false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item!!.itemId==R.id.action_guardar){
            try {
                GuardarAlmacen()
            }catch (e:Exception){
            }
        }
        else if(item!!.itemId==R.id.action_eliminar){
            MensajeEliminarAlmacen()
        }

        return super.onOptionsItemSelected(item)
    }


    fun MensajeEliminarAlmacen(){

        AlertDialog.Builder(this).setTitle("Advertencia")
                .setMessage("¿Desea eliminar el almacén?")
                .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                    asyncAlmacenes.EliminarAlmacen(almacen.idAlmacen,almacen.idTienda)

                    val controladorProcesoCargar=ControladorProcesoCargar(context)
                    controladorProcesoCargar.IniciarDialogCarga("Eliminando almacén")
                    asyncAlmacenes.setListenerEliminarAlmacen(object:AsyncAlmacenes.ListenerEliminarAlmacen{
                        override fun ErrorEliminar() {
                            controladorProcesoCargar.FinalizarDialogCarga()
                            MensajeAlerta("Error","Error al eliminar el almacén.Verifique su conexión a internet")

                        }
                        override fun ExitoEliminar() {
                            MensajeAlerta("Confirmación","El almacén fue eliminado")
                            menuEliminar.isVisible=false
                            menuSaveItem.isVisible=false
                            controladorProcesoCargar.FinalizarDialogCarga()
                        }


                        override fun EliminarIncompleto() {
                            MensajeAlerta("Error","Error al eliminar el almacén.Verifique su conexión a internet")
                            controladorProcesoCargar.FinalizarDialogCarga()
                        }

                        override fun ExisteStockAlmacen() {
                            MensajeAlerta("Advertencia","El almacén tiene productos con stock.No se eliminará el almacén")
                            controladorProcesoCargar.FinalizarDialogCarga()
                        }

                    })
                })
                .setNegativeButton("Cancelar",null)
                .create().show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion_almacen)
        estadoConfig=intent.getIntExtra("estadoConfig",0)
        almacen.idAlmacen=intent.getIntExtra("idAlmacen",0)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        selectTienda= SelectTienda().newInstance(this,"Seleccione una tienda ")
        val fml = supportFragmentManager
        val ftl = fml.beginTransaction()
        ftl.replace(R.id.content_tienda,selectTienda)
        ftl.commit()
        ModificarPantalla()
        ObtenerAlmacen()

    }

    fun ObtenerAlmacen(){
        if(almacen.idAlmacen>0) {
            asyncAlmacenes.ObtenerAlmacenId(almacen.idAlmacen)
            val controladorProcesoCargar = ControladorProcesoCargar(context)
            controladorProcesoCargar.IniciarDialogCarga("Obteniendo datos del almacén")
            asyncAlmacenes.setObtenerInfoAlmacen(object : AsyncAlmacenes.ObtenerInfoAlmacen {
                override fun InfoAlmacen(almacen: mAlmacen?) {
                    controladorProcesoCargar.FinalizarDialogCarga()
                    edtNombreAlmacen.editText?.setText(almacen?.descripcionAlmacen)
                    cbAlmacenP.isChecked = almacen!!.isTienda
                    selectTienda.seleccionarTienda(almacen.idTienda)
                    this@ConfiguracionAlmacen.almacen.idAlmacen=almacen.idAlmacen
                    this@ConfiguracionAlmacen.almacen.idTienda=almacen.idTienda
                }

                override fun AlmacenNoDisponible() {
                    controladorProcesoCargar.FinalizarDialogCarga()
                    finish()
                    Toast.makeText(context, "El almacén no se encuentra disponible.Este fue ELIMINADO", Toast.LENGTH_LONG).show()
                }

                override fun ErrorObtenerInfoAlmacen() {
                    controladorProcesoCargar.FinalizarDialogCarga()
                    finish()
                    Toast.makeText(context, "Error al obtener los datos del almacén verifique su conexión a inernet.", Toast.LENGTH_LONG).show()

                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    fun ModificarPantalla(){
        if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
            edtNombreAlmacen.isEnabled=false
            cbAlmacenP.isEnabled=false
            supportActionBar?.setTitle( "Almacén")
        }
        else if(estadoConfig==Constantes.EstadoConfiguracion.Editar){
            supportActionBar?.setTitle( "Editar almacén" )
        }
        else if(estadoConfig==Constantes.EstadoConfiguracion.Nuevo){
            supportActionBar?.setTitle("Nuevo almacén" )
        }
    }
    fun GuardarAlmacen(){
        permitir=true
        if(edtNombreAlmacen.editText?.text.toString().length<3){
            permitir=false
            edtNombreAlmacen.error="El nombre debe tener 4 carácteres como mínimo"
        }
        else {
            edtNombreAlmacen.error=null
        }
        if(idTienda==0){
            permitir=false
            txtSeleccionTienda.text="Asigne una tienda al almacén \n (Obligatorio)"
            txtSeleccionTienda.setTextColor(Color.RED)
        }
        if(permitir){
            val controladorProcesoCargar=ControladorProcesoCargar(this)
            almacen.idTienda=idTienda
            almacen.descripcionAlmacen=edtNombreAlmacen.editText?.text.toString()
            almacen.isTienda=cbAlmacenP.isChecked
            if(almacen.idAlmacen==0) {
                controladorProcesoCargar.IniciarDialogCarga("Guardando nuevo almacén")
                asyncAlmacenes.GuardarAlmacen(almacen)
            }else if(almacen.idAlmacen>0){
                controladorProcesoCargar.IniciarDialogCarga("Editando almacén")
                asyncAlmacenes.EditarAlmacen(almacen)
            }else{
            }
            asyncAlmacenes.setConfiguracionAlmacen(object:AsyncAlmacenes.ConfiguracionAlmacen{
                override fun FinProceso() {
                    controladorProcesoCargar.FinalizarDialogCarga()
                }
                override fun ExitoGuardarAlmacen() {
                    MensajeAlerta("Confirmación","El almacén se guardo con éxito")
                    menuSaveItem.isVisible=false
                    menuEliminar.isVisible=false
                }
                override fun ExitoEditarAlmacen() {
                    MensajeAlerta("Confirmación","El almacén se editó con éxito")
                    menuSaveItem.isVisible=false
                    menuEliminar.isVisible=false
                }
                override fun ErrorGuardarAlmacen() {
                    MensajeAlerta("Advertencia","El almacén no se logro guardar.Verifique su conexión a internet.")
                }
                override fun ErrorEditarAlmacen() {
                    MensajeAlerta("Advertencia","El almacén no se logro editar.Verifique su conexión a internet.")
                }
                override fun ExisteAlmacenPrincipal() {
                    if(almacen.idAlmacen==0) {
                        MensajeAlerta("Advertencia", "Ya existe un almacén principal.No se logró guardar el almacén")
                    }else if(almacen.idAlmacen>0){
                        MensajeAlerta("Advertencia", "Ya existe un almacén principal.No se logró editar el almacén")
                    }
                }
                override fun ExisteStockComprometido() {
                    MensajeAlerta("Advertencia","El almacén tiene stock comprometido.No se logro editar el almacén.")
                }
                override fun AlmacenNoDisponible() {
                     MensajeAlerta("Advertencia","El almacén se encuentra eliminado")
                }
            })
        }

    }
    fun MensajeAlerta(titulo:String,mensaje:String){
        AlertDialog.Builder(this).setMessage(mensaje)
                .setTitle(titulo).setPositiveButton("Salir",null)
                .create().show()
    }
}
