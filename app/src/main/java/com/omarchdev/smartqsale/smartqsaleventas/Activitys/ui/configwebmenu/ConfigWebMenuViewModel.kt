package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebmenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Model.IMenuItem

class ConfigWebMenuViewModel : ViewModel() {

    val ItemsMenu=MutableLiveData<ArrayList<IMenuItem>>().apply {
        val items=ArrayList<IMenuItem>()
        items.add(IMenuItem().apply {
            Id=1
            TituloItem="Link web de entrega"
            DescripcionItem=""
        })
        items.add(IMenuItem().apply {
            Id=2
            TituloItem="Whatsapp en web"
            DescripcionItem=""
        })
        items.add(IMenuItem().apply {
            Id=3
            TituloItem="Dias de entrega"
            DescripcionItem=""
        })
        items.add(IMenuItem().apply {
            Id=4
            TituloItem="Horario de entrega"
            DescripcionItem=""
        })
        items.add(IMenuItem().apply {
            Id=5
            TituloItem="Logo"
            DescripcionItem=""
        })
        items.add(IMenuItem().apply {
            Id=6
            TituloItem="Banner web"
            DescripcionItem=""
        })
        value=items
    }

    fun GetId(position:Int):Int=ItemsMenu.value!![position].Id

}
