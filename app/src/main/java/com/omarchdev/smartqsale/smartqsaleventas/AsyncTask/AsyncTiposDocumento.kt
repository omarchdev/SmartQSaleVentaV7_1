package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Interface.ListenerResultadosGeneric
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipoDocumento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncTiposDocumento(){

    var listenerBusquedaTipoDoc:ListenerResultadosGeneric<ArrayList<mTipoDocumento>>?=null
    fun obtenerTiposDocumento(){
            GlobalScope.launch  {
            var lista:MutableList<mTipoDocumento>?=BdConnectionSql.getSinglentonInstance().obtenerTiposDocumentoIndentificacion()
           launch(Dispatchers.Main){
                if(lista!=null) {
                    listenerBusquedaTipoDoc?.ObtenerResultadosBusqueda(ArrayList(lista))
                }else{
                    listenerBusquedaTipoDoc?.ErrorBusqueda()
                }
            }
        }
    }

}