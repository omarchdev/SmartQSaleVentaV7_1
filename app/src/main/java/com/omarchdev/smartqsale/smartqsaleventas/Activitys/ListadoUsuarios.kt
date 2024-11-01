package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.View
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncUsers
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterUsuarios
import kotlinx.android.synthetic.main.activity_listado_usuarios.*

class ListadoUsuarios : ActivityParent() {


    val rvAdapterUsuarios= RvAdapterUsuarios(this)
    val asyncUsers=AsyncUsers()
    var positionSelect=-1
    var idSelected=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_usuarios)
        rvUsuarios.adapter = rvAdapterUsuarios
        txtMensajeError.visibility = View.GONE
        ClickAgregarCliente()
        rvUsuarios.visibility = View.GONE
        asyncUsers.setContext(this)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        supportActionBar?.setTitle( "Usuarios")
        pb.visibility = View.GONE
        rvAdapterUsuarios.setListenerUsuarioClick(listenerUsuario)
    }

    fun MensajeAdvertenciaEliminar(){
        AlertDialog.Builder(this).setMessage("¿Desea ver al usuario seleccionado?")
                .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                    if(!asyncUsers.EliminarUsuario(idSelected)){
                        MensajeAdvertencia("Error","No puede eliminar su propio usuario")
                    }else{

                        asyncUsers.setListenerEliminarUsuario(object:AsyncUsers.ListenerEliminarUsuario{
                            override fun ErrorEliminar() {
                                MensajeAdvertencia("Error","Error al eliminar al usuario")
                            }
                            override fun EliminarExito() {
                                MensajeAdvertencia("Confirmación","Se eliminó al usuario con éxito")
                                rvAdapterUsuarios.ActualizarEliminar(positionSelect)
                            }
                            override fun ErrorConexion() {
                                MensajeAdvertencia("Error","Error al eliminar al usuario.Verifique su conexión")
                            }
                        })
                    }
                }).setNegativeButton("Cancelar",null).create().show()

    }

    val listenerUsuario=object:RvAdapterUsuarios.ListenerUsuarioClick{
        override fun EliminarUsuario(idUsuario: Int,position:Int) {

            idSelected=idUsuario
            positionSelect=position

            MensajeAdvertenciaEliminar()
        }

        override fun ObtenerIdUsuarioVisualizar(idUsuario: Int) {
            PantallaRegistroUsuario(idUsuario, Constantes.EstadoConfiguracion.Visualizar)

        }

        override fun ObtenerIdUsuarioEditar(idUsuario: Int) {
            PantallaRegistroUsuario(idUsuario, Constantes.EstadoConfiguracion.Editar)
        }
    }

    fun ClickAgregarCliente(){

        fbUsuario.setOnClickListener {
            PantallaRegistroUsuario(0,Constantes.EstadoConfiguracion.Nuevo)
         }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun PantallaRegistroUsuario(idUsuario:Int,estado:Int) {
        val intent = Intent(this, ConfiguracionUsuario::class.java)
        intent.putExtra("idUsuario",idUsuario)
        intent.putExtra("estado",estado)
        startActivity(intent)
    }

    internal fun MensajeAdvertencia(titulo:String,mensaje:String){
           AlertDialog.Builder(this).setMessage(mensaje)
                .setTitle(titulo).setPositiveButton("Aceptar",null)
                .create().show()
    }

    override fun onResume() {
        super.onResume()
        rvUsuarios.visibility= View.GONE
        pb.visibility=View.VISIBLE
        txtMensajeError.visibility= View.GONE
        asyncUsers.ObtenerUsuarios()
        asyncUsers.setListenerObtenerUsuarios(object:AsyncUsers.ListenerObtenerUsuarios{
           override fun UsuariosObtenidos(usuarioList: MutableList<mUsuario>?) {

                pb.visibility=View.GONE
                rvAdapterUsuarios.ActualizarListaUsuarios(ArrayList(usuarioList!!))
                rvUsuarios.visibility= View.VISIBLE
                txtMensajeError.visibility= View.GONE
            }

            override fun ErrorConnection() {
                txtMensajeError.visibility= View.VISIBLE
                pb.visibility=View.GONE
                txtMensajeError.text="Error al consultar los usuarios.Verifique su conexión"
            }

            override fun ErrorConsulta() {
                pb.visibility=View.GONE
                txtMensajeError.visibility= View.VISIBLE
                txtMensajeError.text="Error al consultar los usuarios.Verifique su conexión"
            }

        })

    }
}
