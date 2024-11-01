package com.omarchdev.smartqsale.smartqsaleventas.Model

data class TipoPedido (val cod:String,val descripcion:String){
    override fun toString(): String {
        return "$descripcion"
    }
}

class ListTipoPedidos(){

    var list:ArrayList<TipoPedido>

    init {
        list= ArrayList()
        list.add(TipoPedido("01","Guardados"))
        list.add(TipoPedido("02","Anulados"))
        list.add(TipoPedido("00","Todos"))

    }


}