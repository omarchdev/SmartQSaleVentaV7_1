package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import android.os.AsyncTask
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IZonaServicioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class AsyncPedidos(context:Context){



    val bdConnectionSql=BdConnectionSql.getSinglentonInstance()
    var listenerObtenerPedidos:ListenerObtenerPedidos?=null
    private var idUltimoPedido=0
    var context=context
    var controladorProcesoCargar=ControladorProcesoCargar(context)
    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iPedidoRespository = retro.create(
        IPedidoRespository::class.java
    )
    var iZonaServicioRepository = retro.create(
        IZonaServicioRepository::class.java
    )

    interface ListenerGuardadoDetallePedido{


        fun ExitoGuardar()
        fun ErrorGuardar()

    }
    interface ListenerHoraActual{

        fun HoraActualEncontrada(time: Tiempo)
    }

    interface ListenerObtenerDatosPedidoCW{

        fun DatosPedidoCW(listaMarca:ArrayList<mMarca>,cabeceraPedido: mCabeceraPedido,listaOperarios:ArrayList<mOperario>,listaModelo: ArrayList<cModelo>)

    }


    interface ListenerObtenerPedidos{
        fun PedidosResultado(result:List<mCabeceraPedido>?)
        fun ErrorPedidos()

    }





    fun ObtenerPedidos(desde:String="",hasta:String="",idCliente:Int=0){
        var obtenerCabecerasPedidos=ObtenerCabecerasPedidos()
        obtenerCabecerasPedidos.desde=desde
        obtenerCabecerasPedidos.hasta=hasta
        obtenerCabecerasPedidos.idCliente=idCliente
        obtenerCabecerasPedidos.execute()

    }


    var resultadosPedidosTasks:ResultPedidosTask?=null

    public interface ResultPedidosTask{
        fun ResultadosConsultas(result: List<mCabeceraPedido>?)
        fun ErrorConsulta()
    }

    fun ObtenerUltimosPedidos(){

        ObtenerCabecerasPedidosTemp().execute(idUltimoPedido)
    }

    var iActualizarEstadoEntrega:IActualizarEstadoEntrega?=null
    public interface IActualizarEstadoEntrega{
         fun ResultActualizaEstado(estadoEntrega: EstadoEntrega)
   }
    fun ActualizaEstadoEntrega(idPedido:Int):EstadoEntrega{
        return BdConnectionSql.getSinglentonInstance().ActualizarEstadoEntrega(idPedido)
    }
    fun ActualizarEstadoEntregado(idPedido:Int){
         GlobalScope.launch {
            var result=ActualizaEstadoEntrega(idPedido)
           launch(Dispatchers.Main){
                iActualizarEstadoEntrega?.ResultActualizaEstado(result)
            }
        }

    }

    fun GuardarDetallesPedidoCW(pedido:mCabeceraPedido,listenerGuardadoDetallePedido: ListenerGuardadoDetallePedido){

         GlobalScope.launch  {

            var r=BdConnectionSql.getSinglentonInstance().GuardarDetallePedido(pedido)
           launch(Dispatchers.Main){
                when(r){

                    100.toByte()->listenerGuardadoDetallePedido.ExitoGuardar()
                    99.toByte()->listenerGuardadoDetallePedido.ErrorGuardar()
                }
            }
        }

    }

    inner class ObtenerCabecerasPedidosTemp():AsyncTask<Int,Void,List<mCabeceraPedido>>(){
        override fun doInBackground(vararg params: Int?): List<mCabeceraPedido> {
            return bdConnectionSql.ObtenerPedidosTimer(params[0]!!)
        }

        override fun onPostExecute(result: List<mCabeceraPedido>?) {
            super.onPostExecute(result)
            if(result!!.size>0){
                idUltimoPedido=result.get(0).idCabecera
                resultadosPedidosTasks?.ResultadosConsultas(result)
            }else{
                idUltimoPedido=0
                resultadosPedidosTasks?.ErrorConsulta()
            }
        }
    }

    inner class ObtenerCabecerasPedidos(): AsyncTask<Void, Void, List<mCabeceraPedido>?>() {
        var idCliente=0
        var desde=""
        var hasta=""

        override fun doInBackground(vararg params: Void?): List<mCabeceraPedido> {
            return bdConnectionSql.ObtenerPedidos(desde,hasta,idCliente)
        }

        override fun onPostExecute(result:List<mCabeceraPedido>?) {
            super.onPostExecute(result)

            if(listenerObtenerPedidos!=null){

                if(result!=null){
                    if(result.size>0){
                        idUltimoPedido=result.get(0).idCabecera
                    }else{
                        idUltimoPedido=0
                    }
                    listenerObtenerPedidos?.PedidosResultado(result)

                }else{

                    listenerObtenerPedidos?.ErrorPedidos()

                }

            }
        }
    }


    fun ObtenerTotalPedidoTienda(fi:String,ff:String,
                                 cIdentificador:String){

         GlobalScope.launch  {
            var lista = BdConnectionSql.getSinglentonInstance().ObtenerPedidosTiendaV2(fi, ff, cIdentificador)

           launch(Dispatchers.Main){
                listenerObtenerPedidos?.PedidosResultado(lista)
            }
        }

    }

    fun ObtenerHoraActual(listenerHoraActual: ListenerHoraActual){
         GlobalScope.launch  {
            var t=BdConnectionSql.getSinglentonInstance().ObtenerHoraActual()
           launch(Dispatchers.Main){
                listenerHoraActual.HoraActualEncontrada(t)
            }
        }

    }

    fun ObtenerDatosPedidoCW(idCabeceraPedido: Int,listenerObtenerDatosPedidoCW: ListenerObtenerDatosPedidoCW?){

         GlobalScope.launch  {

            val marca=iZonaServicioRepository.ObtenerMarcasAutos(codeCia,TIPO_CONSULTA).execute().body()

            var cabeceraPedido=BdConnectionSql.
                    getSinglentonInstance().
                    ObtenerDatosPedidoCW(idCabeceraPedido)
            var operarios=BdConnectionSql.
                    getSinglentonInstance().
                    ObtenerOperariosPedido(idCabeceraPedido)

            val modelo=iZonaServicioRepository.ObtenerModelosCW(codeCia,TIPO_CONSULTA,Constantes.Tienda.cTipoZonaServicio).execute().body()

           launch(Dispatchers.Main){

                if(marca!=null && modelo!=null){

                    listenerObtenerDatosPedidoCW?.DatosPedidoCW(ArrayList(marca),
                        cabeceraPedido,ArrayList(operarios),ArrayList(modelo))
                }


            }
        }
    }

}
