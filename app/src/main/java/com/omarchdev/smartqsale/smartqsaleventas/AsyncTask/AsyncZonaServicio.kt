package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.util.Log
import com.google.gson.Gson
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IZonaServicioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsyncZonaServicio() {


    var listenerZonasServicio: ListenerZonasServicio? = null
    var listenerGuardarZonaServicioPedido: ListenerGuardarZonaServicioPedido? = null
    var listenerEliminarZonaPedido: ListenerEliminarZonaPedido? = null

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iPedidoRespository = retro.create(
        IPedidoRespository::class.java
    )
    var iZonaServicioRepository = retro.create(
        IZonaServicioRepository::class.java
    )

    interface ListenerUltimosPedidos {

        fun UltimosPedidosZona(listaVentas: ArrayList<mCabeceraVenta>)
    }

    interface ListenerEliminarZonaPedido {

        fun ExitoEliminar()
        fun ErrorEliminar()
    }

    interface ListenerZonaServicio {

        fun datosZonaServicio(
            zonaServicio: mZonaServicio,
            marcas: ArrayList<mMarca>,
            modelos: ArrayList<cModelo>
        )

    }

    interface ListenerGuardarZonaServicioPedido{


        fun GuardadoExito()
        fun ZonaServicioOcupado()
        fun ErrorGuardar()

    }

    interface ListenerActualizarZonaServicio {

        fun ActualizarExito()
        fun ErrorActualizar()
    }

    interface ListenerZonaServicioPedido{

        fun RegistroExito(respuesta: ResZonaServicio)
        fun ErrorRegistro()
        fun ExisteEnPedido(respuesta: ResZonaServicio)
    }

    interface ListenerZonasServicio {


        fun ResultadosZonaServicio(listaZonasServicio: ArrayList<mZonaServicio>)
        fun ResultadoGetTipoZonaServicio(tipoZonaServicio: TipoZonaServicio)

    }

    fun RegistrarZonaServicioPedido(
        cabeceraPedido: mCabeceraPedido,
        listenerZonaServicioPedido: ListenerZonaServicioPedido
    ) {
         GlobalScope.launch  {
            val sol = SolicitudEnvio(
                codeCia, TIPO_CONSULTA, cabeceraPedido,
                Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario
            )
            val demo = Gson().toJson(sol)
            val solicitud=iPedidoRespository.RegistrarZonaServicioPedido(sol)
            val exec=solicitud.execute()
            val body=exec.body()
            var respuesta =body
           launch(Dispatchers.Main){
                if (respuesta != null) {
                    Log.d("devres", respuesta.respuesta.toString())
                    when (respuesta.respuesta) {
                        100.toByte() -> listenerZonaServicioPedido.RegistroExito(respuesta)
                        99.toByte() -> listenerZonaServicioPedido.ErrorRegistro()
                        50.toByte() -> listenerZonaServicioPedido.ExisteEnPedido(respuesta)
                    }
                } else {
                    listenerZonaServicioPedido.ErrorRegistro()
                }
            }
        }
    }

    fun ObtenerUltimosServicios(
        idZonaServicio: Int,
        listenerUltimosPedidos: ListenerUltimosPedidos?
    ) {
         GlobalScope.launch  {
            val lista = iZonaServicioRepository.UltimasVentasZonaServicio(
                codeCia,
                TIPO_CONSULTA,
                idZonaServicio
            ).execute().body()

           launch(Dispatchers.Main){
                if (lista != null)
                    listenerUltimosPedidos?.UltimosPedidosZona(ArrayList(lista))
            }
        }
    }


    fun ObtenerDatosZonaServicioCW(
        idZonaServicio: Int,
        listenerZonaServicio: ListenerZonaServicio
    ) {
         GlobalScope.launch  {

            val zona =
                iZonaServicioRepository.DatosZonaServicio(codeCia, TIPO_CONSULTA, idZonaServicio)
                    .execute().body()
            val marcas =
                iZonaServicioRepository.ObtenerMarcasAutos(codeCia, TIPO_CONSULTA).execute().body()
            val modelo = iZonaServicioRepository.ObtenerModelosCW(
                codeCia,
                TIPO_CONSULTA,
                Constantes.Tienda.cTipoZonaServicio
            ).execute().body()
           launch(Dispatchers.Main){
                if (zona != null && marcas != null && modelo != null)
                    listenerZonaServicio.datosZonaServicio(
                        zona,
                        ArrayList(marcas),
                        ArrayList(modelo)
                    )
            }
        }
    }


    fun ActualizarZonaServicio(
        zonaServicio: mZonaServicio,
        listenerActualizarZonaServicio: ListenerActualizarZonaServicio
    ) {

         GlobalScope.launch  {
            val respuesta = iZonaServicioRepository.ActualizarZonaServicioCw(
                SolicitudEnvio<mZonaServicio>(
                    codeCia,
                    TIPO_CONSULTA,
                    zonaServicio,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario
                )
            ).execute().body()

           launch(Dispatchers.Main){
                if (respuesta != null) {
                    val r = respuesta.toByte()
                    when (r) {
                        100.toByte() -> listenerActualizarZonaServicio.ActualizarExito()
                        99.toByte() -> listenerActualizarZonaServicio.ErrorActualizar()
                    }
                } else {
                    listenerActualizarZonaServicio.ErrorActualizar()
                }

            }
        }
    }


    fun GuardarZonaServicio(
        zonaServicio: mZonaServicio,
        listenerActualizarZonaServicio: ListenerActualizarZonaServicio
    ) {

         GlobalScope.launch  {
            val respuesta = iZonaServicioRepository.GuardarZonaServicio(
                SolicitudEnvio<mZonaServicio>(
                    codeCia,
                    TIPO_CONSULTA,
                    zonaServicio,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario
                )
            ).execute().body()
           launch(Dispatchers.Main){
                if (respuesta != null) {
                    val r = respuesta.codeResult
                    when (r) {
                        200 -> listenerActualizarZonaServicio.ActualizarExito()
                        99 -> listenerActualizarZonaServicio.ErrorActualizar()
                    }
                } else {
                    listenerActualizarZonaServicio.ErrorActualizar()
                }

            }
        }
    }




    fun ObtenerTipoZonaServicio(idZonaServicio:Int){
         GlobalScope.launch {
            var tipoZona=TipoZonaServicio()
            try{
                tipoZona=iZonaServicioRepository.GetTipoZonaServicio(codeCia, TIPO_CONSULTA,idZonaServicio).execute().body()!!
            }catch (ex:Exception){
            }
           launch(Dispatchers.Main){
                listenerZonasServicio?.ResultadoGetTipoZonaServicio(tipoZona)
            }

        }
    }

    fun ObtenerZonasServicio(){

         GlobalScope.launch  {

            val lista =
                iZonaServicioRepository.ObtenerZonasServiciosM(codeCia, TIPO_CONSULTA).execute()
                    .body()
           launch(Dispatchers.Main){
                if (lista != null) {

                    listenerZonasServicio?.ResultadosZonaServicio(ArrayList(lista))
                }
            }
        }

    }


    fun EliminarZonaServicioPedido(idCabeceraPedido: Int){

         GlobalScope.launch  {
            val r = iZonaServicioRepository.EliminarZonaServicioPedido(
                codeCia,
                TIPO_CONSULTA, idCabeceraPedido
            ).execute().body()
           launch(Dispatchers.Main){
                if (r != null) {
                    when (r) {
                        100 -> {
                            listenerEliminarZonaPedido?.ExitoEliminar()
                        }
                        99 -> {
                            listenerEliminarZonaPedido?.ErrorEliminar()
                        }
                    }
                }

            }
        }

    }

    fun GuardarZonaServicioPedido(zonaServicio: mZonaServicio, idCabeceraPedido: Int) {

         GlobalScope.launch  {
            val zonaPedido=ZonaServicioPedido()
            zonaPedido.IdPedido=idCabeceraPedido
            zonaPedido.ZonaServicio=zonaServicio
            val r = iZonaServicioRepository.GuardarSeleccionZonaServicio(
                SolicitudEnvio(codeCia,
                    TIPO_CONSULTA,zonaPedido,Constantes.Terminal.idTerminal,Constantes.Usuario.idUsuario)).execute().body()

          /*  var r = BdConnectionSql.getSinglentonInstance()
                .GuardarSeleccionZonaServicio(zonaServicio, idCabeceraPedido)
       */
           launch(Dispatchers.Main){
                if(r!=null){
                    if (r.zonaServicio.idZona == -99) {
                        listenerGuardarZonaServicioPedido?.ErrorGuardar()
                    } else {
                        if (r.zonaServicio.cEstadoOcupado) {
                            listenerGuardarZonaServicioPedido?.ZonaServicioOcupado()
                        } else {
                            listenerGuardarZonaServicioPedido?.GuardadoExito()
                        }
                    }
                }else{
                    listenerGuardarZonaServicioPedido?.ErrorGuardar()
                }

            }
        }

    }

}


