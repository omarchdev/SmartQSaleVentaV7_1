package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen
    import com.github.zawadz88.materialpopupmenu.popupMenu
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_movimiento_almacen.view.*

class RvAdapterMovAlmacen: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val listaMovAlmacen:ArrayList<mMovAlmacen>
    var visible:Boolean
    private lateinit var listenerClickItem: ListenerClickItem
    fun setListenerClickItem(listenerClickItem: ListenerClickItem){
        this.listenerClickItem=listenerClickItem
    }
    public interface ListenerClickItem{
        public fun getClickPosition(position:Int,metodo:Byte)
    }


    private lateinit var contexto: Context
    init {
        listaMovAlmacen= ArrayList()
        visible=true

    }

    fun setContexto(contexto:Context){
        this.contexto=contexto
    }

    fun AgregarMovimiento(mov:mMovAlmacen){
        listaMovAlmacen.add(mov)
    }
    fun AgregarMovimientos(listMov:ArrayList<mMovAlmacen>){
        listaMovAlmacen.clear()
        listaMovAlmacen.addAll(listMov)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.cv_item_movimiento_almacen,parent,false)
        return MovAlmacenVH(v)
    }

    inner class MovAlmacenVH(item: View):RecyclerView.ViewHolder(item){
        val txtNumeroMovimiento=item.txtNumeroMovimiento
        val btnOption=item.btnOption

        val txtFechaMov=item.txtFechaMov

        val txtAlmacenes=item.txtAlmacenes
        val txtDescripcionMovimiento=item.txtDescripcionMovimiento
        val txtTipoMov=item.txtTipoMov
    }
    override fun getItemCount(): Int =listaMovAlmacen.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as MovAlmacenVH
        try{

            h.btnOption.setOnClickListener {
                when (listaMovAlmacen.get( holder.adapterPosition).getcEstadoRegistro()) {
                    "P" -> {
                        popUpMenuVisualizar(it,  holder.adapterPosition)
                    }
                    "A" -> {
                        onSingleSectionWithIconsClicked(it, holder.adapterPosition)
                    }
                }
            }
            if(listaMovAlmacen.get(position).getcISTipoMov().equals("I")){
                h.itemView.setBackgroundColor(Color.parseColor("#98FFFBB1"))
            }
            else if(listaMovAlmacen.get(position).getcISTipoMov().equals("S")){
                h.itemView.setBackgroundColor(Color.parseColor("#45EF5350"))

            }
            h.txtNumeroMovimiento.setText("${listaMovAlmacen.get(position).idMovAlmacen}")
            h.txtFechaMov.setText("Fecha Mov: ${listaMovAlmacen.get(position).fechaMov}")
            h.txtAlmacenes.setText(" ${listaMovAlmacen.get(position).almacen}")
            h.txtDescripcionMovimiento.setText("${listaMovAlmacen.get(position).descripcionMov}")
            h.txtTipoMov.setText("${listaMovAlmacen.get(position).tipoMovAlmacen}")

        }catch (ex:Exception){
            ex.toString();
        }
    }

    fun EliminarMovAlmacen(pos:Int){
       listaMovAlmacen.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun popUpMenuVisualizar(view: View,position: Int){
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label = "Visualizar"
                    iconDrawable = ContextCompat.getDrawable(contexto, R.drawable.abc_ic_menu_paste_mtrl_am_alpha) //optional
                    callback = { //optional
                        listenerClickItem.getClickPosition(position,102)
                    }
                }

            }
        }

        popupMenu.show(contexto, view)

    }

        fun onSingleSectionWithIconsClicked(view: View,position: Int) {
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label = "Editar"
                    icon = R.drawable.check //optional
                    callback = { //optional
            //            ProcesarGuardar();
                        listenerClickItem.getClickPosition(position,100)

                    }
                }
                item {
                    label = "Visualizar"
                    iconDrawable = ContextCompat.getDrawable(contexto, R.drawable.abc_ic_menu_paste_mtrl_am_alpha) //optional
                    callback = { //optional
                        listenerClickItem.getClickPosition(position,102)
                    }
                }
                item {
                    label = "Anular"
                    icon = R.drawable.abc_ic_menu_selectall_mtrl_alpha //optional
                    callback={
                        listenerClickItem.getClickPosition(position,101)

                    }
                }
            }
        }

        popupMenu.show(contexto, view)
    }

}