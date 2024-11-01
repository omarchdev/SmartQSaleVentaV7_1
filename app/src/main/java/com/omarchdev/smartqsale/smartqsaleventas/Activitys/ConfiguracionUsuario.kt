
package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncUsers
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRol
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.SelectTienda
import kotlinx.android.synthetic.main.activity_configuracion_usuario.*

class ConfiguracionUsuario : ActivityParent(), SelectTienda.TiendaInterface {
    override fun TiendaPorDefecto() {

    }

    override fun TiendaSeleccionada(id: Int) {
        idTienda=id
        if(id==0){
            permitirTienda=false
        }else{
            permitirTienda=true
        }


    }

    override fun SeCargoTiendas() {

    }
    private var idTienda=0
   private var permitirTienda=false
    val asyncUser=AsyncUsers()
    val context: Context =this
    val listaDescripciones=ArrayList<String>()
    val usuario= mUsuario()
    val validarPin={valor:String->!valor.equals("") && valor.length==1}
    var permitir=false
    val listaRoles=ArrayList<mRol>()
    private lateinit var adapter:ArrayAdapter<String>
    var estadoConfiguracion=0
    var cargarRolTerminar=false
    var idUsuario=0
    val labelNuevo="Guardar Usuario"
    val mensajeNuevo="¿Desea guardar los datos del nuevo usuario?"
    val mensajeEditar="¿Desea editar los datos del usuario?"
    lateinit var selectTienda:SelectTienda

