package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncProductKt {

    private val bd=BdConnectionSql.getSinglentonInstance()

    interface IVerificarExisteNombre{
        fun NombreEnUso(mensaje:String)
        fun ErrorConexion(mensaje:String)
    }

    var iverificarExisteNombre:IVerificarExisteNombre?=null

    fun VerificarNombreExistenciaProducto(idProducto:Int,nombre:String){
         GlobalScope.launch  {
            var resultado=bd.VerificarExistenciaNombreArticulo(idProducto,nombre)

           launch(Dispatchers.Main){
                if(resultado.code==199){
                    iverificarExisteNombre?.NombreEnUso(resultado.mensajeResult)
                }else if(resultado.code==99){
                    iverificarExisteNombre?.ErrorConexion(resultado.mensajeResult)
                }
            }
        }


    }

    interface IProductosTiempoConsulta{

        fun ResultProductosTiempo(productos:List<mProduct>)

    }

    var iProductosTiempoConsulta:IProductosTiempoConsulta?=null

    fun GetProductosTiempo(){
         GlobalScope.launch  {
            val result=BdConnectionSql.getSinglentonInstance().GetProductsConTiempo()

           launch(Dispatchers.Main){
                iProductosTiempoConsulta?.ResultProductosTiempo(result)
            }
        }

    }

}