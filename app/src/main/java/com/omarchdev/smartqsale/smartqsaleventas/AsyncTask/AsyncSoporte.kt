package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncSoporte{

    var respuestaPin:RespuestaPin?=null

    interface RespuestaPin{
        fun PermitirReinicio()
        fun NoPermitirReinicio()
        fun ErrorProcedimient()
        fun ErrorConexion()
    }
    fun VerificarPinReinicio(pin:String){
         GlobalScope.launch  {
            var respuesta=BdConnectionSql.getSinglentonInstance().VerificarPinReinicio(pin)
           launch(Dispatchers.Main){
                when(respuesta){
                    Constantes.ResultadoPinReset.PermitirReinicio->respuestaPin?.PermitirReinicio()
                    Constantes.ResultadoPinReset.NoReinicio->respuestaPin?.NoPermitirReinicio()
                    Constantes.ResultadoPinReset.errorConexion->respuestaPin?.ErrorProcedimient()
                    Constantes.ResultadoPinReset.errorVerificar->respuestaPin?.ErrorConexion()
                }

            }
        }

    }

}