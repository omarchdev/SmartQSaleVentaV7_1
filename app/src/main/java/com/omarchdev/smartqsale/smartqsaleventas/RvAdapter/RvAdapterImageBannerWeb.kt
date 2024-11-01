package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioImagen
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.item_image_banner.view.*

class RvAdapterImageBannerWeb : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface IRvAdapterImage {
        fun updateList(listMedios: ArrayList<MedioImagen>)
        fun itemDelete(medioImagen:MedioImagen)
    }

    val listMedios = ArrayList<MedioImagen>()
    var iRvAdapterImage: IRvAdapterImage? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.item_image_banner, parent, false)
        return ItemView(v)
    }

    fun upItem(position: Int) {
        if (position > 0) {


            val item = listMedios[position]
            listMedios.removeAt(position)
            listMedios.add(position - 1, item)
            notifyItemChanged(position)
            notifyItemChanged(position - 1)
            iRvAdapterImage?.updateList(listMedios)
        }
    }

    fun downItem(position: Int) {

        if (position < listMedios.size - 1) {
            val item = listMedios[position]
            listMedios.removeAt(position)
            listMedios.add(position + 1, item)
            notifyItemChanged(position)
            notifyItemChanged(position + 1)
            iRvAdapterImage?.updateList(listMedios)
        }
    }

    fun deleteImage(position: Int) {

        var item=listMedios[position]
        listMedios.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
        iRvAdapterImage?.itemDelete(item)
        iRvAdapterImage?.updateList(listMedios)
    }

    fun AddImage(bitmap: Bitmap) {
        try {

            val item = MedioImagen()
            item.bitmapImage = bitmap
            listMedios.add(item)
            notifyDataSetChanged()
            iRvAdapterImage?.updateList(listMedios)
        } catch (e: Exception) {
            Log.i("add-ca", e.toString())
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val h = holder as ItemView
        try {
            h.btnDown.setOnClickListener {
                downItem(position)
            }
            h.btnDeleteImg.setOnClickListener {
                deleteImage(position)
            }
            h.btnUpImg.setOnClickListener {
                upItem(position)
            }
            h.imgBanner.setImageBitmap(listMedios[position].ImageBitmap())

            h.txtNumOrden.text = (position + 1).toString()
        } catch (e: Exception) {
            Log.i("info-cat", e.toString())
        }


    }

    override fun getItemCount(): Int = listMedios.size
    inner class ItemView(item: View) : RecyclerView.ViewHolder(item) {

        val btnDown = item.btnDown
        val btnUpImg = item.btnUpImg
        val btnDeleteImg = item.btnDeleteImg
        val imgBanner = item.imgBanner
        val txtNumOrden = item.txtNumOrden
    }

    fun addList(list: ArrayList<MedioImagen>) {
        if (list != listMedios) {
            listMedios.clear()
            listMedios.addAll(list)
            notifyDataSetChanged()
        }

    }
}