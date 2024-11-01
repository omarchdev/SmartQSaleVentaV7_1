package com.omarchdev.smartqsale.smartqsaleventas.Model

class TipoEntregaPedido {

    var codeTipoEntrega=""
    var calleNumero=""
    var ciudadLocalidad=""
    var descripcionTipoEntrega=""
    var referencia=""

    fun getDireccion():String{
        var direccion=ciudadLocalidad.trim()+" "+calleNumero.trim()
        direccion=direccion.trim()

        if(direccion.length==0){
            return "-"
        }else{
            return direccion+ "\n"+referencia
        }
    }
}