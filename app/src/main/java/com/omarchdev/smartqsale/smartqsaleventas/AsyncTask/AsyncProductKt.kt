package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioVenta
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICierreRepository
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsyncProductKt {
    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iProductoRepository = retro.create(IProductoRepository::class.java)
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

    /*ListaPrecioVenta*/

    interface IListaPreciosVentaConsulta{
        fun ResultListasPreciosVenta(listasPrecios:List<ListaPrecioVenta>)
    }

    var iListaPreciosVentaConsulta:IListaPreciosVentaConsulta?=null

    fun GetListasPreciosVenta(idProduct:Int){
        GlobalScope.launch  {
           var result= iProductoRepository.GetPreciosListaProducto(codeCia,BASECONN.TIPO_CONSULTA,idProduct).execute().body()
            launch(Dispatchers.Main){
                if(result!=null)
                iListaPreciosVentaConsulta?.ResultListasPreciosVenta(result)
            }
        }
    }
}