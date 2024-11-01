package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct

class cProducto(listProduct:ArrayList<mProduct>){

    var listProduct=listProduct

    fun FiltrarLista(texto:String):ArrayList<mProduct>{
        return  ArrayList(listProduct.filter { it->it.getcProductName().contains(texto,false) })
    }


}