     private lateinit var menuSaveItem: MenuItem

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_registro_usuario,menu)
        menuSaveItem=menu!!.findItem(R.id.action_guardar)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item!!.itemId==R.id.action_guardar){
            try {
                GuardarNuevoUsuario(checkNotNull(findViewById(R.id.action_guardar)))
            }catch (e:Exception){
                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_configuracion_usuario)

            selectTienda=SelectTienda().newInstance(this,"Seleccione una Tienda")

            val fml = supportFragmentManager
            val ftl = fml.beginTransaction()
            ftl.replace(R.id.content_tienda,selectTienda)
            ftl.commit()
            estadoConfiguracion=intent.getIntExtra("estado",0)
            idUsuario=intent.getIntExtra("idUsuario",0)
              supportActionBar?.setTitle("Registro de usuario")
            adapter=ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,listaDescripciones)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            supportActionBar?.hide()
            supportActionBar?.setTitle("Registro de usuario" )
            asyncUser.setContext(this)
            asyncUser.setListenerRoles(listenerRoles)
            asyncUser.ObtenerRoles()
            adapter.add("Cargando datos...")
            spnTipoRol.adapter = adapter
            pb.visibility=View.GONE
            txtCargando.visibility=View.GONE
            txtCargando.visibility=View.GONE

            content_tienda.visibility=View.GONE
            ConfiguracionPantalla()


        }catch (e:Exception){
           Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT ).show()}
    }

    private fun ConfiguracionPantalla(){
        when(estadoConfiguracion){
            Constantes.EstadoConfiguracion.Nuevo->{
                edtNombreUsuario.isEnabled=true
                edtPin1.isEnabled=true
                edtPin3.isEnabled=true
                edtPin2.isEnabled=true
                edtPin4.isEnabled=true
                spnTipoRol.isEnabled=true

                content_tienda.visibility=View.VISIBLE
                supportActionBar?.show()
                pb.visibility=View.GONE
                txtCargando.visibility=View.GONE
            }
            Constantes.EstadoConfiguracion.Visualizar->{
                supportActionBar?.hide()
                supportActionBar?.setTitle( "Usuario" )
                edtNombreUsuario.isEnabled=false
                edtPin1.isEnabled=false
                edtPin3.isEnabled=false
                edtPin2.isEnabled=false
                edtPin4.isEnabled=false
                spnTipoRol.isEnabled=false
                pb.visibility=View.VISIBLE
                txtCargando.visibility=View.VISIBLE
                edtNombreUsuario.visibility=View.GONE
                edtPin1.visibility=View.GONE
                edtPin2.visibility=View.GONE
                edtPin3.visibility=View.GONE
                edtPin4.visibility=View.GONE
                spnTipoRol.visibility=View.GONE
                edtNombreUsuario.visibility=View.GONE
                txtRol.visibility=View.GONE
                txtTituloPin.visibility=View.GONE

                txtCargando.visibility=View.VISIBLE
             }
            Constantes.EstadoConfiguracion.Editar->{
                txtRol.visibility=View.GONE
                txtTituloPin.visibility=View.GONE
                supportActionBar?.hide()
                edtNombreUsuario.isEnabled=true
                edtPin1.isEnabled=true
                edtPin3.isEnabled=true
                edtPin2.isEnabled=true
                edtPin4.isEnabled=true
                supportActionBar?.setTitle("Editar usuario")
                edtNombreUsuario.visibility=View.GONE
                txtCargando.visibility=View.VISIBLE
                spnTipoRol.isEnabled=true
                pb.visibility=View.VISIBLE
                edtPin1.visibility=View.GONE
                edtPin2.visibility=View.GONE
                edtPin3.visibility=View.GONE
                edtPin4.visibility=View.GONE
                spnTipoRol.visibility=View.GONE

                txtCargando.visibility=View.VISIBLE
           }
        }
    }

    fun ObtenerUsuario(){
        asyncUser.ObtenerUsuarioId(idUsuario)
        asyncUser.setListenerResultadoUsuarioSolicitado(UsuarioPorId)
    }

    val UsuarioPorId=object:AsyncUsers.ListenerResultadoUsuarioSolicitado{
        override fun UsuarioObtenido(usuario: mUsuario?) {
            UsuarioObtenidoConsulta(usuario)
        }

        override fun ErrorObtener() {
            MensajeAdvertencia("Advertencia","Error al obtener la informacion del usuario.Codigo 999")
        }

        override fun ErrorConnection() {
            MensajeAdvertencia("Advertencia",
                    "Error al obtener la informacion del usuario.Verifique su conexion a internet.Codigo 998")
        }

    }
    fun UsuarioObtenidoConsulta(usuario: mUsuario?){
        var pin=usuario?.contrasena
        selectTienda!!.seleccionarTienda(usuario!!.idTienda)
        var posicionRol=0
        pb.visibility=View.GONE
        pb.visibility=View.GONE
        txtRol.visibility=View.VISIBLE
        txtTituloPin.visibility=View.VISIBLE
        edtNombreUsuario.visibility=View.VISIBLE
        edtPin1.visibility=View.VISIBLE
        edtPin2.visibility=View.VISIBLE
        edtPin3.visibility=View.VISIBLE
        edtPin4.visibility=View.VISIBLE
        content_tienda.visibility=View.VISIBLE
        spnTipoRol.visibility=View.VISIBLE

        txtCargando.visibility=View.GONE
        edtPin1.setText(pin?.substring(0,1))
        edtPin2.setText(pin?.substring(1,2))
        edtPin3.setText(pin?.substring(2,3))
        edtPin4.setText(pin?.substring(3,4))
        edtNombreUsuario.editText?.setText(usuario?.nombreUsuario)
        for(i in listaRoles.indices){
            if(listaRoles.get(i).idRol==usuario?.getmRol()!!.idRol){
                posicionRol=i
            }
        }
        posicionRol=posicionRol+1
        spnTipoRol.setSelection(posicionRol)
        if(estadoConfiguracion.equals(Constantes.EstadoConfiguracion.Visualizar)){
            menuSaveItem.isVisible=false
        }
        supportActionBar?.show()
    }

    val listenerRoles=object:AsyncUsers.ListenerRoles{
        override fun ObtenerRoles(mRolList: MutableList<mRol>?, listaDescripciones: MutableList<String>?) {
            cargarRolTerminar=true
            listaRoles.clear()
            listaRoles.addAll(ArrayList(mRolList!!))
            adapter.clear()
            adapter.addAll(listaDescripciones!!)
            when(estadoConfiguracion){

                Constantes.EstadoConfiguracion.Editar->{

                    ObtenerUsuario()
                }
                Constantes.EstadoConfiguracion.Visualizar->{

                    ObtenerUsuario()
                }
            }
        }
        override fun ErrorObtener() {
            finish()
            Toast.makeText(context,"Error en la conexión",Toast.LENGTH_SHORT).show()
        }

    }


    private fun RegistroUsuario(){

        permitir=false
        permitir=validarPin(edtPin1.text.toString())
        permitir=validarPin(edtPin2.text.toString())
        permitir=validarPin(edtPin3.text.toString())
        permitir=validarPin(edtPin4.text.toString())
        if(!cargarRolTerminar){
            permitir=false
        }
        if(edtNombreUsuario.editText!!.text.toString().length<3){
            permitir=false
            edtNombreUsuario.error="Ingrese un nombre mas largo"
        }
        else{
            edtNombreUsuario.error=null
        }
        if(spnTipoRol.selectedItemPosition==0){
            permitir=false
            txtRol.setText("Elija un Rol para el usuario")
            txtRol.setTextColor(Color.RED)
        }
        if(permitir && permitirTienda){
            edtNombreUsuario.error=null
            txtRol.setText("Rol de acceso")
            txtRol.setTextColor(Color.BLACK)
            txtTituloPin.setTextColor(Color.BLACK)
            txtTituloPin.setText("Permitir")
            usuario.idUsuario=idUsuario
            usuario.idTienda=idTienda
            usuario.nombreUsuario=edtNombreUsuario.editText!!.text.toString()
            usuario.contrasena=edtPin1.text.toString()+edtPin2.text.toString()+edtPin3.text.toString()+edtPin4.text.toString()
            usuario.getmRol().idRol=listaRoles.get(spnTipoRol.selectedItemPosition-1).idRol
            if(estadoConfiguracion.equals(Constantes.EstadoConfiguracion.Nuevo)) {
                asyncUser.RegistrarNuevoUsuario(usuario)
                asyncUser.setListenerRegistroUsuario(object : AsyncUsers.ListenerRegistroUsuario {
                    override fun NumeroExcedido(numeroUsuario: Int) {
                        AlertDialog.Builder(context)
                                .setMessage("Usted alcanzó el limite de usuarios.Número limite de $numeroUsuario usuarios")
                                .setTitle("Advertencia")
                                .setPositiveButton("Salir",null)
                                .create().show()
                    }

                    override fun RegistroExitoso() {
                        MensajeAdvertencia("Confirmacion", "Registro de usuario exitoso")
                        menuSaveItem.isVisible=false
                        menuSaveItem.isVisible=false
                    }
                    override fun ErrorRegistro() {
                        menuSaveItem.isVisible=false
                        MensajeAdvertencia("Error", "Error en el registro del usuario.Verifique su conexión")
                    }
                    override fun PinExiste() {
                        MensajeAdvertencia("Error", "El número PIN se encuentra en uso")
                        txtTituloPin.text = "Elija otro numero PIN"
                    }
                    override fun NombreUsuarioExiste() {
                        edtNombreUsuario.error = "Elija otro nombre de usuario"
                        MensajeAdvertencia("Error", "El nombre de usuario se encuentra en uso")
                    }
                    override fun ErrorRol() {
                        Toast.makeText(context, "11111111", Toast.LENGTH_SHORT).show()
                        menuSaveItem.isVisible=false
                        MensajeAdvertencia("Error", "Codigo de error E-999")
                    }


                })
            }
            else if(estadoConfiguracion.equals(Constantes.EstadoConfiguracion.Editar)) {
                asyncUser.EditarUsuario(usuario)
                asyncUser.setListenerEditarUsuario(object : AsyncUsers.ListenerEditarUsuario {
                    override fun ErrorNombreUsuario() {
                        edtNombreUsuario.error = "Elija otro nombre de usuario"
                        MensajeAdvertencia("Error", "El nombre de usuario se encuentra en uso")
                    }

                    override fun ErrorPin() {
                        MensajeAdvertencia("Error", "El número PIN se encuentra en uso")
                        txtTituloPin.text = "Elija otro numero PIN"
                    }

                    override fun EditarExito() {
                        MensajeAdvertencia("Confirmacion", "El usuario se actualizó con éxito")
                        menuSaveItem.isVisible=false
                    }

                    override fun ErrorEditar() {
                        menuSaveItem.isVisible=false
                        MensajeAdvertencia("Confirmacion", "Error al momento de actualizar")
                    }

                    override fun ErrorEditarConexion() {
                        menuSaveItem.isVisible=false
                        MensajeAdvertencia("Confirmacion", "Error al momento de actualizar.Verifique su conexion")
                    }
                })
            }
        }else{
            txtTituloPin.setText("Ingrese un pin correcto")
            txtTituloPin.setTextColor(Color.RED)
        }
    }
    private fun MensajeAdvertencia(titulo:String,mensaje:String){

        AlertDialog.Builder(this).
                setTitle(titulo).setMessage(mensaje).setPositiveButton("Aceptar",null)
                .create().show()
    }
    internal fun ConfirmacionGuardar(){

        var mensaje=""

        if(estadoConfiguracion.equals(Constantes.EstadoConfiguracion.Editar)){
            mensaje=mensajeEditar
        }else if(estadoConfiguracion.equals(Constantes.EstadoConfiguracion.Nuevo)){
            mensaje=mensajeNuevo
        }

        val dialog=AlertDialog.Builder(this).setMessage(mensaje)
                .setTitle("Alerta").setPositiveButton("Aceptar",object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        RegistroUsuario()
                    }
                }).setNegativeButton("Cancelar",null)
        dialog.create().show()
    }

    fun GuardarNuevoUsuario(view: View) {
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label =labelNuevo
                    icon = R.drawable.check //optional
                    callback = { //optional
                      ConfirmacionGuardar()
                    }
                }

            }
        }

        popupMenu.show(this@ConfiguracionUsuario, view)
    }
}
