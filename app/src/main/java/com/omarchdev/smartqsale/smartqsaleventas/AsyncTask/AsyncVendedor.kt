package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncVendedor{

    var listenerVendedores: AsyncVendedores.ListenerVendedores? = null

    fun ObtenerVendedoresTienda(idTienda:Int){

         GlobalScope.launch  {

            var lista:List<mVendedor>?=BdConnectionSql.getSinglentonInstance().getVendedorTienda(idTienda)
           launch(Dispatchers.Main){

                if(lista!=null){
                    listenerVendedores?.ObtenerVendedores(lista)
                }else{
                    listenerVendedores?.ErrorObtener()
                }

            }

        }

    }

}
