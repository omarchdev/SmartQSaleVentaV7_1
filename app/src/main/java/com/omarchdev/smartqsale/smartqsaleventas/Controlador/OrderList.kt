package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct

fun OrdernarList(lista:List<mProduct>):List<mProduct>{

    return lista.sortedBy { it->it.getcKey() }.sortedBy { it->it.getcProductName()}

}