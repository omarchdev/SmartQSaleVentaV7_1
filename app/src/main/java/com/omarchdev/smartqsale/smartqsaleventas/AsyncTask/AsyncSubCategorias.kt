package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSubCategoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AsyncSubCategorias{

    var resultadoSubCategorias:ResultadoSubCategorias?=null

    interface ResultadoSubCategorias{
        fun ErrorBusqueda()
        fun ResultadoBusqueda(listaSubCategoria:MutableList<mSubCategoria>?)
    }
    var listenerConfigSubCategoria:ListenerConfigSubCategoria?=null
    interface ListenerConfigSubCategoria{
        fun errorEditar()
        fun editarSubCategoria(subCategoria: mSubCategoria)
        fun subCategoriaAgregada(subCategoria:mSubCategoria)
        fun errorAgregarSubCategoria()
        fun ResultadoEliminarExito(mSubCategoria: mSubCategoria)
        fun ErrorEliminar()
    }
    fun ObtenerSubCategorias(id:Int){
         GlobalScope.launch  {
            var listaSubCategoria:MutableList<mSubCategoria>?=BdConnectionSql.getSinglentonInstance().ObtenerSubCategorias(id)
           launch(Dispatchers.Main){
                if(listaSubCategoria!=null) {
                    resultadoSubCategorias?.ResultadoBusqueda(listaSubCategoria)
                }else{
                    resultadoSubCategorias?.ErrorBusqueda()
                }
            }
        }
    }
    fun AgregarSubCategoria(id:Int,descripcion:String){
         GlobalScope.launch  {
            var subCategoria=BdConnectionSql.getSinglentonInstance().AgregarSubCategoria(id,descripcion)
           launch(Dispatchers.Main){
                if(subCategoria.idSubCategoria!=-99){
                    listenerConfigSubCategoria?.subCategoriaAgregada(subCategoria)
                }else{
                    listenerConfigSubCategoria?.errorAgregarSubCategoria()
                }
            }
        }
    }

    fun EditarSubCategoria(subCategoria: mSubCategoria){
        GlobalScope.launch {
            var subCategoria=BdConnectionSql.getSinglentonInstance().EditarSubCategoria(subCategoria)
           launch(Dispatchers.Main){

                if(subCategoria.idSubCategoria!=-99) {
                    listenerConfigSubCategoria?.editarSubCategoria(subCategoria)
                }
                else {
                    listenerConfigSubCategoria?.errorEditar()
                }
            }
        }

    }

    fun EliminarSubCategoria(subCategoria: mSubCategoria){

         GlobalScope.launch  {


           launch(Dispatchers.Main){

            }
        }
    }
}
