package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncAreasProduccion{

    var listenerAreasProduccion:ListenerAreasProduccion?=null
    var listenerObtenerAreaProduccion:ListenerObtenerAreaProduccion?=null
    var listenerGuardarAreaProduccion:ListenerGuardarAreaProduccion?=null
    var listenerEliminarArea:ListenerEliminarArea?=null
    var context: Context?=null

    interface ListenerObtenerAreaProduccion{
        fun ResultadoObtenerArea(area:mAreaProduccion)
        fun ErrorConsultaArea()
    }
    interface ListenerAreasProduccion{
        fun ResultadosBusquedaArea(result:MutableList<mAreaProduccion>)
        fun ErrorConsultaAreas()
    }

    interface ListenerGuardarAreaProduccion{
        fun GuardarExito()
        fun ErrorGuardar()
    }

    interface ListenerEliminarArea{
        fun EliminarExito()
        fun ErrorEliminar()
    }
    fun EditarAreaProduccion(area: mAreaProduccion?){

        var controladorProcesoCargar=ControladorProcesoCargar(context)
        controladorProcesoCargar.IniciarDialogCarga("Editando Area")
         GlobalScope.launch  {

            var respuesta:Byte?=BdConnectionSql.getSinglentonInstance()?.EditarAreaProduccion(area)
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()
                if(respuesta==100.toByte()) {
                    listenerGuardarAreaProduccion?.GuardarExito()
                }else if(respuesta==99.toByte()){
                    listenerGuardarAreaProduccion?.ErrorGuardar()
                }
            }
        }
    }




    fun GuardarAreaProduccion(area:mAreaProduccion?){

         GlobalScope.launch  {
            var respuesta:Byte?=BdConnectionSql.getSinglentonInstance().GuardarAreaProduccion(area)
           launch(Dispatchers.Main){
                if(respuesta==100.toByte()){
                    listenerGuardarAreaProduccion?.GuardarExito()
                }else if(respuesta==99.toByte()){
                    listenerGuardarAreaProduccion?.ErrorGuardar()
                }
            }
        }
    }
    fun getAreaPorId(idArea:Int){
         GlobalScope.launch  {
            var areaProduccion:mAreaProduccion?=BdConnectionSql.getSinglentonInstance().getAreaPorId(idArea)
           launch(Dispatchers.Main){
                if(areaProduccion!=null){
                    listenerObtenerAreaProduccion?.ResultadoObtenerArea(areaProduccion)
                }
                else{
                    listenerObtenerAreaProduccion?.ErrorConsultaArea()
                }
            }
        }
    }

    fun ObtenerAreasProduccionListado(){

         GlobalScope.launch  {
            var listadoAreas=BdConnectionSql.getSinglentonInstance().areasProduccion
           launch(Dispatchers.Main){
                  if (listadoAreas != null) {
                      listenerAreasProduccion?.ResultadosBusquedaArea(ArrayList(listadoAreas))
                  } else {
                       listenerAreasProduccion?.ErrorConsultaAreas()
                 }
            }
        }
    }

    fun EliminarAreaProduccion(area:mAreaProduccion){
         GlobalScope.launch  {
            var respuesta=BdConnectionSql.getSinglentonInstance().EliminarAreaProduccion(area)
            launch(Dispatchers.Main){
                when (respuesta){
                    100.toByte()->listenerEliminarArea?.EliminarExito()
                    99.toByte()->listenerEliminarArea?.ErrorEliminar()
                }
               }
        }
    }


}
