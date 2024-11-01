package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_cliente.view.*

class RvAdapterAlmacenes(context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val contexto=context
    val listadoAlmacenes=ArrayList<mAlmacen>()
    val imagenesController=ImagenesController()
    private lateinit var listenerClickItem: ListenerClickItem

    fun setListenerClickItem(listenerClickItem: ListenerClickItem){
        this.listenerClickItem=listenerClickItem
    }
    public interface ListenerClickItem{

        public fun getClickPosition(idAlmacen:Int,metodo:Int)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_cliente,parent,false)
        return ItemAlmacen(v)
    }

    override fun getItemCount(): Int=listadoAlmacenes.size


    internal class ItemAlmacen(item:View):RecyclerView.ViewHolder(item){
        val txtNombreCliente=item.txtNombreCliente
        val imgCliente=item.imgCliente
        val btnSetting=item.btnSetting
        val btnEdit=itemView.btnEdit
        val txtNumDocumento=itemView.txtNumDocumento
       }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as ItemAlmacen
        h.txtNumDocumento.visibility=View.VISIBLE
        h.btnEdit.visibility=View.GONE
        h.txtNombreCliente.text=listadoAlmacenes.get(position).descripcionAlmacen
        h.imgCliente.setImageBitmap(imagenesController.textAsBitmap(listadoAlmacenes.get(position).descripcionAlmacen))
        h.btnSetting.setOnClickListener {
                onSingleSectionWithIconsClicked(it!!,listadoAlmacenes.get(h.adapterPosition).idAlmacen)
        }
        h.txtNumDocumento.text=ObtenerNombreTienda(listadoAlmacenes.get(position).idTienda)
        if(listadoAlmacenes.get(position).isTienda){
            h.itemView.setBackgroundColor(Color.parseColor("#7DE57373"))
        }else{

        }
    }
    fun LimpiarLista(){
        this.listadoAlmacenes.clear()
        notifyDataSetChanged()
    }

    fun AgregarAlmacenes(listadoAlmacenes:ArrayList<mAlmacen>){
        this.listadoAlmacenes.addAll(listadoAlmacenes)
        notifyDataSetChanged()

     }
    fun onSingleSectionWithIconsClicked(view: View,idAlmacen:Int) {
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label = "Editar"
                    icon = R.drawable.check //optional
                    callback = { //optional
                       listenerClickItem.getClickPosition(idAlmacen,Constantes.EstadoConfiguracion.Editar)
                    }
                }
                item {
                    label = "Visualizar"
                    iconDrawable = ContextCompat.getDrawable(contexto, R.drawable.abc_ic_menu_paste_mtrl_am_alpha) //optional
                    callback = { //optional
                        listenerClickItem.getClickPosition(idAlmacen,Constantes.EstadoConfiguracion.Visualizar)
                    }
                }
            }
        }
        popupMenu.show(contexto, view)
    }
}