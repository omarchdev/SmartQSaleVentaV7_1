package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import java.util.*

class SesionUsuario() {

    private val controlArchivoTxt=ControlArchivoTxt()
    private val c= Calendar.getInstance()
    fun ObtenerIdConfigHora():Int{
        var id=0
        if(!controlArchivoTxt!!.Existe(Constantes.TxtName.txtConfigTerminal)) {
          id=0
        }else{
            var r= controlArchivoTxt!!.LeerArchivo(Constantes.TxtName.txtConfigTerminal)
            if(r.isNotEmpty()) {
              id=r.get(0).toInt()
            }
        }
        return id
    }

    fun ReiniciarConteoUsuario(){

        var d=InfoSesionTemp()

        if(!controlArchivoTxt!!.Existe(Constantes.TxtName.txtConfigTerminal)) {
            d.frecuencia=0
        }else{
            var r= controlArchivoTxt!!.LeerArchivo(Constantes.TxtName.txtConfigTerminal)
            if(r.isNotEmpty()) {
                d.frecuencia=r.get(0).toInt()
                d.Temp=r.get(1).toLong()
                d.user=r.get(2).toInt()

                if(d.frecuencia!=0 && !d.permitirAcceso) {
                    GuardarFrecuenciaValidacionPin(d.frecuencia)
                }
            }
        }
    }

    fun BorrarSesion(){

        controlArchivoTxt.BorrarArchivo(Constantes.TxtName.txtConfigTerminal)

    }
    fun ObtenerInfoSesionTemp():InfoSesionTemp{

        var d=InfoSesionTemp()
        if(!controlArchivoTxt!!.Existe(Constantes.TxtName.txtConfigTerminal)) {
            d.frecuencia=0
        }else{
            var r= controlArchivoTxt!!.LeerArchivo(Constantes.TxtName.txtConfigTerminal)
            if(r.isNotEmpty()) {
                d.frecuencia=r.get(0).toInt()
                d.Temp=r.get(1).toLong()
                d.user=r.get(2).toInt()
            }
        }
        return d
    }



    fun GuardarFrecuenciaValidacionPin(frecuencia:Int){


        var mensaje="${frecuencia}\n${c.timeInMillis}\n${Constantes.Usuario.idUsuario}"
        controlArchivoTxt!!.GenerarArchivo(mensaje,Constantes.TxtName.txtConfigTerminal)
    }


}

class InfoSesionTemp{
    var frecuencia=0
    var Temp:Long?=null
    var user=0
    private val c= Calendar.getInstance()
    var permitirAcceso=false
        get() {
            var f = false
            if (Temp != null) {
                if(frecuencia!=0){
                    c.timeInMillis=Temp!!
                    c.add(Calendar.HOUR_OF_DAY,(frecuencia))
                   var horaActual=Calendar.getInstance()
                    if(c.after(horaActual)){
                        f=true
                    }
                }else{
                    f=false

                }


            }else{
               f=false
            }
                return f
            }
}

