package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.OperarioSelect
import com.omarchdev.smartqsale.smartqsaleventas.Model.mOperario
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_select_operario.view.*

class RvAdapterSelectOperario(listaOperario:ArrayList<OperarioSelect>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface ActionsOperarioList{
        fun clickEditOperario(idOperario:Int)
    }
    private val listaOperario=listaOperario
    private val limit=1
    var actionsOperarioList:ActionsOperarioList?=null
    var count=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.cv_item_select_operario,parent,false)
        return ItemVhSelectOperario(view)
    }

    inner class ItemVhSelectOperario(item: View):RecyclerView.ViewHolder(item){
        val cbOperario=item.cbOperario
        val btnEditaOperario=item.btnEditOperador
    }
    override fun getItemCount(): Int=listaOperario.size

    fun ObtenerOperariosSeleccionados():ArrayList<mOperario>{
        var lista=ArrayList<mOperario>()
        listaOperario.forEach {
            if(it.Select){
                lista.add(it.operario)
            }
        }
        return lista
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val h=holder as ItemVhSelectOperario
        try {
            h.cbOperario.setText(listaOperario.get(position).operario.ApellidoPaterno
                    + " " + listaOperario.get(position).operario.ApellidoMaterno + " " +
                    listaOperario.get(position).operario.PrimerNombre + " " +
                    listaOperario.get(position).operario.SegundoNombre)
            h.cbOperario.isChecked = listaOperario.get(position).Select

            h.btnEditaOperario.setOnClickListener {
                actionsOperarioList?.clickEditOperario(listaOperario[position].operario.idOperario)
            }

            h.cbOperario.setOnCheckedChangeListener { buttonView, isChecked ->
                if(listaOperario.get(position).Select){
                    listaOperario.get(position).Select=isChecked
                    count--
                }
                else{
                    if(count<limit) {
                        count++
                        listaOperario.get(position).Select = isChecked
                    }else{
                        buttonView.isChecked=false
                    }
                }

            }
        }catch (e:Exception){
            Toast.makeText(holder.itemView.context,e.toString(),Toast.LENGTH_LONG).show()
        }
    }

}