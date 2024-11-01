package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.bg
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.TextWatcherNumber
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.cv_item_categoria_precio.view.*
import java.math.BigDecimal
import java.util.ArrayList

class RvAdapterPrecioCategoriaV2(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = ArrayList<CategoriaPack>()
    var positionMostrar = -1
    private val listListener=ArrayList<TextWatcherNumber>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_categoria_precio, parent, false)
        return ItemVh(v)
    }



    inner class ItemVh(item: View):RecyclerView.ViewHolder(item){

        val txtCategoria=item.txtCategoria
        val edtPrecioCategoria=item.edtPrecioCategoria
        val txtPrecio=item.txtPrecio
        val btnEdit=item.btnEdit
        val imgSave=item.imgSave
        val imgCancelar=item.imgCancelar


    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as ItemVh

        h.txtPrecio.setText(list.get(position).precio.fortMoneda)
        h.txtCategoria.setText(list.get(position).descripcionCategoria)


        h.edtPrecioCategoria.setText(if(list.get(position).precio.fortMoneda.equals("0.00")){""}else{list.get(position).precio.fortMoneda})
        if(positionMostrar==position){
            listListener.get(position).edt=h.edtPrecioCategoria
          //  h.edtPrecioCategoria.addTextChangedListener(listListener.get(position))

            h.btnEdit.visibility=View.GONE
            h.imgSave.visibility=View.VISIBLE
            h.imgCancelar.visibility=View.VISIBLE
            h.edtPrecioCategoria.visibility=View.VISIBLE
        }else{

           h.btnEdit.visibility=View.VISIBLE
            h.imgSave.visibility=View.GONE
            h.imgCancelar.visibility=View.GONE
            h.edtPrecioCategoria.visibility=View.GONE
        }

        h.btnEdit.setOnClickListener {
            positionMostrar=position
            notifyDataSetChanged()
        }



        h.imgSave.setOnClickListener {

            if(h.edtPrecioCategoria.text.toString().isNotEmpty()){
                list.get(position).precio= BigDecimal(h.edtPrecioCategoria.text.toString())
            }else{


                list.get(position).precio=0.bg
            }
            positionMostrar=-1
            notifyDataSetChanged()
        }

        h.imgCancelar.setOnClickListener {
            positionMostrar=-1


            notifyDataSetChanged()


        }

    }

    fun AgregarElementos(list:ArrayList<CategoriaPack>){
        this.list.clear()
        this.list.addAll(list)
        list.forEach {
            listListener.add(TextWatcherNumber())
        }
        notifyDataSetChanged()
    }
}