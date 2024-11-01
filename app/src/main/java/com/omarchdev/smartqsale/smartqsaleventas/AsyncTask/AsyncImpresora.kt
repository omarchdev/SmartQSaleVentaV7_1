package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.mImpresora
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AsyncImpresora(context: Context){

    var controladorProcesoCargar=ControladorProcesoCargar(context)
    var listenerImpresora:ListenerImpresora?=null
    var listenerResultadosImpresoras:ListenerResultadosImpresoras?=null
    interface ListenerImpresora{
        fun ExitoGuardar()
        fun ErrorGuardar()
        fun ExitoEliminar()
        fun ErrorEliminar()
        fun ObtenerImpresora(impresora: mImpresora)
        fun ErrorObtenerImpresora()
    }

    interface ListenerResultadosImpresoras{
        fun ImpresorasObtenidas(impresoras:ArrayList<mImpresora>)
        fun ErrorEncotrarImpresoras()
    }

    fun GuardarNuevaImpresora(impresora:mImpresora){

        controladorProcesoCargar.IniciarDialogCarga("Guardando impresora")
         GlobalScope.launch  {

            var byte=BdConnectionSql.getSinglentonInstance().GuardarNuevaImpresora(impresora)
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()
                if(byte==100.toByte()){
                    listenerImpresora?.ExitoGuardar()
                }else if(byte==99.toByte()){
                    listenerImpresora?.ErrorGuardar()
                }
            }
        }
    }

    fun EditarImpresora(impresora: mImpresora){
        controladorProcesoCargar.IniciarDialogCarga("Editando impresora")

         GlobalScope.launch  {
            var byte=BdConnectionSql.getSinglentonInstance().EditarImpresora(impresora)
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()

                when(byte){
                    100.toByte()->{
                        listenerImpresora?.ExitoGuardar()
                    }
                    99.toByte()->{
                        listenerImpresora?.ErrorGuardar()
                    }
                }
            }

        }
    }

    fun EliminarImpresora(id:Int){
        controladorProcesoCargar.IniciarDialogCarga("Eliminando impresora")
         GlobalScope.launch  {
            var byte=BdConnectionSql.getSinglentonInstance().EliminarImpresora(id)
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()
                when(byte){
                    100.toByte()->listenerImpresora?.ExitoEliminar()
                    99.toByte()->listenerImpresora?.ErrorEliminar()

                }
            }
        }
    }

    fun ObtenerImpresora(id:Int){
        controladorProcesoCargar.IniciarDialogCarga("Obteniendo impresora")
         GlobalScope.launch  {
            var impresora=BdConnectionSql.getSinglentonInstance().ObtenerImpresora(id)
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()
                if(impresora.idImpresora!=-99){
                    listenerImpresora?.ObtenerImpresora(impresora)
                }else{
                    listenerImpresora?.ErrorObtenerImpresora()
                }
            }
        }
    }

    fun ObtenerImpresoras(){

         GlobalScope.launch  {
            var lista=BdConnectionSql.getSinglentonInstance().ObtenerImpresoras()

           launch(Dispatchers.Main){

                if(lista.size>0){
                if(lista.get(0).idImpresora!=-99) {
                    listenerResultadosImpresoras?.ImpresorasObtenidas(ArrayList(lista))
                }else{
                    listenerResultadosImpresoras?.ErrorEncotrarImpresoras()
                }
                }

            }
        }
    }
}