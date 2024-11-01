package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter

import android.hardware.usb.UsbDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.omarchdev.smartqsale.smartqsaleventas.Interface.ClickPositionItem
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.UsbConnect.getDescription
import kotlinx.android.synthetic.main.cv_item_device_disponible.view.*

class RvUsbDevice: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    var clickPosition:ClickPositionItem<UsbDevice>?=null
    private val listUsbDevice=ArrayList<UsbDevice>()

    fun agregarItems(listUsbDevice:List<UsbDevice>){
        this.listUsbDevice.clear()
        this.listUsbDevice.addAll(listUsbDevice)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v= LayoutInflater.from(p0.context).inflate(R.layout.cv_item_device_disponible,p0,false)
        return Item(v)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h=holder as Item
        try{
           h.txtDescripcion.text=listUsbDevice[position].getDescription()
        }catch (e:Exception){
            Toast.makeText(h.itemView.context,"rv-> "+e.toString(),Toast.LENGTH_SHORT).show()
        }

        h.itemView.setOnClickListener {
            clickPosition?.selectionItem(listUsbDevice[h.adapterPosition])
        }


    }


    inner class Item(item: View):RecyclerView.ViewHolder(item){
        val txtDescripcion=item.txtDescripcion

    }


    override fun getItemCount(): Int=listUsbDevice.size

}