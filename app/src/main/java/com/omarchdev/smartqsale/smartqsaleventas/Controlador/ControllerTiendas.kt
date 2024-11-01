package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes

fun ObtenerNombreTienda(id:Int):String{
    var s=""
      Constantes.Tiendas.tiendaList.forEach {

        if(it.idTienda==id){

            s=it.nombreTienda
            return@forEach
        }

    }

    return s
}