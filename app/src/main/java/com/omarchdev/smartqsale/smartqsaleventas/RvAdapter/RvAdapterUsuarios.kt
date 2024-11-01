package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_cliente.view.*

class RvAdapterUsuarios(context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var listaUsuario=ArrayList<mUsuario>()
    val imagenesController=ImagenesController()
    val context=context
    private lateinit var listenerUsuarioClick:ListenerUsuarioClick

    fun setListenerUsuarioClick(listenerUsuarioClick:ListenerUsuarioClick){
        this.listenerUsuarioClick=listenerUsuarioClick
    }

    public interface ListenerUsuarioClick{

        public fun ObtenerIdUsuarioEditar(idUsuario:Int)
        public fun ObtenerIdUsuarioVisualizar(idUsuario: Int)
        public fun EliminarUsuario(idUsuario: Int,position:Int)
    }



    internal inner class UsuarioVh(itemView: View): RecyclerView.ViewHolder(itemView) {

        val txtNombre=itemView.txtNombreCliente
        val btnSetting=itemView.btnSetting
        val imgCliente=itemView.imgCliente
        val txtEmailCliente=itemView.txtEmailCliente
        val btnEdit=itemView.btnEdit
        val txtNumDocumento=itemView.txtNumDocumento

    }

    fun ActualizarListaUsuarios(listaUsuario:ArrayList<mUsuario>){
        this.listaUsuario=listaUsuario
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_cliente, parent, false)
        return UsuarioVh(v)
    }

    override fun getItemCount(): Int=listaUsuario.size

    fun ActualizarEliminar(position: Int){

        if(position>=0){

            listaUsuario.get(position).setcEstadoUsuario("E")
            notifyItemChanged(position)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      val h=holder as UsuarioVh
        h.txtNumDocumento.visibility=View.GONE
        h.btnEdit.visibility=View.GONE
        h.txtNombre.setText("${listaUsuario.get(position).nombreUsuario}")

        if(listaUsuario.get(position).getcEstadoUsuario().equals("E")){
             h.itemView.setBackgroundColor(Color.parseColor("#45EF5350"))


        }else if(listaUsuario.get(position).getcEstadoUsuario().equals("A")){
   //         h.itemView.setBackgroundColor(Color.parseColor("#ffffff"))

        }
        h.btnSetting.setOnClickListener {
            if(listaUsuario.get(position).getcEstadoUsuario().equals("A")){
                OpcionesUsuario(it,h.adapterPosition)
            }else if(listaUsuario.get(position).getcEstadoUsuario().equals("E")){
                MensajeAlerta("Advertencia","El usuario se encuentra eliminado")
            }
        }
        h.txtEmailCliente.setText("${listaUsuario.get(position).getmRol().getcDescripcion()}")
        h.imgCliente.setImageBitmap(imagenesController.textAsBitmap(listaUsuario.get(position).nombreUsuario))
    }

    internal fun MensajeAlerta(titulo:String,mensaje:String){
        val dialog=AlertDialog.Builder(context).setMessage(mensaje)
                .setTitle(titulo).setPositiveButton("Aceptar",null).
                        create().show()
    }

    fun OpcionesUsuario(view: View,position: Int) {
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label = "Editar"
                    icon = R.drawable.check //optional
                    callback = { //optional

                        if(listenerUsuarioClick!=null) {
                            listenerUsuarioClick.ObtenerIdUsuarioEditar(listaUsuario.get(position).idUsuario)
                        }

                    }
                }
                item{
                    label ="Visualizar"
                    callback={
                        if(listenerUsuarioClick!=null) {
                            listenerUsuarioClick.ObtenerIdUsuarioVisualizar(listaUsuario.get(position).idUsuario)
                        }

                    }
                }
                item{
                    label="Eliminar usuario"
                    callback={
                        if(listenerUsuarioClick!=null) {
                            listenerUsuarioClick.EliminarUsuario(listaUsuario.get(position).idUsuario,position)
                        }
                    }
                }

            }
        }

        popupMenu.show(context, view)
    }

}


















